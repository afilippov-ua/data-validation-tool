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
import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.TablePairDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
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

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("workspaces")
public class WorkspaceController extends AbstractController {

    public WorkspaceController(WorkspaceService workspaceService, MetadataService metadataService,
                               DataStoragePairRepository dataStoragePairRepository, DtoMapper dtoMapper) {
        super(workspaceService, metadataService, dataStoragePairRepository, dtoMapper);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkspaceDto> getWorkspaces() {
        log.debug("Calling 'getWorkspaces' endpoint");
        final Timer timer = Timer.start();

        final List<WorkspaceDto> result = workspaceService.getAllWorkspaces().stream()
                .map(dtoMapper::toDto)
                .collect(toList());

        log.debug("Returning list of all workspaces. Size: {}. Execution time: {}", result.size(), timer.stop());
        return result;
    }

    @GetMapping(path = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkspaceDto getWorkspace(@PathVariable("workspaceId") String workspaceId) {
        log.debug("Calling 'getWorkspace' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .validate();

        final WorkspaceDto result = dtoMapper.toDto(getWorkspaceByIdOrName(workspaceId));

        log.debug("Returning workspace by workspaceId: {}. Execution time: {}", workspaceId, timer.stop());
        return result;
    }

    @PostMapping(produces = MediaType.TEXT_PLAIN_VALUE)
    public String createWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        log.debug("Calling 'createWorkspace' endpoint for workspace dto: {}", workspaceDto);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceDto(workspaceDto)
                .validate();

        final String result = workspaceService.create(dtoMapper.fromDto(workspaceDto));

        log.debug("Workspace: {} has been successfully created. Execution time: {}", workspaceDto, timer.stop());
        return result;
    }

    @DeleteMapping(path = "/{workspaceId}")
    public void deleteWorkspace(@PathVariable("workspaceId") String workspaceId) {
        log.debug("Calling 'deleteWorkspace' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        metadataService.deleteMetadata(workspace);
        workspaceService.delete(workspace.getId());

        log.debug("Workspace with id: {} has been successfully deleted. Execution time: {}", workspaceId, timer.stop());
    }

    @GetMapping(path = "/{workspaceId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkspaceMetadataDto getWorkspaceMetadata(@PathVariable("workspaceId") String workspaceId) {
        log.debug("Calling 'getWorkspaceMetadata' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final WorkspaceMetadataDto result = dtoMapper.toDto(metadataService.getMetadata(workspace));

        log.debug("Returning workspace metadata by workspace id: {}. Execution time: {}", workspaceId, timer.stop());
        return result;
    }

    @GetMapping(path = "/{id}/metadata/tablePairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TablePairDto> getTablePairs(@PathVariable("id") String workspaceId) {
        log.debug("Calling 'getTablePairs' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final List<TablePairDto> result = metadataService.getTablePairs(workspace).stream()
                .map(dtoMapper::toDto)
                .collect(toList());

        log.debug("Returning table pairs by workspace id: {}. Execution time: {}", workspaceId, timer.stop());
        return result;
    }

    @GetMapping(path = "/{workspaceId}/metadata/tablePairs/{tablePairId}/columnPairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ColumnPairDto> getColumnPairs(@PathVariable("workspaceId") String workspaceId, @PathVariable("tablePairId") String tablePairId) {
        log.debug("Calling 'getColumnPairs' endpoint for workspace id: {} and table pair id: {}", workspaceId, tablePairId);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final List<ColumnPairDto> result = metadataService.getColumnPairs(workspace, tablePair).stream()
                .map(dtoMapper::toDto)
                .collect(toList());

        log.debug("Returning column pairs for workspace id: {} and table pair id: {}. Execution time: {}", workspaceId, tablePairId, timer.stop());
        return result;
    }
}
