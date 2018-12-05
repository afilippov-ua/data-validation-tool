package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.TestJsonDatasource;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.DataStoragePair;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.datastorage.cache.InMemoryColumnDataCache;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;

public class AbstractTest {
    protected static final Path CACHE_PATH = Paths.get("./../.cache");

    protected static final String LEFT_DS_METADATA_PATH = "datasource1/metadata.json";
    protected static final String LEFT_DS_DATA_PATH = "datasource1/data.json";

    protected static final String RIGHT_DS_METADATA_PATH = "datasource2/metadata.json";
    protected static final String RIGHT_DS_DATA_PATH = "datasource2/data.json";

    protected static final Datasource LEFT_DATASOURCE = new TestJsonDatasource(LEFT_DS_METADATA_PATH, LEFT_DS_DATA_PATH);
    protected static final Datasource RIGHT_DATASOURCE = new TestJsonDatasource(RIGHT_DS_METADATA_PATH, RIGHT_DS_DATA_PATH);

    protected static final String TABLE_A = "TableA";
    protected static final String TABLE_B = "TableB";

    protected static final String ID = "id";
    protected static final String INTEGER_COLUMN = "integerColumn";
    protected static final String DOUBLE_COLUMN = "doubleColumn";
    protected static final String STRING_COLUMN = "stringColumn";

    protected final DataStorage leftStorage = DefaultDataStorage.builder()
            .relationType(LEFT)
            .datasource(LEFT_DATASOURCE)
            .cache(new InMemoryColumnDataCache())
            .maxThreads(1)
            .build();

    protected final DataStorage rightStorage = DefaultDataStorage.builder()
            .relationType(RIGHT)
            .datasource(RIGHT_DATASOURCE)
            .cache(new InMemoryColumnDataCache())
            .maxThreads(1)
            .build();

    protected final DataStoragePair storagePair = DataStoragePair.builder()
            .left(leftStorage)
            .right(rightStorage)
            .build();
}
