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

package com.filippov.data.validation.tool.model.serialization;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.datasource.DatasourceMetadata;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class DatasourceMetadataSerializationTest extends AbstractUnitTest {

    @Test
    @SneakyThrows
    void metadataSerializationTest() {
        final DatasourceMetadata metadata = DatasourceMetadata.builder()
                .tables(asList(USERS_TABLE, DEPARTMENTS_TABLE))
                .columns(asList(USERS_ID_COLUMN, USERS_USERNAME_COLUMN, DEPARTMENTS_ID_COLUMN, DEPARTMENTS_NAME_COLUMN))
                .build();

        final String result = new ObjectMapper().writeValueAsString(metadata);

        final DatasourceMetadata datasourceMetadata = new ObjectMapper().readValue(result, DatasourceMetadata.class);

        assertThat(datasourceMetadata).isNotNull();

        assertThat(datasourceMetadata.isTableExist(USERS)).isTrue();
        assertThat(datasourceMetadata.getTableByName(USERS))
                .isNotNull()
                .isEqualTo(USERS_TABLE);

        assertThat(datasourceMetadata.isTableExist(DEPARTMENTS)).isTrue();
        assertThat(datasourceMetadata.getTableByName(DEPARTMENTS))
                .isNotNull()
                .isEqualTo(DEPARTMENTS_TABLE);

        assertThat(datasourceMetadata.isColumnExist(USERS, USERS_ID)).isTrue();
        assertThat(datasourceMetadata.getColumnByName(USERS, USERS_ID))
                .isNotNull()
                .isEqualTo(USERS_ID_COLUMN);

        assertThat(datasourceMetadata.isColumnExist(USERS, USERS_USERNAME)).isTrue();
        assertThat(datasourceMetadata.getColumnByName(USERS, USERS_USERNAME))
                .isNotNull()
                .isEqualTo(USERS_USERNAME_COLUMN);

        assertThat(datasourceMetadata.isColumnExist(DEPARTMENTS, DEPARTMENTS_ID)).isTrue();
        assertThat(datasourceMetadata.getColumnByName(DEPARTMENTS, DEPARTMENTS_ID))
                .isNotNull()
                .isEqualTo(DEPARTMENTS_ID_COLUMN);

        assertThat(datasourceMetadata.isColumnExist(DEPARTMENTS, DEPARTMENTS_NAME)).isTrue();
        assertThat(datasourceMetadata.getColumnByName(DEPARTMENTS, DEPARTMENTS_NAME))
                .isNotNull()
                .isEqualTo(DEPARTMENTS_NAME_COLUMN);
    }
}
