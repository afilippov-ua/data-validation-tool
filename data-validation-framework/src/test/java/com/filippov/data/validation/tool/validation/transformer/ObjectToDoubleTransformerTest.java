package com.filippov.data.validation.tool.validation.transformer;

import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToDoubleTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectToDoubleTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {1.0, 1.0},
                {-1.000001, -1.000001},
                {0.0, 0.0},

                {"1.0", 1.0},
                {"-1.000001", -1.000001},
                {"0.0", 0.0},

                {1, 1.0},
                {-1, -1.0},
                {0, 0.0},

                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Object value, Double expectedValue) {
        ObjectToDoubleTransformer transformer = new ObjectToDoubleTransformer();
        assertThat(transformer.transform(value)).isEqualTo(expectedValue);
    }
}
