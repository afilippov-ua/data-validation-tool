package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import com.filippov.data.validation.tool.model.CachingStatus;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnDataInfoPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.filippov.data.validation.tool.model.CachingStatus.FINISHED;
import static com.filippov.data.validation.tool.model.CachingStatus.NON_DEFINED;
import static com.filippov.data.validation.tool.model.CachingStatus.RUNNING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Component
@RequiredArgsConstructor
public class CacheService {

    private final MetadataService metadataService;
    private final DataStoragePairRepository dataStoragePairRepository;

    public List<ColumnDataInfoPair> getTablePairCacheDetails(Workspace workspace, TablePair tablePair) {
        return metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair).stream()
                .map(columnPair -> getColumnPairCacheDetails(workspace, tablePair, columnPair))
                .collect(toList());
    }

    public ColumnDataInfoPair getColumnPairCacheDetails(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        return ColumnDataInfoPair.builder()
                .tablePair(tablePair)
                .columnPair(columnPair)
                .leftColumnDataInfo(
                        dataStoragePair.getLeftDataStorage().getColumnDataInfo(
                                Query.builder()
                                        .tablePair(tablePair)
                                        .columnPair(columnPair)
                                        .build()))
                .rightColumnDataInfo(
                        dataStoragePair.getRightDataStorage().getColumnDataInfo(
                                Query.builder()
                                        .tablePair(tablePair)
                                        .columnPair(columnPair)
                                        .build()))
                .build();
    }

    public void deleteCacheForColumnPair(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        dataStoragePair.getLeftDataStorage().deleteCache(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());
        dataStoragePair.getRightDataStorage().deleteCache(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());
    }

    public CachingStatus processCachingCommand(Workspace workspace, TablePair tablePair, ColumnPair columnPair, CacheFetchingCommand cacheFetchingCommand) {
        switch (cacheFetchingCommand) {
            case START:
                return startCaching(workspace, tablePair, columnPair);
            case STOP:
                return stopCaching(workspace, tablePair, columnPair);
            default:
                throw new UnsupportedOperationException("Unsupported cache fetching command: " + cacheFetchingCommand);
        }
    }

    public Map<ColumnPair, CachingStatus> getTablePairCacheStatus(Workspace workspace, TablePair tablePair) {
        return metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair).stream()
                .collect(toMap(
                        Function.identity(),
                        columnPair -> getColumnPairCacheStatus(workspace, tablePair, columnPair)));

    }

    public CachingStatus getColumnPairCacheStatus(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        final CachingStatus leftStatus = dataStoragePair.getLeftDataStorage()
                .getCachingStatus(
                        Query.builder()
                                .tablePair(tablePair)
                                .columnPair(columnPair)
                                .build());
        final CachingStatus rightStatus = dataStoragePair.getRightDataStorage()
                .getCachingStatus(
                        Query.builder()
                                .tablePair(tablePair)
                                .columnPair(columnPair)
                                .build());
        return getCachingStatusPair(leftStatus, rightStatus);
    }

    public CachingStatus startCaching(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        dataStoragePair.getLeftDataStorage().preloadInBackground(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());
        dataStoragePair.getRightDataStorage().preloadInBackground(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());
        return CachingStatus.STARTED;
    }

    public CachingStatus stopCaching(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        dataStoragePair.getLeftDataStorage().stopPreloadInBackground(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());
        dataStoragePair.getRightDataStorage().stopPreloadInBackground(
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());
        return CachingStatus.STOPPED;
    }

    private CachingStatus getCachingStatusPair(CachingStatus leftStatus, CachingStatus rightStatus) {
        if (leftStatus == RUNNING || rightStatus == RUNNING) {
            return RUNNING;
        } else if (leftStatus == FINISHED && rightStatus == FINISHED) {
            return FINISHED;
        } else {
            return NON_DEFINED;
        }
    }
}
