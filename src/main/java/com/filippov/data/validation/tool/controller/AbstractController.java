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

    protected Workspace getWorkspace(String workspaceId) {
        return workspaceService.getWorkspaceById(workspaceId).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Workspace with id '" + workspaceId + "' was not found"));
    }

    protected Metadata getMetadata(Workspace workspace) {
        return metadataService.getMetadata(workspace);
    }

    protected TablePair getTablePair(String workspaceId, String tablePairId) {
        var workspace = getWorkspace(workspaceId);
        var metadata = getMetadata(workspace);
        return metadata.getTablePairById(tablePairId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Table pair with id '"
                                + tablePairId + "' for workspace: " + workspaceId + " was not found"));
    }

    protected ColumnPair getColumnPair(String workspaceId, String tablePairId, String columnPairId) {
        var workspace = getWorkspace(workspaceId);
        var metadata = getMetadata(workspace);
        var tablePair = getTablePair(workspaceId, tablePairId);

        return metadata.getColumnPairById(tablePair, columnPairId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Column pair with id '"
                                + columnPairId + "' for table: " + tablePairId + " for workspace: "
                                + workspaceId + " was not found"));
    }
}
