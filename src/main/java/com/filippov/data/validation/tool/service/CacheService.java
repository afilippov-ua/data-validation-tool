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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.filippov.data.validation.tool.model.CachingStatus.FINISHED;
import static com.filippov.data.validation.tool.model.CachingStatus.NON_DEFINED;
import static com.filippov.data.validation.tool.model.CachingStatus.RUNNING;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CacheService {

    private final MetadataService metadataService;
    private final DataStoragePairRepository dataStoragePairRepository;

    public List<ColumnDataInfoPair> getTablePairCacheDetails(Workspace workspace, TablePair tablePair) {
        log.debug("Getting table pair cache details for workspace: {} and table pair: {}", workspace.getId(), tablePair.getName());
        final List<ColumnDataInfoPair> result = metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair).stream()
                .map(columnPair -> getColumnPairCacheDetails(workspace, tablePair, columnPair))
                .collect(toList());

        log.debug("Table pair cache details were fetched successfully: {}", result);
        return result;
    }

    public ColumnDataInfoPair getColumnPairCacheDetails(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        log.debug("Getting column pair cache details for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        final ColumnDataInfoPair result = ColumnDataInfoPair.builder()
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

        log.debug("Column pair cache details has been successfully fetched for workspace: {}, table pair: {} and columnPair: {}. ColumnDataInfoPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName(), result);

        return result;
    }

    public void deleteCacheForColumnPair(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        log.debug("Deleting column cache for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

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

        log.debug("Column cache for workspace: {}, table pair: {} and columnPair: {} has been successfully deleted",
                workspace.getId(), tablePair.getName(), columnPair.getName());
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
        log.debug("Getting table pair cache status for workspace: {} and table pair: {}",
                workspace.getId(), tablePair.getName());

        final Map<ColumnPair, CachingStatus> result = metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair).stream()
                .collect(toMap(
                        Function.identity(),
                        columnPair -> getColumnPairCacheStatus(workspace, tablePair, columnPair)));

        log.debug("Returning table pair cache status for workspace: {} and table pair: {}",
                workspace.getId(), tablePair.getName());

        return result;
    }

    public CachingStatus getColumnPairCacheStatus(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        log.debug("Getting column pair cache status for workspace: {}, table pair: {} and column pair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

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
        final CachingStatus result = getCachingStatusPair(leftStatus, rightStatus);

        log.debug("Returning column pair cache status for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

        return result;
    }

    public CachingStatus startCaching(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        log.debug("Starting caching for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

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

        log.debug("Caching has been successfully started for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

        return CachingStatus.STARTED;
    }

    public CachingStatus stopCaching(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        log.debug("Stopping caching for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

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

        log.debug("Caching has been successfully stopped for workspace: {}, table pair: {} and columnPair: {}",
                workspace.getId(), tablePair.getName(), columnPair.getName());

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
