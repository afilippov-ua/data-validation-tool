package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.TablePairDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;
    private final MetadataService metadataService;
    private final DtoMapper dtoMapper;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<WorkspaceDto> getWorkspaces() {
        return workspaceService.getAllWorkspaces().stream()
                .map(dtoMapper::toDto)
                .collect(toList());
    }

    @GetMapping(path = "/{workspaceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkspaceDto getWorkspace(@PathVariable("workspaceId") String workspaceId) {
        return workspaceService.getWorkspaceById(workspaceId)
                .map(dtoMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String createWorkspace(@RequestBody WorkspaceDto workspaceDto) {
        return workspaceService.create(dtoMapper.fromDto(workspaceDto));
    }

    @DeleteMapping(path = "/{workspaceId}")
    public void deleteWorkspace(@PathVariable("workspaceId") String workspaceId) {
        workspaceService.delete(workspaceId);
    }

    @GetMapping(path = "/{workspaceId}/metadata", produces = MediaType.APPLICATION_JSON_VALUE)
    public WorkspaceMetadataDto getWorkspaceMetadata(@PathVariable("workspaceId") String workspaceId) {
        return workspaceService.getWorkspaceById(workspaceId)
                .map(metadataService::getMetadata)
                .map(dtoMapper::toDto)
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));
    }

    @GetMapping(path = "/{id}/metadata/tablePairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<TablePairDto> getTablePairs(@PathVariable("id") String id) {
        return workspaceService.getWorkspaceById(id)
                .map(workspace -> metadataService.getTablePairs(workspace).stream()
                        .map(dtoMapper::toDto)
                        .collect(toList()))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + id));
    }

    @GetMapping(path = "/{workspaceId}/metadata/tablePairs/{tablePairId}/columnPairs", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ColumnPairDto> getColumnPairs(@PathVariable("workspaceId") String workspaceId, @PathVariable("tablePairId") String tablePairId) {
        // TODO: check table pair name
        return workspaceService.getWorkspaceById(workspaceId)
                .map(workspace -> metadataService.getColumnPairs(workspace, tablePairId).stream()
                        .map(dtoMapper::toDto)
                        .collect(toList()))
                .orElseThrow(() -> new ResponseStatusException(NOT_FOUND, "Unable to find workspace by id: " + workspaceId));
    }
}
