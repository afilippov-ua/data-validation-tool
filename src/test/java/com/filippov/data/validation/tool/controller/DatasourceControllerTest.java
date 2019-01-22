package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.AbstractDataValidationToolTest;
import com.filippov.data.validation.tool.datasource.DatasourceType;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class DatasourceControllerTest extends AbstractDataValidationToolTest {

    @Autowired
    private DatasourceController datasourceController;

    @Test
    public void getAllDatasourceTypesTest() {
        assertThat(datasourceController.getAllDatasources())
                .isEqualTo(Arrays.stream(DatasourceType.values())
                        .sorted()
                        .collect(toList()));
    }

}
