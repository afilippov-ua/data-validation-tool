package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.TablePairDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Slf4j
@RestController
@RequestMapping("workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final MetadataService metadataService;
    private final DtoMapper dtoMapper;

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

        final WorkspaceDto result = workspaceService.getWorkspaceById(workspaceId)
                .map(dtoMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));

        log.debug("Returning workspace by workspaceId: {}. Execution time: {}", workspaceId, timer.stop());
        return result;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String createWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        log.debug("Calling 'createWorkspace' endpoint for workspace dto: {}", workspaceDto);
        final Timer timer = Timer.start();

        final String result = workspaceService.create(dtoMapper.fromDto(workspaceDto));

        log.debug("Workspace: {} has been successfully created. Execution time: {}", workspaceDto, timer.stop());
        return result;
    }

    @DeleteMapping(path = "/{workspaceId}")
    public void deleteWorkspace(@PathVariable("workspaceId") String workspaceId) {
        log.debug("Calling 'deleteWorkspace' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        workspaceService.delete(workspaceId);

        log.debug("Workspace with id: {} has been successfully deleted. Execution time: {}", workspaceId, timer.stop());
    }

    @GetMapping(path = "/{workspaceId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkspaceMetadataDto getWorkspaceMetadata(@PathVariable("workspaceId") String workspaceId) {
        log.debug("Calling 'getWorkspaceMetadata' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        final WorkspaceMetadataDto result = workspaceService.getWorkspaceById(workspaceId)
                .map(metadataService::getMetadata)
                .map(dtoMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));

        log.debug("Returning workspace metadata by workspace id: {}. Execution time: {}", workspaceId, timer.stop());
        return result;
    }

    @GetMapping(path = "/{id}/metadata/tablePairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TablePairDto> getTablePairs(@PathVariable("id") String workspaceId) {
        log.debug("Calling 'getTablePairs' endpoint for workspace id: {}", workspaceId);
        final Timer timer = Timer.start();

        final List<TablePairDto> result = workspaceService.getWorkspaceById(workspaceId)
                .map(workspace -> metadataService.getTablePairs(workspace).stream()
                        .map(dtoMapper::toDto)
                        .collect(toList()))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));

        log.debug("Returning table pairs by workspace id: {}. Execution time: {}", workspaceId, timer.stop());
        return result;
    }

    @GetMapping(path = "/{workspaceId}/metadata/tablePairs/{tablePairId}/columnPairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ColumnPairDto> getColumnPairs(@PathVariable("workspaceId") String workspaceId, @PathVariable("tablePairId") String tablePairId) {
        log.debug("Calling 'getColumnPairs' endpoint for workspace id: {} and table pair id: {}", workspaceId, tablePairId);
        final Timer timer = Timer.start();

        // TODO: check table pair name
        final List<ColumnPairDto> result = workspaceService.getWorkspaceById(workspaceId)
                .map(workspace -> metadataService.getColumnPairs(workspace, tablePairId).stream()
                        .map(dtoMapper::toDto)
                        .collect(toList()))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));

        log.debug("Returning column pairs for workspace id: {} and table pair id: {}. Execution time: {}", workspaceId, tablePairId, timer.stop());
        return result;
    }
}
