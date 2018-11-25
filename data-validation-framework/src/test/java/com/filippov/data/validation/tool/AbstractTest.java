package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.TestJsonDatasource;

import java.nio.file.Path;
import java.nio.file.Paths;

public class AbstractTest {
    protected static final Path CACHE_PATH = Paths.get("./../.cache");

    protected static final String DS_1_METADATA_PATH = "datasource1/metadata.json";
    protected static final String DS_1_DATA_PATH = "datasource1/data.json";

    protected static final String DS_2_METADATA_PATH = "datasource2/metadata.json";
    protected static final String DS_2_DATA_PATH = "datasource2/data.json";

    protected static final Datasource TEST_DATASOURCE_1 = new TestJsonDatasource(DS_1_METADATA_PATH, DS_1_DATA_PATH);
    protected static final Datasource TEST_DATASOURCE_2 = new TestJsonDatasource(DS_2_METADATA_PATH, DS_2_DATA_PATH);
}
