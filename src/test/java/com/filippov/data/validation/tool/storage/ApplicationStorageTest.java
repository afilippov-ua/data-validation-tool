package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.AbstractDataValidationToolTest;
import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.DtoMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

public class ApplicationStorageTest extends AbstractDataValidationToolTest {
    private final List<String> createdIds = new ArrayList<>();

    @Autowired
    private ApplicationStorage applicationStorage;
    @Autowired
    private DtoMapper dtoMapper;


    @Test(expected = IllegalArgumentException.class)
    public void getByIncorrectIdFormatTest() {
        applicationStorage.getDatasourcePair("incorrect object id");
    }

    public void getByNonExistentIdTest() {
        assertThat(applicationStorage.getDatasourcePair("5c40e54f9a234a2f0f65e32d")).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDatasourcePairByNullIdTest() {
        applicationStorage.getDatasourcePair(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullDatasourcePairTest() {
        applicationStorage.putDatasourcePair(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void deleteDatasourcePairByNullIdTest() {
        applicationStorage.deleteDatasourcePair(null);
    }

    @Test
    public void saveAndLoadDataStoragePairTest() {
        final DatasourcePairDto datasourcePairDto = newDatasourcePairDto();
        final String id = applicationStorage.putDatasourcePair(datasourcePairDto);
        assertThat(id).isNotNull().isNotEmpty();

        createdIds.add(id);

        final Optional<DatasourcePairDto> datasourcePair = applicationStorage.getDatasourcePair(id);
        assertThat(datasourcePair).isNotEmpty().hasValue(datasourcePairDto);
    }

    @Test
    public void saveAndDeleteDataStoragePairTest() {
        final DatasourcePairDto datasourcePairDto = newDatasourcePairDto();
        final String id = applicationStorage.putDatasourcePair(datasourcePairDto);
        assertThat(id).isNotNull().isNotEmpty();

        createdIds.add(id);

        final Optional<DatasourcePairDto> datasourcePair = applicationStorage.getDatasourcePair(id);
        assertThat(datasourcePair).isNotEmpty().hasValue(datasourcePairDto);

        applicationStorage.deleteDatasourcePair(id);

        final Optional<DatasourcePairDto> emptyResult = applicationStorage.getDatasourcePair(id);
        assertThat(emptyResult).isEmpty();
    }

    @After
    public void cleanUp() {
        createdIds.forEach(applicationStorage::deleteDatasourcePair);
    }

    private DataStoragePair newDataStoragePair() {
        return DataStoragePair.builder()
                .left(new DefaultDataStorage(LEFT, new EmptyDatasource("localhost:10001"), new InMemoryColumnDataCache(), 1))
                .right(new DefaultDataStorage(RIGHT, new EmptyDatasource("localhost:10002"), new InMemoryColumnDataCache(), 1))
                .build();
    }

    private DatasourcePairDto newDatasourcePairDto() {
        final DataStoragePair dataStoragePair = newDataStoragePair();
        return DatasourcePairDto.builder()
                .left(dtoMapper.toDto(dataStoragePair.getLeft().getDatasource()))
                .right(dtoMapper.toDto(dataStoragePair.getRight().getDatasource()))
                .build();
    }
}
