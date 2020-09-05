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

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.controller.validation.InputValidator;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.cache.CacheRequestDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.model.CachingStatus;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.CacheService;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@Slf4j
@RestController
@RequestMapping("workspaces")
public class CacheController extends AbstractController {

    private final CacheService cacheService;

    public CacheController(WorkspaceService workspaceService, MetadataService metadataService, DataStoragePairRepository dataStoragePairRepository,
                           CacheService cacheService, DtoMapper dtoMapper) {
        super(workspaceService, metadataService, dataStoragePairRepository, dtoMapper);
        this.cacheService = cacheService;
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cacheDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ColumnPairCacheDetailsDto> getTablePairCacheDetails(@PathVariable("workspaceId") String workspaceId,
                                                                    @PathVariable("tablePairId") String tablePairId) {
        log.debug("Calling 'getTablePairCacheDetails' endpoint for workspace id: {} and table pair id: {}", workspaceId, tablePairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final List<ColumnPairCacheDetailsDto> result =
                cacheService.getTablePairCacheDetails(workspace, tablePair).stream()
                        .map(dtoMapper::toDto)
                        .collect(toList());

        log.debug("Returning table pair cache details for workspace id: {} and table pair id: {}. Execution time: {}", workspaceId, tablePairId, timer.stop());
        return result;
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cacheDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ColumnPairCacheDetailsDto getColumnPairCacheDetails(@PathVariable("workspaceId") String workspaceId,
                                                               @PathVariable("tablePairId") String tablePairId,
                                                               @PathVariable("columnPairId") String columnPairId) {
        log.debug("Calling 'getColumnPairCacheDetails' endpoint for workspace id: {}, table pair id: {} and column pair id: {}",
                workspaceId, tablePairId, columnPairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .withColumnPairId(columnPairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final ColumnPair columnPair = getColumnPairByIdOrName(workspace, tablePair, columnPairId);
        final ColumnPairCacheDetailsDto result = dtoMapper.toDto(cacheService.getColumnPairCacheDetails(workspace, tablePair, columnPair));

        log.debug("Returning column pair cache details for workspace id: {}, table pair id: {} and column pair id: {}. Execution time: {}",
                workspaceId, tablePairId, columnPairId, timer.stop());
        return result;
    }

    @PostMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache")
    public CachingStatus processCachingCommand(@PathVariable("workspaceId") String workspaceId,
                                               @PathVariable("tablePairId") String tablePairId,
                                               @RequestBody CacheRequestDto cacheRequestDto) {

        log.debug("Calling 'processCachingCommand' endpoint for workspaceId: {}, tablePairId: {} with cacheRequestDto: {}",
                workspaceId, tablePairId, cacheRequestDto);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .withCacheRequestDto(cacheRequestDto)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final List<CachingStatus> result = metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair)
                .stream()
                .map(columnPair ->
                        cacheService.processCachingCommand(
                                workspace,
                                tablePair,
                                columnPair,
                                cacheRequestDto.getCacheFetchingCommand()))
                .collect(toList());

        log.debug("Caching command has been processed for workspaceId: {}, tablePairId: {} with cacheRequestDto: {}. Execution time: {}",
                workspaceId, tablePairId, cacheRequestDto, timer.stop());
        return result.size() > 0 ? result.get(0) : CachingStatus.NON_DEFINED;
    }

    @PostMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache")
    public CachingStatus processCachingCommand(@PathVariable("workspaceId") String workspaceId,
                                               @PathVariable("tablePairId") String tablePairId,
                                               @PathVariable("columnPairId") String columnPairId,
                                               @RequestBody CacheRequestDto cacheRequestDto) {
        log.debug("Calling 'processCachingCommand' endpoint for workspaceId: {}, tablePairId: {}, columnPairId: {} with cacheRequestDto: {}",
                workspaceId, tablePairId, columnPairId, cacheRequestDto);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .withColumnPairId(columnPairId)
                .withCacheRequestDto(cacheRequestDto)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final ColumnPair columnPair = getColumnPairByIdOrName(workspace, tablePair, columnPairId);
        final CachingStatus result = cacheService.processCachingCommand(workspace, tablePair, columnPair, cacheRequestDto.getCacheFetchingCommand());

        log.debug("Caching command has been processed for workspaceId: {}, tablePairId: {}, columnPairId: {} with cacheRequestDto: {}. Execution time: {}",
                workspaceId, tablePairId, columnPairId, cacheRequestDto, timer.stop());
        return result;
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, CachingStatus> getTablePairCacheStatus(@PathVariable("workspaceId") String workspaceId,
                                                              @PathVariable("tablePairId") String tablePairId) {
        log.debug("Calling 'getTablePairCacheStatus' endpoint for workspaceId: {} and tablePairId: {}", workspaceId, tablePairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final Map<String, CachingStatus> result = cacheService.getTablePairCacheStatus(workspace, tablePair)
                .entrySet()
                .stream()
                .collect(toMap(
                        entry -> entry.getKey().getName(),
                        Map.Entry::getValue));

        log.debug("Returning table pair cache status for workspaceId: {} and tablePairId: {}. Execution time: {}",
                workspaceId, tablePairId, timer.stop());
        return result;
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CachingStatus getColumnPairCacheStatus(@PathVariable("workspaceId") String workspaceId,
                                                  @PathVariable("tablePairId") String tablePairId,
                                                  @PathVariable("columnPairId") String columnPairId) {
        log.debug("Calling 'getColumnPairCacheStatus' endpoint for workspaceId: {}, tablePairId: {} and columnPairId: {}",
                workspaceId, tablePairId, columnPairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .withColumnPairId(columnPairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final ColumnPair columnPair = getColumnPairByIdOrName(workspace, tablePair, columnPairId);
        final CachingStatus result = cacheService.getColumnPairCacheStatus(workspace, tablePair, columnPair);

        log.debug("Returning column pair cache status for workspaceId: {}, tablePairId: {} and columnPairId: {}. Execution time: {}",
                workspaceId, tablePairId, columnPairId, timer.stop());
        return result;
    }

    @DeleteMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache")
    public void deleteTableCache(@PathVariable("workspaceId") String workspaceId,
                                 @PathVariable("tablePairId") String tablePairId) {
        log.debug("Calling 'deleteTableCache' endpoint for workspaceId: {} and tablePairId: {}", workspaceId, tablePairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair)
                .forEach(columnPair -> cacheService.deleteCacheForColumnPair(workspace, tablePair, columnPair));

        log.debug("Cache has been deleted for workspaceId: {} and tablePairId: {}. Execution time: {}", workspaceId, tablePairId, timer.stop());
    }

    @DeleteMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache")
    public void deleteColumnCache(@PathVariable("workspaceId") String workspaceId,
                                  @PathVariable("tablePairId") String tablePairId,
                                  @PathVariable("columnPairId") String columnPairId) {
        log.debug("Calling 'deleteColumnCache' endpoint for workspaceId: {}, tablePairId: {} and columnPairId: {}",
                workspaceId, tablePairId, columnPairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .withColumnPairId(columnPairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final ColumnPair columnPair = getColumnPairByIdOrName(workspace, tablePair, columnPairId);
        cacheService.deleteCacheForColumnPair(workspace, tablePair, columnPair);

        log.debug("Cache has been deleted for workspaceId: {}, tablePairId: {} and columnPairId: {}. Execution time: {}",
                workspaceId, tablePairId, columnPairId, timer.stop());
    }
}
