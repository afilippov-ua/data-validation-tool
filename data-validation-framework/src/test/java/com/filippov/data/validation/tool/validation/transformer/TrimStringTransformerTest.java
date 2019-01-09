package com.filippov.data.validation.tool.validation.transformer;

import com.filippov.data.validation.tool.validation.transformer.specific.string.TrimStringTransformer;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.assertj.core.api.Assertions.assertThat;

public class TrimStringTransformerTest {

    static Object[][] valueProvider() {
        return new Object[][]{
                {"table1", "table1"},
                {"", ""},
                {" value", "value"},
                {"value ", "value"},
                {" value ", "value"},
                {null, null}
        };
    }

    @ParameterizedTest()
    @MethodSource("valueProvider")
    void transformerTest(String value, String expectedValue) {
        TrimStringTransformer transformer = new TrimStringTransformer();
        assertThat(transformer.transform(value)).isEqualTo(expectedValue);
    }
}
