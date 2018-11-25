package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datastorage.cache.DiskColumnDataCache;
import org.junit.jupiter.api.Test;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

class DefaultDataStorageTest extends AbstractTest {
    private final DataStoragePair dataStoragePair = DataStoragePair.builder()
            .left(DefaultDataStorage.builder()
                    .relationType(LEFT)
                    .datasource(TEST_DATASOURCE_1)
                    .cache(new DiskColumnDataCache(CACHE_PATH))
                    .maxThreads(1)
                    .build())
            .right(DefaultDataStorage.builder()
                    .relationType(RIGHT)
                    .datasource(TEST_DATASOURCE_2)
                    .cache(new DiskColumnDataCache(CACHE_PATH))
                    .maxThreads(1)
                    .build())
            .build();

    @Test
    void test() {

    }
}
