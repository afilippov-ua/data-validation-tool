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

import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.testinmemorydatasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.testinmemorydatasource.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.DataStorageConfig;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.model.DataType;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.utils.uuid.RandomUuidRuntimeGenerator;
import com.filippov.data.validation.tool.utils.uuid.UuidGenerator;
import com.filippov.data.validation.tool.validation.transformer.datatype.obj.ObjectToIntegerTransformer;
import com.filippov.data.validation.tool.validation.transformer.datatype.obj.ObjectToStringTransformer;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static java.util.Arrays.asList;

public class AbstractUnitTest {
    protected static final UuidGenerator UUID_GENERATOR = new RandomUuidRuntimeGenerator();

    protected static final String USERS = "users";
    protected static final String DEPARTMENTS = "departments";

    protected static final String USERS_ID = "id";
    protected static final String USERS_USERNAME = "username";
    protected static final String USERS_PASSWORD = "password";

    protected static final String DEPARTMENTS_ID = "id";
    protected static final String DEPARTMENTS_NAME = "name";
    protected static final String DEPARTMENTS_NUMBER_OF_EMPLOYEES = "number_of_employees";

    protected static final DatasourceColumn USERS_ID_COLUMN = DatasourceColumn.builder()
            .tableName(USERS)
            .name(USERS_ID)
            .dataType(DataType.STRING)
            .build();
    protected static final DatasourceColumn USERS_USERNAME_COLUMN = DatasourceColumn.builder()
            .tableName(USERS)
            .name(USERS_USERNAME)
            .dataType(DataType.STRING)
            .build();
    protected static final DatasourceColumn USERS_PASSWORD_COLUMN = DatasourceColumn.builder()
            .tableName(USERS)
            .name(USERS_PASSWORD)
            .dataType(DataType.STRING)
            .build();
    protected static final DatasourceColumn DEPARTMENTS_ID_COLUMN = DatasourceColumn.builder()
            .tableName(DEPARTMENTS)
            .name(DEPARTMENTS_ID)
            .dataType(DataType.STRING)
            .build();
    protected static final DatasourceColumn DEPARTMENTS_NAME_COLUMN = DatasourceColumn.builder()
            .tableName(DEPARTMENTS)
            .name(DEPARTMENTS_NAME)
            .dataType(DataType.STRING)
            .build();
    protected static final DatasourceColumn DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN = DatasourceColumn.builder()
            .tableName(DEPARTMENTS)
            .name(DEPARTMENTS_NUMBER_OF_EMPLOYEES)
            .dataType(DataType.INTEGER)
            .build();

    protected static final DatasourceTable USERS_TABLE = DatasourceTable.builder()
            .name(USERS)
            .primaryKey(USERS_ID)
            .columns(asList(USERS_ID, USERS_USERNAME, USERS_PASSWORD))
            .build();
    protected static final DatasourceTable DEPARTMENTS_TABLE = DatasourceTable.builder()
            .name(DEPARTMENTS)
            .primaryKey(DEPARTMENTS_ID)
            .columns(asList(DEPARTMENTS_ID, DEPARTMENTS_NAME, DEPARTMENTS_NUMBER_OF_EMPLOYEES))
            .build();

    protected static final Datasource LEFT_DATASOURCE = new TestInMemoryDatasource(
            TestInMemoryDatasourceConfig.builder()
                    .relationType(LEFT)
                    .maxConnections(1)
                    .build());
    protected static final Datasource RIGHT_DATASOURCE = new TestInMemoryDatasource(
            TestInMemoryDatasourceConfig.builder()
                    .relationType(RIGHT)
                    .maxConnections(1)
                    .build());

    protected static final DefaultDataStorage LEFT_STORAGE = DefaultDataStorage.builder()
            .config(DataStorageConfig.builder()
                    .relationType(LEFT)
                    .maxConnections(1)
                    .build())
            .datasource(LEFT_DATASOURCE)
            .cache(new InMemoryColumnDataCache())
            .build();
    protected static final DefaultDataStorage RIGHT_STORAGE = DefaultDataStorage.builder()
            .config(DataStorageConfig.builder()
                    .relationType(RIGHT)
                    .maxConnections(1)
                    .build())
            .datasource(RIGHT_DATASOURCE)
            .cache(new InMemoryColumnDataCache())
            .build();

