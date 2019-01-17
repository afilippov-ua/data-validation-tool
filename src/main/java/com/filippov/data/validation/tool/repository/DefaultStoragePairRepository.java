package com.filippov.data.validation.tool.repository;

import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.datastorage.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.DtoMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
public class DefaultStoragePairRepository implements StoragePairRepository {
    private final ApplicationStorage applicationStorage;
    private final DtoMapper dtoMapper;

    public DefaultStoragePairRepository(ApplicationStorage applicationStorage, DtoMapper dtoMapper) {
        this.applicationStorage = applicationStorage;
        this.dtoMapper = dtoMapper;
    }

    @Override
    public List<DataStoragePair> get() {
        return applicationStorage.getDatasourcePairs().stream()
                .map(this::toDataStoragePair)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @Override
    public Optional<DataStoragePair> get(String id) {
        return applicationStorage.getDatasourcePair(id)
                .map(this::toDataStoragePair);
    }

    @Override
    public String put(DataStoragePair dataStoragePair) {
        return applicationStorage.putDatasourcePair(DatasourcePairDto.builder()
                .left(dtoMapper.toDto(dataStoragePair.getLeft().getDatasource()))
                .right(dtoMapper.toDto(dataStoragePair.getRight().getDatasource()))
                .build());
    }

    @Override
    public void delete(String id) {
        applicationStorage.deleteDatasourcePair(id);
    }

    private DataStoragePair toDataStoragePair(DatasourcePairDto datasourcePairDto) {
        return DataStoragePair.builder()
                .left(DefaultDataStorage.builder()
                        .relationType(RelationType.LEFT)
                        .datasource(dtoMapper.fromDto(datasourcePairDto.getLeft()))
                        .cache(new InMemoryColumnDataCache()) // TODO : load from config
                        .maxThreads(1) // TODO : load from config
                        .build())
                .right(DefaultDataStorage.builder()
                        .relationType(RelationType.RIGHT)
                        .datasource(dtoMapper.fromDto(datasourcePairDto.getRight()))
                        .cache(new InMemoryColumnDataCache()) // TODO : load from config
                        .maxThreads(1) // TODO : load from config
                        .build())
                .build();
    }
}
