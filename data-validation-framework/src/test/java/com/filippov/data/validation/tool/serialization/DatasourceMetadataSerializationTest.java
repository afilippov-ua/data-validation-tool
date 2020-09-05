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

package com.filippov.data.validation.tool.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.model.DataType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DatasourceMetadataSerializationTest extends AbstractTest {

//    @Test
//    @SneakyThrows
//    void metadataSerializationTest() {
//        final DatasourceTable table1 = DatasourceTable.builder().name("table1").build();
//        final DatasourceTable table2 = DatasourceTable.builder().name("table2").build();
//
//        final DatasourceColumn pk1 = DatasourceColumn.builder().tableName(table1.getName()).name("PK1").dataType(DataType.INTEGER).build();
//        final DatasourceColumn pk2 = DatasourceColumn.builder().tableName(table2.getName()).name("PK2").dataType(DataType.INTEGER).build();
//
//        final DatasourceColumn col1 = DatasourceColumn.builder().tableName(table1.getName()).name("col1").dataType(DataType.INTEGER).build();
//        final DatasourceColumn col2 = DatasourceColumn.builder().tableName(table2.getName()).name("col2").dataType(DataType.INTEGER).build();
//
//        table1.setPrimaryKey(pk1.getName());
//        table1.setColumns(asList(pk1.getName(), col1.getName()));
//
//        table2.setPrimaryKey(pk2.getName());
//        table2.setColumns(asList(pk2.getName(), col2.getName()));
//
//        final DatasourceMetadata metadata = DatasourceMetadata.builder()
//                .tables(asList(table1, table2))
//                .columns(asList(pk1, pk2, col1, col2))
//                .build();
//
//        String result = new ObjectMapper().writeValueAsString(metadata);
//
//        final DatasourceMetadata datasourceMetadata = new ObjectMapper().readValue(result, DatasourceMetadata.class);
//
//        assertThat(datasourceMetadata).isNotNull();
//
//        final DatasourceTable t1 = datasourceMetadata.getTableByName("table1").get();
//        final DatasourceTable t2 = datasourceMetadata.getTableByName("table2").get();
//
//        assertThat(datasourceMetadata.getColumnByName(t1.getName(), "PK1"))
//                .isNotEmpty()
//                .map(column -> column.getTableName())
//                .hasValue(t1.getName());
//        assertThat(datasourceMetadata.getColumnByName(t1.getName(), "col1"))
//                .isNotEmpty()
//                .map(DatasourceColumn::getTableName)
//                .hasValue(t1.getName());
//        assertThat(datasourceMetadata.getColumnByName(t2.getName(), "PK2"))
//                .isNotEmpty()
//                .map(DatasourceColumn::getTableName)
//                .hasValue(t2.getName());
//        assertThat(datasourceMetadata.getColumnByName(t2.getName(), "col2"))
//                .isNotEmpty()
//                .map(DatasourceColumn::getTableName)
//                .hasValue(t2.getName());
//    }
}
