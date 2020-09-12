package com.filippov.data.validation.tool.datasource.config;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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
        assertThatThrownBy(() -> new JsonDatasourceConfig(metadataFilePath, dataFilePath, maxConnections))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
