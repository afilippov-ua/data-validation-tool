/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.datasource.TestJsonDatasource;
import com.filippov.data.validation.tool.datasource.TestJsonDatasourceConfig;
import com.filippov.data.validation.tool.model.Datasource;
import com.filippov.data.validation.tool.model.UuidGenerator;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.cache.EvictionStrategy;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.model.pair.TablePair;
import com.filippov.data.validation.tool.transformer.TestObjectToStringTransformer;

public abstract class AbstractUnitTest {
    protected static final UuidGenerator UUID_GENERATOR = new TestUuidGenerator();
    protected static final String LEFT_DS_METADATA_PATH = "datasource1/metadata.json";
    protected static final String LEFT_DS_DATA_PATH = "datasource1/data.json";
    protected static final String RIGHT_DS_METADATA_PATH = "datasource2/metadata.json";
    protected static final String RIGHT_DS_DATA_PATH = "datasource2/data.json";

    protected static final String USERS = "users";
    protected static final String DEPARTMENTS = "departments";

    protected static final String USERS_ID = "id";
    protected static final String USERS_USERNAME = "username";
    protected static final String USERS_PASSWORD = "password";

    protected static final String DEPARTMENTS_ID = "id";
    protected static final String DEPARTMENTS_NAME = "name";
    protected static final String DEPARTMENTS_NUMBER_OF_EMPLOYEES = "number_of_employees";

    protected static final CacheConfig DEFAULT_CACHE_CONFIG = CacheConfig.builder()
            .evictionStrategy(EvictionStrategy.FIFO)
            .maxNumberOfElementsInCache(200)
            .build();

    protected static final Datasource LEFT_DATASOURCE = new TestJsonDatasource(
            TestJsonDatasourceConfig.builder()
                    .metadataFilePath(LEFT_DS_METADATA_PATH)
                    .dataFilePath(LEFT_DS_DATA_PATH)
                    .maxConnections(1)
                    .build());
    protected static final Datasource RIGHT_DATASOURCE = new TestJsonDatasource(
            TestJsonDatasourceConfig.builder()
                    .metadataFilePath(RIGHT_DS_METADATA_PATH)
                    .dataFilePath(RIGHT_DS_DATA_PATH)
                    .maxConnections(1)
                    .build());
    protected static final DatasourceColumn USERS_ID_COLUMN = LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID);
    protected static final DatasourceColumn USERS_USERNAME_COLUMN = LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME);
    protected static final DatasourceColumn USERS_PASSWORD_COLUMN = LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_PASSWORD);
    protected static final DatasourceColumn DEPARTMENTS_ID_COLUMN = LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_ID);
    protected static final DatasourceColumn DEPARTMENTS_NAME_COLUMN = LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NAME);
    protected static final DatasourceColumn DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN = LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES);

    protected static final DatasourceTable USERS_TABLE = LEFT_DATASOURCE.getMetadata().getTableByName(USERS);
    protected static final DatasourceTable DEPARTMENTS_TABLE = LEFT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);


    protected static final DatasourceTable LEFT_USERS_TABLE = LEFT_DATASOURCE.getMetadata().getTableByName(USERS);
    protected static final DatasourceTable RIGHT_USERS_TABLE = RIGHT_DATASOURCE.getMetadata().getTableByName(USERS);

    protected static final DatasourceTable LEFT_DEPARTMENTS_TABLE = LEFT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);
    protected static final DatasourceTable RIGHT_DEPARTMENTS_TABLE = RIGHT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);

    protected static final TablePair USERS_TABLE_PAIR = TablePair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .name(USERS)
            .leftDatasourceTable(LEFT_USERS_TABLE)
            .rightDatasourceTable(RIGHT_USERS_TABLE)
            .build();
    protected static final TablePair DEPARTMENTS_TABLE_PAIR = TablePair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .name(DEPARTMENTS)
            .leftDatasourceTable(LEFT_DEPARTMENTS_TABLE)
            .rightDatasourceTable(RIGHT_DEPARTMENTS_TABLE)
            .build();

    protected static final ColumnPair DEPARTMENTS_ID_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(DEPARTMENTS_TABLE_PAIR)
            .name(DEPARTMENTS_ID)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_ID))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_ID))
            .leftTransformer(new TestObjectToStringTransformer())
            .rightTransformer(new TestObjectToStringTransformer())
            .build();

    protected static final ColumnPair DEPARTMENTS_NAME_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(DEPARTMENTS_TABLE_PAIR)
            .name(DEPARTMENTS_NAME)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NAME))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NAME))
            .leftTransformer(new TestObjectToStringTransformer())
            .rightTransformer(new TestObjectToStringTransformer())
            .build();

    protected static final ColumnPair DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(DEPARTMENTS_TABLE_PAIR)
            .name(DEPARTMENTS_NUMBER_OF_EMPLOYEES)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES))
            .leftTransformer(new TestObjectToStringTransformer())
            .rightTransformer(new TestObjectToStringTransformer())
            .build();

    protected static final ColumnPair USERS_ID_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(USERS_TABLE_PAIR)
            .name(USERS_ID)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID))
            .leftTransformer(new TestObjectToStringTransformer())
            .rightTransformer(new TestObjectToStringTransformer())
            .build();

    protected static final ColumnPair USERS_USERNAME_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(USERS_TABLE_PAIR)
            .name(USERS_USERNAME)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME))
            .leftTransformer(new TestObjectToStringTransformer())
            .rightTransformer(new TestObjectToStringTransformer())
            .build();

    protected static final ColumnPair USERS_PASSWORD_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(USERS_TABLE_PAIR)
            .name(USERS_PASSWORD)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_PASSWORD))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_PASSWORD))
            .leftTransformer(new TestObjectToStringTransformer())
            .rightTransformer(new TestObjectToStringTransformer())
            .build();

    static {
        USERS_TABLE_PAIR.setKeyColumnPair(USERS_ID_COLUMN_PAIR);
        DEPARTMENTS_TABLE_PAIR.setKeyColumnPair(DEPARTMENTS_ID_COLUMN_PAIR);
    }
}