    protected static final DatasourceTable LEFT_USERS_TABLE = LEFT_DATASOURCE.getMetadata().getTableByName(USERS);
    protected static final DatasourceTable RIGHT_USERS_TABLE = RIGHT_DATASOURCE.getMetadata().getTableByName(USERS);

    protected static final DatasourceTable LEFT_DEPARTMENTS_TABLE = LEFT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);
    protected static final DatasourceTable RIGHT_DEPARTMENTS_TABLE = RIGHT_DATASOURCE.getMetadata().getTableByName(DEPARTMENTS);

    protected static final DataStoragePair STORAGE_PAIR = DataStoragePair.builder()
            .leftDataStorage(LEFT_STORAGE)
            .rightDataStorage(RIGHT_STORAGE)
            .build();

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
            .leftTransformer(new ObjectToStringTransformer())
            .rightTransformer(new ObjectToStringTransformer())
            .build();

    protected static final ColumnPair DEPARTMENTS_NAME_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(DEPARTMENTS_TABLE_PAIR)
            .name(DEPARTMENTS_NAME)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NAME))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NAME))
            .leftTransformer(new ObjectToStringTransformer())
            .rightTransformer(new ObjectToStringTransformer())
            .build();

    protected static final ColumnPair DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(DEPARTMENTS_TABLE_PAIR)
            .name(DEPARTMENTS_NUMBER_OF_EMPLOYEES)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(DEPARTMENTS, DEPARTMENTS_NUMBER_OF_EMPLOYEES))
            .leftTransformer(new ObjectToIntegerTransformer())
            .rightTransformer(new ObjectToIntegerTransformer())
            .build();

    protected static final ColumnPair USERS_ID_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(USERS_TABLE_PAIR)
            .name(USERS_ID)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_ID))
            .leftTransformer(new ObjectToStringTransformer())
            .rightTransformer(new ObjectToStringTransformer())
            .build();

    protected static final ColumnPair USERS_USERNAME_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(USERS_TABLE_PAIR)
            .name(USERS_USERNAME)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_USERNAME))
            .leftTransformer(new ObjectToStringTransformer())
            .rightTransformer(new ObjectToStringTransformer())
            .build();

    protected static final ColumnPair USERS_PASSWORD_COLUMN_PAIR = ColumnPair.builder()
            .id(UUID_GENERATOR.generateRandomUuid())
            .tablePair(USERS_TABLE_PAIR)
            .name(USERS_PASSWORD)
            .leftDatasourceColumn(LEFT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_PASSWORD))
            .rightDatasourceColumn(RIGHT_DATASOURCE.getMetadata().getColumnByName(USERS, USERS_PASSWORD))
            .leftTransformer(new ObjectToStringTransformer())
            .rightTransformer(new ObjectToStringTransformer())
            .build();

    protected static final Metadata METADATA = Metadata.builder()
            .tablePairs(asList(USERS_TABLE_PAIR, DEPARTMENTS_TABLE_PAIR))
            .columnPairs(asList(USERS_ID_COLUMN_PAIR, USERS_USERNAME_COLUMN_PAIR, USERS_PASSWORD_COLUMN_PAIR,
                    DEPARTMENTS_ID_COLUMN_PAIR, DEPARTMENTS_NAME_COLUMN_PAIR, DEPARTMENTS_NUMBER_OF_EMPLOYEES_COLUMN_PAIR))
            .build();

    static {
        USERS_TABLE_PAIR.setKeyColumnPair(USERS_ID_COLUMN_PAIR);
        DEPARTMENTS_TABLE_PAIR.setKeyColumnPair(DEPARTMENTS_ID_COLUMN_PAIR);
    }
}
