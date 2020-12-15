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

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.TablePairDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static com.filippov.data.validation.tool.model.datasource.DatasourceType.TEST_IN_MEMORY_DATASOURCE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

public class WorkspaceControllerTest extends AbstractDataValidationApplicationTest {

    private static final WorkspaceDto TEST_WORKSPACE_1 = WorkspaceDto.builder()
            .name("test-workspace-name-1")
            .left(DatasourceDefinitionDto.builder()
                    .datasourceType(TEST_IN_MEMORY_DATASOURCE)
                    .maxConnections(1)
                    .configParams(Map.of("relationType", "LEFT"))
                    .build())
            .right(DatasourceDefinitionDto.builder()
                    .datasourceType(TEST_IN_MEMORY_DATASOURCE)
                    .maxConnections(2)
                    .configParams(Map.of("relationType", "RIGHT"))
                    .build())
            .build();

    private static final WorkspaceDto TEST_WORKSPACE_2 = WorkspaceDto.builder()
            .name("test-workspace-name-2")
            .left(DatasourceDefinitionDto.builder()
                    .datasourceType(TEST_IN_MEMORY_DATASOURCE)
                    .maxConnections(1)
                    .configParams(Map.of("relationType", "LEFT"))
                    .build())
            .right(DatasourceDefinitionDto.builder()
                    .datasourceType(TEST_IN_MEMORY_DATASOURCE)
                    .maxConnections(2)
                    .configParams(Map.of("relationType", "RIGHT"))
                    .build())
            .build();

    @Test
    public void getWorkspacesTest() {
        final String workspaceId_1 = createWorkspace(TEST_WORKSPACE_1).getBody();
        final String workspaceId_2 = createWorkspace(TEST_WORKSPACE_2).getBody();

        final ParameterizedTypeReference<List<WorkspaceDto>> responseType = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<List<WorkspaceDto>> response =
                restTemplate.exchange(buildUrl(WORKSPACES_PATH), HttpMethod.GET, null, responseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));
        assertThat(response.getBody()).isNotEmpty().hasSize(2);

        deleteWorkspace(workspaceId_1);
        deleteWorkspace(workspaceId_2);
    }

    @Test
    public void createAndGetWorkspaceTest() {
        final ResponseEntity<String> response = createWorkspace(TEST_WORKSPACE_1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE).stream()
                .anyMatch(val -> val.contains(TEXT_PLAIN_VALUE))).isTrue();
        final String workspaceId = response.getBody();
        assertThat(workspaceId).isNotEmpty();

        final ResponseEntity<WorkspaceDto> fetchedResponse = getWorkspace(workspaceId);

        assertThat(response).isNotNull();
        assertThat(fetchedResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(fetchedResponse.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(fetchedResponse.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final WorkspaceDto fetchedWorkspace = fetchedResponse.getBody();

        assertThat(fetchedWorkspace).isNotNull();
        assertThat(fetchedWorkspace.getId()).isEqualTo(workspaceId);
        assertThat(fetchedWorkspace.getName()).isEqualTo(TEST_WORKSPACE_1.getName());
        assertThat(fetchedWorkspace.getLeft()).isEqualTo(TEST_WORKSPACE_1.getLeft());
        assertThat(fetchedWorkspace.getRight()).isEqualTo(TEST_WORKSPACE_1.getRight());

        deleteWorkspace(workspaceId);
    }

    @Test
    public void deleteWorkspaceTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE_1).getBody();
        assertThat(workspaceId).isNotEmpty();

        final ResponseEntity<String> response = deleteWorkspace(workspaceId);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void getMetadataTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE_1).getBody();
        assertThat(workspaceId).isNotEmpty();

        final ResponseEntity<WorkspaceMetadataDto> response = restTemplate.exchange(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/metadata"),
                HttpMethod.GET,
                null,
                WorkspaceMetadataDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final WorkspaceMetadataDto metadata = response.getBody();
        assertThat(metadata).isNotNull();
        assertThat(metadata.getTablePairs()).isNotEmpty();
        assertThat(metadata.getTablePairs().keySet().stream().sorted().collect(toList()))
                .isEqualTo(asList("departments", "users"));
        assertThat(metadata.getColumnPairs()).isNotEmpty();
        assertThat(metadata.getColumnPairs().get("departments").keySet().stream().sorted().collect(toList()))
                .isEqualTo(asList("id", "name", "number_of_employees"));
        assertThat(metadata.getColumnPairs().get("users").keySet().stream().sorted().collect(toList()))
                .isEqualTo(asList("id", "password", "username"));

        deleteWorkspace(workspaceId);
    }

    @Test
    public void getTablePairsTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE_1).getBody();
        assertThat(workspaceId).isNotEmpty();

        final ParameterizedTypeReference<List<TablePairDto>> responseType = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<List<TablePairDto>> response = restTemplate.exchange(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/metadata/tablePairs"),
                HttpMethod.GET,
                null,
                responseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final List<TablePairDto> tablePairs = response.getBody();
        assertThat(tablePairs).isNotNull();
        assertThat(tablePairs.stream().map(TablePairDto::getName).sorted().collect(toList()))
                .isEqualTo(asList("departments", "users"));

        deleteWorkspace(workspaceId);
    }

    @Test
    public void getColumnPairsTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE_1).getBody();
        assertThat(workspaceId).isNotEmpty();

        final ParameterizedTypeReference<List<ColumnPairDto>> responseType = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<List<ColumnPairDto>> response = restTemplate.exchange(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/metadata/tablePairs/departments/columnPairs"),
                HttpMethod.GET,
                null,
                responseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final List<ColumnPairDto> tablePairs = response.getBody();
        assertThat(tablePairs).isNotNull();
        assertThat(tablePairs.stream().map(ColumnPairDto::getName).sorted().collect(toList()))
                .isEqualTo(asList("id", "name", "number_of_employees"));

        deleteWorkspace(workspaceId);
    }
}
