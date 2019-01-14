package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.ApplicationStorageBsonMapper;
import com.filippov.data.validation.tool.storage.mapper.DatasourceDtoMapper;
import com.mongodb.MongoClient;
import org.junit.jupiter.api.Test;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;

public class MongoApplicationStorageTest {
    private MongoApplicationStorage storage;
    private DatasourceDtoMapper mapper;

    public MongoApplicationStorageTest() {
        final String host = "localhost";
        final int port = 27017;
        final String dbName = "test_dvt";

        this.storage = new MongoApplicationStorage(new MongoClient(host, port).getDatabase(dbName), new ApplicationStorageBsonMapper());
        this.mapper = new DatasourceDtoMapper();
    }

    @Test
    public void saveTest() {
        DataStoragePair dataStoragePair = DataStoragePair.builder()
                .left(new DefaultDataStorage(LEFT, new EmptyDatasource("localhost:10001"), new InMemoryColumnDataCache(), 1))
                .right(new DefaultDataStorage(RIGHT, new EmptyDatasource("localhost:10002"), new InMemoryColumnDataCache(), 1))
                .build();

        final String id = storage.putDatasourcePair(DatasourcePairDto.builder()
                .left(mapper.toDto(dataStoragePair.getLeft().getDatasource()))
                .right(mapper.toDto(dataStoragePair.getRight().getDatasource()))
                .build());
        assertThat(id).isNotNull().isNotEmpty();
        System.out.println("ID=" + id);
    }
}
