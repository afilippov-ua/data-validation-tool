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

import com.filippov.data.validation.tool.model.datasource.DatasourceType;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonDatasourceConfigTest {

    private static final String METADATA_FILE_PATH = "test-metadata-file-path";
    private static final String DATA_FILE_PATH = "test-data-file-path";

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {METADATA_FILE_PATH, DATA_FILE_PATH, 0},
                {METADATA_FILE_PATH, DATA_FILE_PATH, -1},
                {METADATA_FILE_PATH, DATA_FILE_PATH, Integer.MIN_VALUE},
                {null, DATA_FILE_PATH, 1},
                {METADATA_FILE_PATH, null, 1},
                {"", DATA_FILE_PATH, 1},
                {METADATA_FILE_PATH, "", 1}
        };
    }

    static Object[][] correctInputProvider() {
        return new Object[][]{
                {METADATA_FILE_PATH, DATA_FILE_PATH, 1},
                {METADATA_FILE_PATH, DATA_FILE_PATH, 10},
                {METADATA_FILE_PATH, DATA_FILE_PATH, Integer.MAX_VALUE}
        };
    }

    @ParameterizedTest
    @MethodSource("correctInputProvider")
    void jsonDatasourceConfigTest(String metadataFilePath, String dataFilePath, int maxConnections) {
        final JsonDatasourceConfig config = new JsonDatasourceConfig(metadataFilePath, dataFilePath, maxConnections);
        assertThat(config.getMetadataFilePath()).isEqualTo(metadataFilePath);
        assertThat(config.getDataFilePath()).isEqualTo(dataFilePath);
        assertThat(config.getDatasourceType()).isEqualTo(DatasourceType.JSON_DATASOURCE);
        assertThat(config.getMaxConnections()).isEqualTo(maxConnections);
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectMaxConnections(String metadataFilePath, String dataFilePath, int maxConnections) {
        Assertions.assertThatThrownBy(() -> new JsonDatasourceConfig(metadataFilePath, dataFilePath, maxConnections))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
