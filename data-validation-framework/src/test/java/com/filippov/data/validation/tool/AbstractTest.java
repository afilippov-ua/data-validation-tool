package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.TestJsonDatasource;
import com.filippov.data.validation.tool.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.metadata.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.metadata.uuid.UuidGenerator;
import com.filippov.data.validation.tool.pair.DataStoragePair;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

public class AbstractTest {
    protected static final String BASE_CACHE_PATH = "../cache";
    protected static final String KRYO_CACHE_PATH = BASE_CACHE_PATH + "/kryo";
    protected static final String EH_CACHE_PATH = BASE_CACHE_PATH + "/eh";

    protected static final String LEFT_DS_METADATA_PATH = "datasource1/metadata.json";
    protected static final String LEFT_DS_DATA_PATH = "datasource1/data.json";
    protected static final String RIGHT_DS_METADATA_PATH = "datasource2/metadata.json";
    protected static final String RIGHT_DS_DATA_PATH = "datasource2/data.json";

    protected static final String TABLE_A = "TableA";
    protected static final String TABLE_B = "TableB";
    protected static final String ID = "id";
    protected static final String INTEGER_COLUMN = "integerColumn";
    protected static final String DOUBLE_COLUMN = "doubleColumn";
    protected static final String STRING_COLUMN = "stringColumn";

    protected static final Datasource LEFT_DATASOURCE = new TestJsonDatasource(LEFT_DS_METADATA_PATH, LEFT_DS_DATA_PATH);
    protected static final Datasource RIGHT_DATASOURCE = new TestJsonDatasource(RIGHT_DS_METADATA_PATH, RIGHT_DS_DATA_PATH);

    protected final DefaultDataStorage leftStorage = DefaultDataStorage.builder()
            .config(DataStorageConfig.builder()
                    .relationType(LEFT)
                    .datasource(LEFT_DATASOURCE)
                    .maxConnections(1)
                    .build())
            .cache(new InMemoryColumnDataCache())
            .build();

    protected final DefaultDataStorage rightStorage = DefaultDataStorage.builder()
            .config(DataStorageConfig.builder()
                    .relationType(RIGHT)
                    .datasource(RIGHT_DATASOURCE)
                    .maxConnections(1)
                    .build())
            .cache(new InMemoryColumnDataCache())
            .build();

    protected final DataStoragePair storagePair = DataStoragePair.builder()
            .left(leftStorage)
            .right(rightStorage)
            .build();

    protected final UuidGenerator uuidGenerator = new RandomUuidRuntimeGenerator();
}
