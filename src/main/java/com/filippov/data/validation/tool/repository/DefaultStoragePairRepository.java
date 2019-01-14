package com.filippov.data.validation.tool.repository;

import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.storage.ApplicationStorage;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.DatasourceDtoMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
public class DefaultStoragePairRepository implements StoragePairRepository {
    private final ApplicationStorage applicationStorage;
    private final DatasourceDtoMapper mapper;

    public DefaultStoragePairRepository(ApplicationStorage applicationStorage, DatasourceDtoMapper mapper) {
        this.applicationStorage = applicationStorage;
        this.mapper = mapper;
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
                .left(mapper.toDto(dataStoragePair.getLeft().getDatasource()))
                .right(mapper.toDto(dataStoragePair.getRight().getDatasource()))
                .build());
    }

    @Override
    public void delete(String id) {
        applicationStorage.deleteDatasourcePair(id);
    }

    private DataStoragePair toDataStoragePair(DatasourcePairDto datasourcePairDto) {
        return null;
    }
}
