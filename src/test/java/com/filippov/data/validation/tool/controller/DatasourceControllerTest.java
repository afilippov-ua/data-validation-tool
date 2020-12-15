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

package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.datasource.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.dto.datasource.DatasourceConfigParamsDefinitionDto;
import com.filippov.data.validation.tool.model.datasource.DatasourceType;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class DatasourceControllerTest extends AbstractDataValidationApplicationTest {

    @Test
    public void getAllDatasourcesTest() {
        final ParameterizedTypeReference<List<DatasourceType>> responseType = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<List<DatasourceType>> response =
                restTemplate.exchange(buildUrl(DATASOURCES_PATH + "/types"), HttpMethod.GET, null, responseType);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isEqualTo(Arrays.stream(DatasourceType.values()).collect(toList()));
    }

    @Test
    public void getDatasourceConfigParamsForTestInMemoryDatasourceTest() {
        final ResponseEntity<DatasourceConfigParamsDefinitionDto> response =
                restTemplate.getForEntity(buildUrl(DATASOURCES_PATH + "/" + DatasourceType.TEST_IN_MEMORY_DATASOURCE.toString() + "/configParams"), DatasourceConfigParamsDefinitionDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final DatasourceConfigParamsDefinitionDto paramsDefinition = response.getBody();
        assertThat(paramsDefinition).isNotNull();
        assertThat(paramsDefinition.getParamsDefinition()).isNotNull();
        assertThat(paramsDefinition.getParamsDefinition().containsKey(TestInMemoryDatasourceConfig.RELATION_TYPE)).isTrue();
        assertThat(paramsDefinition.getParamsDefinition().get(TestInMemoryDatasourceConfig.RELATION_TYPE)).isNotNull().isNotEmpty();
    }

    @Test
    public void getDatasourceConfigParamsForJsonDatasourceTest() {
        final ResponseEntity<DatasourceConfigParamsDefinitionDto> response =
                restTemplate.getForEntity(buildUrl(DATASOURCES_PATH + "/" + DatasourceType.JSON_DATASOURCE.toString() + "/configParams"), DatasourceConfigParamsDefinitionDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final DatasourceConfigParamsDefinitionDto paramsDefinition = response.getBody();
        assertThat(paramsDefinition).isNotNull();
        assertThat(paramsDefinition.getParamsDefinition()).isNotNull();
        assertThat(paramsDefinition.getParamsDefinition().containsKey(JsonDatasourceConfig.METADATA_FILE_PATH)).isTrue();
        assertThat(paramsDefinition.getParamsDefinition().get(JsonDatasourceConfig.METADATA_FILE_PATH)).isNotNull().isNotEmpty();
        assertThat(paramsDefinition.getParamsDefinition().containsKey(JsonDatasourceConfig.DATA_FILE_PATH)).isTrue();
        assertThat(paramsDefinition.getParamsDefinition().get(JsonDatasourceConfig.DATA_FILE_PATH)).isNotNull().isNotEmpty();
    }
}
