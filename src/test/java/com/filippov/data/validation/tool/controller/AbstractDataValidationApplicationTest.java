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

import com.filippov.data.validation.tool.TestDataValidationToolApplication;
import com.filippov.data.validation.tool.dto.cache.CacheRequestDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import com.filippov.data.validation.tool.model.CachingStatus;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.util.Map;

import static com.filippov.data.validation.tool.datasource.model.DatasourceType.TEST_IN_MEMORY_DATASOURCE;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TestDataValidationToolApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractDataValidationApplicationTest {

    protected static final String WORKSPACES_PATH = "/workspaces";
    protected static final String DATASOURCES_PATH = "/datasources";
    protected static final String VALIDATION_RESULTS_PATH = "/validationResults";

    protected static final String DEPARTMENTS = "departments";
    protected static final String USERS = "users";

    protected static final String DEPARTMENTS_ID = "id";
    protected static final String DEPARTMENTS_NAME = "name";
    protected static final String DEPARTMENTS_NUMBER_OF_EMPLOYEES = "number_of_employees";

    protected static final String USERS_ID = "id";
    protected static final String USERS_USERNAME = "username";
    protected static final String USERS_PASSWORD = "password";

    protected static final WorkspaceDto TEST_WORKSPACE = WorkspaceDto.builder()
            .name("test-workspace-name")
            .left(DatasourceDefinitionDto.builder()
                    .datasourceType(TEST_IN_MEMORY_DATASOURCE)
                    .maxConnections(1)
                    .configParams(Map.of("relation", "LEFT"))
                    .build())
            .right(DatasourceDefinitionDto.builder()
                    .datasourceType(TEST_IN_MEMORY_DATASOURCE)
                    .maxConnections(2)
                    .configParams(Map.of("relation", "RIGHT"))
                    .build())
            .build();

    @Resource
    protected TestRestTemplate restTemplate = new TestRestTemplate();

    @LocalServerPort
    protected int port;

    protected String buildUrl(String uri) {
        return "http://localhost:" + port + uri;
    }

    protected ResponseEntity<WorkspaceDto> getWorkspace(String workspaceId) {
        return restTemplate.getForEntity(buildUrl(WORKSPACES_PATH + "/" + workspaceId), WorkspaceDto.class);
    }

    protected ResponseEntity<String> createWorkspace(WorkspaceDto workspaceDto) {
        return restTemplate.postForEntity(buildUrl(WORKSPACES_PATH), new HttpEntity<>(workspaceDto), String.class);
    }

    protected ResponseEntity<String> deleteWorkspace(String workspaceId) {
        return restTemplate.exchange(buildUrl(WORKSPACES_PATH + "/" + workspaceId), HttpMethod.DELETE, null, String.class);
    }

    protected boolean isTableCached(String workspaceId, String tablePaidId) {
        final Map<String, CachingStatus> columnStatuses = getTableCacheStatuses(workspaceId, tablePaidId).getBody();
        assertThat(columnStatuses).isNotNull();
        return columnStatuses.values().stream()
                .allMatch(status -> status == CachingStatus.FINISHED);
    }

    protected boolean isColumnCached(String workspaceId, String tablePaidId, String columnPairId) {
        final CachingStatus columnStatus = getColumnCacheStatus(workspaceId, tablePaidId, columnPairId).getBody();
        return columnStatus == CachingStatus.FINISHED;
    }

    protected ResponseEntity<CachingStatus> startCachingTable(String workspaceId, String tablePairId) {
        return processCachingTableCommand(workspaceId, tablePairId, CacheFetchingCommand.START);
    }

    protected ResponseEntity<CachingStatus> stopCachingTable(String workspaceId, String tablePairId) {
        return processCachingTableCommand(workspaceId, tablePairId, CacheFetchingCommand.STOP);
    }

    protected ResponseEntity<CachingStatus> processCachingTableCommand(String workspaceId, String tablePairId, CacheFetchingCommand start) {
        return restTemplate.postForEntity(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + tablePairId + "/cache"),
                new HttpEntity<>(CacheRequestDto.builder()
                        .cacheFetchingCommand(CacheFetchingCommand.START)
                        .build()),
                CachingStatus.class);
    }

    protected ResponseEntity<CachingStatus> startCachingColumn(String workspaceId, String tablePairId, String columnPairId) {
        return processCachingColumnCommand(workspaceId, tablePairId, columnPairId, CacheFetchingCommand.START);
    }

    protected ResponseEntity<CachingStatus> stopCachingColumn(String workspaceId, String tablePairId, String columnPairId) {
        return processCachingColumnCommand(workspaceId, tablePairId, columnPairId, CacheFetchingCommand.STOP);
    }

    protected ResponseEntity<CachingStatus> processCachingColumnCommand(
            String workspaceId, String tablePairId, String columnPairId, CacheFetchingCommand cacheFetchingCommand) {
        return restTemplate.postForEntity(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + tablePairId + "/columnPairs/" + columnPairId + "/cache"),
                new HttpEntity<>(CacheRequestDto.builder()
                        .cacheFetchingCommand(CacheFetchingCommand.START)
                        .build()),
                CachingStatus.class);
    }

    protected ResponseEntity<Map<String, CachingStatus>> getTableCacheStatuses(String workspaceId, String tablePaidId) {
        final ParameterizedTypeReference<Map<String, CachingStatus>> responseType = new ParameterizedTypeReference<>() {
        };
        return restTemplate.exchange(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + tablePaidId + "/cache/status"),
                HttpMethod.GET,
                null,
                responseType);
    }

    protected ResponseEntity<CachingStatus> getColumnCacheStatus(String workspaceId, String tablePaidId, String columnPairId) {
        return restTemplate.getForEntity(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + tablePaidId + "/columnPairs/" + columnPairId + "/cache/status"),
                CachingStatus.class);
    }
}
