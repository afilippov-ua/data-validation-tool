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

package com.filippov.data.validation.tool.datasource;

import com.filippov.data.validation.tool.datasource.config.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.filippov.data.validation.tool.model.DataType.INTEGER;
import static com.filippov.data.validation.tool.model.DataType.STRING;
import static java.util.Arrays.asList;

@Slf4j
@RequiredArgsConstructor
public class TestInMemoryDatasource implements Datasource {
    private static final String USERS = "users";
    private static final String DEPARTMENTS = "departments";

    private static final String ID = "id";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private static final String NAME = "name";
    private static final String NUMBER_OF_EMPLOYEES = "number_of_employees";

    private static final DatasourceColumn U_ID = DatasourceColumn.builder().tableName(USERS).name(ID).dataType(INTEGER).build();
    private static final DatasourceColumn U_NAME = DatasourceColumn.builder().tableName(USERS).name(USERNAME).dataType(STRING).build();
    private static final DatasourceColumn U_PASS = DatasourceColumn.builder().tableName(USERS).name(PASSWORD).dataType(STRING).build();

    private static final DatasourceColumn D_ID = DatasourceColumn.builder().tableName(DEPARTMENTS).name(ID).dataType(INTEGER).build();
    private static final DatasourceColumn D_NAME = DatasourceColumn.builder().tableName(DEPARTMENTS).name(NAME).dataType(STRING).build();
    private static final DatasourceColumn D_NUM = DatasourceColumn.builder().tableName(DEPARTMENTS).name(NUMBER_OF_EMPLOYEES).dataType(INTEGER).build();

    private static final DatasourceMetadata METADATA = DatasourceMetadata.builder()
            .tables(asList(
                    DatasourceTable.builder()
                            .name(USERS)
                            .primaryKey(ID)
                            .columns(asList(ID, USERNAME, PASSWORD))
                            .build(),
                    DatasourceTable.builder()
                            .name(DEPARTMENTS)
                            .primaryKey(ID)
                            .columns(asList(ID, NAME, NUMBER_OF_EMPLOYEES))
                            .build()))
            .columns(asList(U_ID, U_NAME, U_PASS, D_ID, D_NAME, D_NUM))
            .build();
    private final TestInMemoryDatasourceConfig config;
    private Map<String, Map<String, ColumnData<?, ?>>> dataMap;

    private Map<String, Map<String, ColumnData<?, ?>>> buildDataMap() {
        final Map<String, ColumnData<?, ?>> userColumns = new HashMap<>();
        if (config.getRelation() == RelationType.LEFT) {
            userColumns.put(ID, ColumnData.builder().keyColumn(U_ID).dataColumn(U_ID).keys(asList(1, 2, 3, 4, 5, 7))
                    .data(asList(1, 2, 3, 4, 5, 7)).build());
            userColumns.put(USERNAME, ColumnData.builder().keyColumn(U_ID).dataColumn(U_NAME).keys(asList(1, 2, 3, 4, 5, 7))
                    .data(asList("user1", "user2", "user3", "user4", "user5", "user7")).build());
            userColumns.put(PASSWORD, ColumnData.builder().keyColumn(U_ID).dataColumn(U_PASS).keys(asList(1, 2, 3, 4, 5, 7))
                    .data(asList("pass1", "pass2", "pass3", "pass4", "pass5", "pass7")).build());
        } else {
            userColumns.put(ID, ColumnData.builder().keyColumn(U_ID).dataColumn(U_ID).keys(asList(1, 2, 3, 4, 5, 6))
                    .data(asList(1, 2, 3, 4, 5, 6)).build());
            userColumns.put(USERNAME, ColumnData.builder().keyColumn(U_ID).dataColumn(U_NAME).keys(asList(1, 2, 3, 4, 5, 6))
                    .data(asList("user1", "user2_changed", "user3", "user4", "user5", "user6")).build());
            userColumns.put(PASSWORD, ColumnData.builder().keyColumn(U_ID).dataColumn(U_PASS).keys(asList(1, 2, 3, 4, 5, 6))
                    .data(asList("pass1", "pass2_changed", "pass3", "pass4", "pass5", "user6")).build());
        }

        final Map<String, ColumnData<?, ?>> departmentColumns = new HashMap<>();
        if (config.getRelation() == RelationType.LEFT) {
            departmentColumns.put(ID, ColumnData.builder().keyColumn(D_ID).dataColumn(D_ID).keys(asList(10, 20, 30, 40, 50, 70))
                    .data(asList(10, 20, 30, 40, 50, 70)).build());
            departmentColumns.put(NAME, ColumnData.builder().keyColumn(D_ID).dataColumn(D_NAME).keys(asList(10, 20, 30, 40, 50, 70))
                    .data(asList("dep1", "dep2", "dep3", "dep4", "dep5", "dep7")).build());
            departmentColumns.put(NUMBER_OF_EMPLOYEES, ColumnData.builder().keyColumn(D_ID).dataColumn(D_NUM).keys(asList(10, 20, 30, 40, 50, 70))
                    .data(asList(25, 50, 75, 100, 125, 175)).build());
        } else {
            departmentColumns.put(ID, ColumnData.builder().keyColumn(D_ID).dataColumn(D_ID).keys(asList(10, 20, 30, 40, 50, 60))
                    .data(asList(10, 20, 30, 40, 50, 60)).build());
            departmentColumns.put(NAME, ColumnData.builder().keyColumn(D_ID).dataColumn(D_NAME).keys(asList(10, 20, 30, 40, 50, 60))
                    .data(asList("dep1", "dep2_changed", "dep3", "dep4", "dep5", "dep6")).build());
            departmentColumns.put(NUMBER_OF_EMPLOYEES, ColumnData.builder().keyColumn(D_ID).dataColumn(D_NUM).keys(asList(10, 20, 30, 40, 50, 60))
                    .data(asList(25, -50, 75, 100, 125, 150)).build());
        }

        final Map<String, Map<String, ColumnData<?, ?>>> dataMap = new HashMap<>();
        dataMap.put(USERS, userColumns);
        dataMap.put(DEPARTMENTS, departmentColumns);

        return dataMap;
    }

    @Override
    public DatasourceConfig getConfig() {
        return config;
    }

    @Override
    public DatasourceMetadata getMetadata() {
        return METADATA;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query) {
        log.debug("Loading data from Test-in-memory-datasource with query: {}", query);
        if (dataMap == null) {
            dataMap = buildDataMap();
        }
        final ColumnData<K, V> result = (ColumnData<K, V>) Optional.ofNullable(
                Optional.ofNullable(dataMap.get(query.getTable().getName()))
                        .orElseThrow(() -> new IllegalArgumentException("Table with name: " + query.getTable().getName() + " wasn't found in metadata!"))
                        .get(query.getDataColumn().getName()))
                .orElseThrow(() -> new IllegalArgumentException("Column with name: " + query.getDataColumn().getName() + " for table: "
                        + query.getTable().getName() + " wasn't found in metadata!"));

        log.debug("Data from Test-in-memory-datasource has been successfully loaded with query: {}", query);
        return result;
    }

    @Override
    public DatasourceQuery toDatasourceQuery(Query query, RelationType relationType) {
        final DatasourceTable table = query.getTablePair().getDatasourceTableFor(relationType);
        return DatasourceQuery.builder()
                .table(table)
                .keyColumn(query.getTablePair().getKeyColumnPair().getColumnFor(relationType))
                .dataColumn(query.getColumnPair().getColumnFor(relationType))
                .build();
    }

    public String toString() {
        return "TestInMemoryDatasource(" + this.config.getRelation() + ")";
    }
}
