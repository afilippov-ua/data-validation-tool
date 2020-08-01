package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.utils.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.utils.uuid.UuidGenerator;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

public class AbstractTest {
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

    protected static final Datasource LEFT_DATASOURCE = new JsonDatasource(
            JsonDatasourceConfig.builder()
                    .metadataFilePath(LEFT_DS_METADATA_PATH)
                    .dataFilePath(LEFT_DS_DATA_PATH)
                    .maxConnections(1)
                    .build());
    protected static final Datasource RIGHT_DATASOURCE = new JsonDatasource(
            JsonDatasourceConfig.builder()
                    .metadataFilePath(RIGHT_DS_METADATA_PATH)
                    .dataFilePath(RIGHT_DS_DATA_PATH)
                    .maxConnections(1)
                    .build());

    protected final DefaultDataStorage leftStorage = DefaultDataStorage.builder()
            .config(DataStorageConfig.builder()
                    .relationType(LEFT)
                    .maxConnections(1)
                    .build())
            .datasource(LEFT_DATASOURCE)
            .cache(new InMemoryColumnDataCache())
            .build();

    protected final DefaultDataStorage rightStorage = DefaultDataStorage.builder()
            .config(DataStorageConfig.builder()
                    .relationType(RIGHT)
                    .maxConnections(1)
                    .build())
            .datasource(RIGHT_DATASOURCE)
            .cache(new InMemoryColumnDataCache())
            .build();

    protected final DataStoragePair storagePair = DataStoragePair.builder()
            .leftDataStorage(leftStorage)
            .rightDataStorage(rightStorage)
            .build();

    protected final UuidGenerator uuidGenerator = new RandomUuidRuntimeGenerator();
}
