package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.AbstractDataValidationToolTest;
import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.MongoDtoBsonMapper;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

public class MongoApplicationStorageTest extends AbstractDataValidationToolTest {
    private final List<String> newIds = new ArrayList<>();

    @Autowired
    private ApplicationStorage storage;
    @Autowired
    private MongoDtoBsonMapper mongoDtoBsonMapper;

    @Test(expected = IllegalArgumentException.class)
    public void getByIncorrectIdFormatTest() {
        storage.getDatasourcePair("incorrect object id");
    }

    public void getByNonExistentIdTest() {
        assertThat(storage.getDatasourcePair("5c40e54f9a234a2f0f65e32d")).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDatasourcePairByNullIdTest() {
        storage.getDatasourcePair(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void putNullDatasourcePairTest() {
        storage.putDatasourcePair(null);
    }
    @Test(expected = IllegalArgumentException.class)
    public void deleteDatasourcePairByNullIdTest() {
        storage.deleteDatasourcePair(null);
    }

    @Test
    public void saveAndLoadDataStoragePairTest() {
        final DatasourcePairDto datasourcePairDto = newDatasourcePairDto();
        final String id = storage.putDatasourcePair(datasourcePairDto);
        assertThat(id).isNotNull().isNotEmpty();

        newIds.add(id);

        final Optional<DatasourcePairDto> datasourcePair = storage.getDatasourcePair(id);
        assertThat(datasourcePair).isNotEmpty().hasValue(datasourcePairDto);
    }

    @Test
    public void saveAndDeleteDataStoragePairTest() {
        final DatasourcePairDto datasourcePairDto = newDatasourcePairDto();
        final String id = storage.putDatasourcePair(datasourcePairDto);
        assertThat(id).isNotNull().isNotEmpty();

        newIds.add(id);

        final Optional<DatasourcePairDto> datasourcePair = storage.getDatasourcePair(id);
        assertThat(datasourcePair).isNotEmpty().hasValue(datasourcePairDto);

        storage.deleteDatasourcePair(id);

        final Optional<DatasourcePairDto> emptyResult = storage.getDatasourcePair(id);
        assertThat(emptyResult).isEmpty();
    }

    @After
    public void cleanUp() {
        newIds.forEach(storage::deleteDatasourcePair);
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
                .left(mongoDtoBsonMapper.toDto(dataStoragePair.getLeft().getDatasource()))
                .right(mongoDtoBsonMapper.toDto(dataStoragePair.getRight().getDatasource()))
                .build();
    }
}
