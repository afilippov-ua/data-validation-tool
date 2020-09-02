package com.filippov.data.validation.tool.controller;

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

    AbstractController(WorkspaceService workspaceService, MetadataService metadataService,
                       DataStoragePairRepository dataStoragePairRepository) {
        this.workspaceService = workspaceService;
        this.metadataService = metadataService;
        this.dataStoragePairRepository = dataStoragePairRepository;
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

    protected TablePair getTablePairByIdOrName(String workspace, String tablePair) {
        return getMetadata(getWorkspaceByIdOrName(workspace))
                .getTablePairByIdOrName(tablePair)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Table pair with id or name: '"
                                + tablePair + "' for workspace: " + workspace + " was not found"));
    }

    protected ColumnPair getColumnPairByIdOrName(String workspace, String tablePair, String columnPair) {
        return getMetadata(getWorkspaceByIdOrName(workspace))
                .getColumnPairByIdOrName(getTablePairByIdOrName(workspace, tablePair), columnPair)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Column pair with id or name '"
                                + columnPair + "' for table: " + tablePair + " for workspace: "
                                + workspace + " was not found"));
    }
}