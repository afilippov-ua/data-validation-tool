package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.AbstractDataValidationToolTest;
import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.DatasourceDtoMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

public class MongoApplicationStorageTest extends AbstractDataValidationToolTest {

    @Autowired
    ApplicationStorage storage;
    DatasourceDtoMapper mapper = new DatasourceDtoMapper();

    @Test
    public void saveTest() {
        DataStoragePair dataStoragePair = DataStoragePair.builder()
                .left(new DefaultDataStorage(LEFT, new EmptyDatasource("localhost:10001"), new InMemoryColumnDataCache(), 1))
                .right(new DefaultDataStorage (RIGHT, new EmptyDatasource("localhost:10002"), new InMemoryColumnDataCache(), 1))
                .build();

        final String id = storage.putDatasourcePair(DatasourcePairDto.builder()
                .left(mapper.toDto(dataStoragePair.getLeft().getDatasource()))
                .right(mapper.toDto(dataStoragePair.getRight().getDatasource()))
                .build());
        assertThat(id).isNotNull().isNotEmpty();
        System.out.println("ID=" + id);
    }
}
