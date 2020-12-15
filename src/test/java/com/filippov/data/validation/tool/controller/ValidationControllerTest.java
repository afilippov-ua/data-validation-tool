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

import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.dto.validation.ValidationDataDto;
import com.filippov.data.validation.tool.model.binder.DataRow;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class ValidationControllerTest extends AbstractDataValidationApplicationTest {

    @Test
    public void getValidationResultsTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final ResponseEntity<ValidationDataDto> response = restTemplate.getForEntity(
                buildUrl(VALIDATION_RESULTS_PATH
                        + "/workspaces/" + workspaceId
                        + "/tablePairs/departments"
                        + "/columnPairs/name"
                        + "?offset=0&limit=100"),
                ValidationDataDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final ValidationDataDto validationDataDto = response.getBody();
        assertThat(validationDataDto).isNotNull();

        assertThat(validationDataDto.getTablePair().getName()).isEqualTo("departments");
        assertThat(validationDataDto.getKeyColumnPair().getName()).isEqualTo("id");
        assertThat(validationDataDto.getDataColumnPair().getName()).isEqualTo("name");
        assertThat(validationDataDto.getFailedRows().stream().map(DataRowDto::getKey).collect(toList()))
                .isEqualTo(asList(20, 60, 70));

        deleteWorkspace(workspaceId);
    }
}
