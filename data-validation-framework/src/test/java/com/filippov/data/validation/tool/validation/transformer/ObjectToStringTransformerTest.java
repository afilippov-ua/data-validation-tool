package com.filippov.data.validation.tool.validation.transformer;

import com.filippov.data.validation.tool.validation.transformer.basic.ObjectToStringTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class ObjectToStringTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {"table1", "table1"},
                {"", ""},
                {1, "1"},
                {1.0, "1.0"},
                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(Object value, String expectedValue) {
        ObjectToStringTransformer transformer = new ObjectToStringTransformer();
        assertThat(transformer.transform(value)).isEqualTo(expectedValue);
    }
}
