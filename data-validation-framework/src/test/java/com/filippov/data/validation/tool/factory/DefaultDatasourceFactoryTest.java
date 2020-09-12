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

package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datastorage.RelationType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DefaultDatasourceFactoryTest extends AbstractTest {

    @Test
    void createJsonDatasourceTest() {
        final DefaultDatasourceFactory factory = new DefaultDatasourceFactory();

        final JsonDatasourceConfig config = JsonDatasourceConfig.builder()
                .metadataFilePath(LEFT_DS_METADATA_PATH)
                .dataFilePath(LEFT_DS_DATA_PATH)
                .maxConnections(1)
                .build();
        final Datasource datasource = factory.create(config);

        assertThat(datasource)
                .isNotNull()
                .isInstanceOf(JsonDatasource.class);
        assertThat(datasource.getConfig()).isEqualTo(config);
    }

    @Test
    void createTestInMemoryDatasourceTest() {
        final DefaultDatasourceFactory factory = new DefaultDatasourceFactory();

        final TestInMemoryDatasourceConfig config = TestInMemoryDatasourceConfig.builder()
                .relationType(RelationType.LEFT)
                .maxConnections(1)
                .build();

        final Datasource datasource = factory.create(config);

        assertThat(datasource)
                .isNotNull()
                .isInstanceOf(TestInMemoryDatasource.class);
        assertThat(datasource.getConfig()).isEqualTo(config);
    }

    @Test
    void incorrectInputTest() {
        final DefaultDatasourceFactory factory = new DefaultDatasourceFactory();

        assertThatThrownBy(() -> factory.create(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
