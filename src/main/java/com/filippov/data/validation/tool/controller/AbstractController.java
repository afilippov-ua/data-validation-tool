package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractController {

    protected final WorkspaceService workspaceService;
    protected final MetadataService metadataService;
    protected final DataStoragePairRepository dataStoragePairRepository;
    protected final DtoMapper dtoMapper;

    AbstractController(WorkspaceService workspaceService, MetadataService metadataService,
                       DataStoragePairRepository dataStoragePairRepository, DtoMapper dtoMapper) {
        this.workspaceService = workspaceService;
        this.metadataService = metadataService;
        this.dataStoragePairRepository = dataStoragePairRepository;
        this.dtoMapper = dtoMapper;
    }

    protected Workspace getWorkspaceByIdOrName(String workspace) {
        return workspaceService.getWorkspaceById(workspace)
                .orElseGet(() -> workspaceService.getWorkspaceByName(workspace)
                        .orElseThrow(() ->
                                new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace with id or name '" + workspace + "' was not found")));
    }

    protected Metadata getMetadata(Workspace workspace) {
        return metadataService.getMetadata(workspace);
    }

    protected TablePair getTablePairByIdOrName(Workspace workspace, String tablePair) {
        return getMetadata(workspace)
                .getTablePairByIdOrName(tablePair)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Table pair with id or name: '"
                                + tablePair + "' for workspace: " + workspace.getName() + " was not found"));
    }

    protected ColumnPair getColumnPairByIdOrName(Workspace workspace, TablePair tablePair, String columnPair) {
        return getMetadata(workspace)
                .getColumnPairByIdOrName(tablePair, columnPair)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Column pair with id or name '"
                                + columnPair + "' for table: " + tablePair.getName() + " for workspace: "
                                + workspace.getName() + " was not found"));
    }
}