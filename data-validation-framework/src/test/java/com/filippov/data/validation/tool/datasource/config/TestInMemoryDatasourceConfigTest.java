package com.filippov.data.validation.tool.datasource.config;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.datastorage.RelationType;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static com.filippov.data.validation.tool.datastorage.RelationType.LEFT;
import static com.filippov.data.validation.tool.datastorage.RelationType.RIGHT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TestInMemoryDatasourceConfigTest {

    static Object[][] correctInputProvider() {
        return new Object[][]{
                {LEFT, 1},
                {LEFT, 10},
                {LEFT, Integer.MAX_VALUE},
                {RIGHT, 1},
                {RIGHT, 10},
                {RIGHT, Integer.MAX_VALUE}
        };
    }

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {LEFT, 0},
                {LEFT, -1},
                {LEFT, Integer.MIN_VALUE},
                {null, 1}
        };
    }

    @ParameterizedTest
    @MethodSource("correctInputProvider")
    void testInMemoryDatasourceConfigTest(RelationType relationType, int maxConnections) {
        final TestInMemoryDatasourceConfig config = new TestInMemoryDatasourceConfig(relationType, maxConnections);
        assertThat(config).isNotNull();
        assertThat(config.getDatasourceType()).isEqualTo(DatasourceType.TEST_IN_MEMORY_DATASOURCE);
        assertThat(config.getRelation()).isEqualTo(relationType);
        assertThat(config.getMaxConnections()).isEqualTo(maxConnections);
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void incorrectInputTest(RelationType relationType, int maxConnections) {
        assertThatThrownBy(() -> new TestInMemoryDatasourceConfig(relationType, maxConnections))
                .isInstanceOf(IllegalArgumentException.class);
    }
}