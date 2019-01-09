package com.filippov.data.validation.tool.validation.transformer;

import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToIntegerTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectToIntegerTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {1, 1},
                {0, 0},
                {-1, -1},

                {"1", 1},
                {"0", 0},
                {"-1", -1},

                {1.67, 1},
                {0.0, 0},
                {-1.000001, -1},

                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Object value, Integer expectedValue) {
        ObjectToIntegerTransformer transformer = new ObjectToIntegerTransformer();
        assertThat(transformer.transform(value)).isEqualTo(expectedValue);
    }
}
