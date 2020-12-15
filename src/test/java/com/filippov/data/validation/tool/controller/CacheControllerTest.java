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

import com.filippov.data.validation.tool.dto.cache.CacheRequestDto;
import com.filippov.data.validation.tool.dto.cache.CacheInfoDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import com.filippov.data.validation.tool.model.cache.CachingStatus;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public class CacheControllerTest extends AbstractDataValidationApplicationTest {

    @Test
    public void getTablePairCacheDetailsTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final ParameterizedTypeReference<List<ColumnPairCacheDetailsDto>> responseType = new ParameterizedTypeReference<>() {
        };
        final ResponseEntity<List<ColumnPairCacheDetailsDto>> response = restTemplate.exchange(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + DEPARTMENTS + "/cacheDetails"),
                HttpMethod.GET,
                null,
                responseType);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final List<ColumnPairCacheDetailsDto> cacheDetailsList = response.getBody();
        assertThat(cacheDetailsList)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3);

        final CacheInfoDto emptyCacheDetails = CacheInfoDto.builder()
                .cached(false)
                .date(null)
                .build();

        final Optional<ColumnPairCacheDetailsDto> idCacheDetailsOptional = cacheDetailsList.stream()
                .filter(cacheDetails -> cacheDetails.getColumnPair().getName().equals(DEPARTMENTS_ID))
                .findFirst();
        assertThat(idCacheDetailsOptional).isNotEmpty();
        final ColumnPairCacheDetailsDto idCacheDetails = idCacheDetailsOptional.get();
        assertThat(idCacheDetails.getLeftCacheInfo()).isEqualTo(emptyCacheDetails);
        assertThat(idCacheDetails.getRightCacheInfo()).isEqualTo(emptyCacheDetails);

        final Optional<ColumnPairCacheDetailsDto> nameCacheDetailsOptional = cacheDetailsList.stream()
                .filter(cacheDetails -> cacheDetails.getColumnPair().getName().equals(DEPARTMENTS_NAME))
                .findFirst();
        assertThat(nameCacheDetailsOptional).isNotEmpty();
        final ColumnPairCacheDetailsDto nameCacheDetails = nameCacheDetailsOptional.get();
        assertThat(nameCacheDetails.getLeftCacheInfo()).isEqualTo(emptyCacheDetails);
        assertThat(nameCacheDetails.getRightCacheInfo()).isEqualTo(emptyCacheDetails);

        final Optional<ColumnPairCacheDetailsDto> numberOfEmployeesCacheDetailsOptional = cacheDetailsList.stream()
                .filter(cacheDetails -> cacheDetails.getColumnPair().getName().equals(DEPARTMENTS_NUMBER_OF_EMPLOYEES))
                .findFirst();
        assertThat(numberOfEmployeesCacheDetailsOptional).isNotEmpty();
        final ColumnPairCacheDetailsDto numberOfEmployeesCacheDetails = numberOfEmployeesCacheDetailsOptional.get();
        assertThat(numberOfEmployeesCacheDetails.getLeftCacheInfo()).isEqualTo(emptyCacheDetails);
        assertThat(numberOfEmployeesCacheDetails.getRightCacheInfo()).isEqualTo(emptyCacheDetails);

        deleteWorkspace(workspaceId);
    }

    @Test
    public void getColumnPairCacheDetailsTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final ResponseEntity<ColumnPairCacheDetailsDto> response = restTemplate.getForEntity(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + DEPARTMENTS + "/columnPairs/" + DEPARTMENTS_NAME + "/cacheDetails"),
                ColumnPairCacheDetailsDto.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final ColumnPairCacheDetailsDto cacheDetails = response.getBody();
        assertThat(cacheDetails).isNotNull();

        final CacheInfoDto emptyCacheDetails = CacheInfoDto.builder()
                .cached(false)
                .date(null)
                .build();

        assertThat(cacheDetails.getColumnPair().getName()).isEqualTo(DEPARTMENTS_NAME);
        assertThat(cacheDetails.getLeftCacheInfo()).isEqualTo(emptyCacheDetails);
        assertThat(cacheDetails.getRightCacheInfo()).isEqualTo(emptyCacheDetails);

        deleteWorkspace(workspaceId);
    }

    @Test
    @SneakyThrows
    public void processTableCachingCommandTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final CacheRequestDto cacheRequestDto = CacheRequestDto.builder()
                .cacheFetchingCommand(CacheFetchingCommand.START)
                .build();

        final ResponseEntity<CachingStatus> response = restTemplate.postForEntity(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + DEPARTMENTS + "/cache"),
                new HttpEntity<>(cacheRequestDto),
                CachingStatus.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final CachingStatus cachingStatus = response.getBody();
        assertThat(cachingStatus).isEqualTo(CachingStatus.STARTED);

        // query 5 times until finished
        int n = 5;
        while (n > 0) {
            if (isTableCached(workspaceId, DEPARTMENTS)) {
                break;
            } else {
                Thread.sleep(100);
            }
            n--;
        }

        final ResponseEntity<Map<String, CachingStatus>> statusesResponse = getTableCacheStatuses(workspaceId, DEPARTMENTS);
        assertThat(statusesResponse).isNotNull();
        assertThat(statusesResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statusesResponse.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(statusesResponse.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final Map<String, CachingStatus> columnStatuses = statusesResponse.getBody();
        assertThat(columnStatuses).isNotNull();
        assertThat(columnStatuses.values().stream().allMatch(status -> status == CachingStatus.FINISHED)).isTrue();

        deleteWorkspace(workspaceId);
    }

    @Test
    @SneakyThrows
    public void processColumnCachingCommandTest() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final CacheRequestDto cacheRequestDto = CacheRequestDto.builder()
                .cacheFetchingCommand(CacheFetchingCommand.START)
                .build();

        final ResponseEntity<CachingStatus> response = restTemplate.postForEntity(
                buildUrl(WORKSPACES_PATH + "/" + workspaceId + "/tablePairs/" + DEPARTMENTS + "/columnPairs/" + DEPARTMENTS_NAME + "/cache"),
                new HttpEntity<>(cacheRequestDto),
                CachingStatus.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final CachingStatus cachingStatus = response.getBody();
        assertThat(cachingStatus).isEqualTo(CachingStatus.STARTED);

        // query 5 times until finished
        int n = 5;
        while (n > 0) {
            if (isColumnCached(workspaceId, DEPARTMENTS, DEPARTMENTS_NAME)) {
                break;
            } else {
                Thread.sleep(100);
            }
            n--;
        }

        final ResponseEntity<CachingStatus> statusResponse = getColumnCacheStatus(workspaceId, DEPARTMENTS, DEPARTMENTS_NAME);
        assertThat(statusResponse).isNotNull();
        assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statusResponse.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(statusResponse.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final CachingStatus columnStatus = statusResponse.getBody();
        assertThat(columnStatus).isNotNull();
        assertThat(columnStatus).isEqualTo(CachingStatus.FINISHED);

        deleteWorkspace(workspaceId);
    }

    @Test
    @SneakyThrows
    public void getTablePairCacheStatus() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final ResponseEntity<Map<String, CachingStatus>> response = getTableCacheStatuses(workspaceId, DEPARTMENTS);
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(response.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final Map<String, CachingStatus> statuses = response.getBody();
        assertThat(statuses).isNotNull();
        assertThat(statuses.get(DEPARTMENTS_ID)).isEqualTo(CachingStatus.NON_DEFINED);
        assertThat(statuses.get(DEPARTMENTS_NAME)).isEqualTo(CachingStatus.NON_DEFINED);
        assertThat(statuses.get(DEPARTMENTS_NUMBER_OF_EMPLOYEES)).isEqualTo(CachingStatus.NON_DEFINED);

        final ResponseEntity<CachingStatus> cachingResponse = startCachingTable(workspaceId, DEPARTMENTS);
        assertThat(cachingResponse).isNotNull();
        assertThat(cachingResponse.getBody()).isNotNull().isEqualTo(CachingStatus.STARTED);

        // query 5 times until finished
        int n = 5;
        while (n > 0) {
            if (isTableCached(workspaceId, DEPARTMENTS)) {
                break;
            } else {
                Thread.sleep(100);
            }
            n--;
        }

        final ResponseEntity<Map<String, CachingStatus>> statusesAfterCachingResponse = getTableCacheStatuses(workspaceId, DEPARTMENTS);
        assertThat(statusesAfterCachingResponse).isNotNull();
        assertThat(statusesAfterCachingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statusesAfterCachingResponse.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(statusesAfterCachingResponse.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final Map<String, CachingStatus> statusesAfterCaching = statusesAfterCachingResponse.getBody();
        assertThat(statusesAfterCaching).isNotNull();
        assertThat(statusesAfterCaching.get(DEPARTMENTS_ID)).isEqualTo(CachingStatus.FINISHED);
        assertThat(statusesAfterCaching.get(DEPARTMENTS_NAME)).isEqualTo(CachingStatus.FINISHED);
        assertThat(statusesAfterCaching.get(DEPARTMENTS_NUMBER_OF_EMPLOYEES)).isEqualTo(CachingStatus.FINISHED);

        deleteWorkspace(workspaceId);
    }

    @Test
    @SneakyThrows
    public void getColumnPairCacheStatus() {
        final String workspaceId = createWorkspace(TEST_WORKSPACE).getBody();

        final ResponseEntity<CachingStatus> statusResponse = getColumnCacheStatus(workspaceId, DEPARTMENTS, DEPARTMENTS_NAME);
        assertThat(statusResponse).isNotNull();
        assertThat(statusResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statusResponse.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(statusResponse.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final CachingStatus columnStatus = statusResponse.getBody();
        assertThat(columnStatus).isNotNull();
        assertThat(columnStatus).isEqualTo(CachingStatus.NON_DEFINED);

        final ResponseEntity<CachingStatus> cachingResponse = startCachingColumn(workspaceId, DEPARTMENTS, DEPARTMENTS_NAME);
        assertThat(cachingResponse).isNotNull();
        assertThat(cachingResponse.getBody()).isNotNull().isEqualTo(CachingStatus.STARTED);

        // query 5 times until finished
        int n = 5;
        while (n > 0) {
            if (isColumnCached(workspaceId, DEPARTMENTS, DEPARTMENTS_NAME)) {
                break;
            } else {
                Thread.sleep(100);
            }
            n--;
        }

        final ResponseEntity<CachingStatus> statusAfterCachingResponse = getColumnCacheStatus(workspaceId, DEPARTMENTS, DEPARTMENTS_NAME);
        assertThat(statusAfterCachingResponse).isNotNull();
        assertThat(statusAfterCachingResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(statusAfterCachingResponse.getHeaders().containsKey(CONTENT_TYPE)).isTrue();
        assertThat(statusAfterCachingResponse.getHeaders().get(CONTENT_TYPE)).isEqualTo(singletonList(APPLICATION_JSON_VALUE));

        final CachingStatus statusAfterCaching = statusAfterCachingResponse.getBody();
        assertThat(statusAfterCaching).isNotNull();
        assertThat(statusAfterCaching).isEqualTo(CachingStatus.FINISHED);

        deleteWorkspace(workspaceId);
    }
}
