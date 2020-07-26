package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.service.DataStoragePairService;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class AbstractController {

    private final WorkspaceService workspaceService;
    private final MetadataService metadataService;
    private final DataStoragePairService dataStoragePairService;

    AbstractController(WorkspaceService workspaceService, MetadataService metadataService,
                       DataStoragePairService dataStoragePairService) {
        this.workspaceService = workspaceService;
        this.metadataService = metadataService;
        this.dataStoragePairService = dataStoragePairService;
    }

    protected Workspace getWorkspace(String workspaceId) {
        return workspaceService.get(workspaceId).orElseThrow(() ->
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

    protected ColumnPair getColumnPair(String workspaceId, String tablePairName, String columnPairName) {
        var workspace = getWorkspace(workspaceId);
        var metadata = getMetadata(workspace);
        var tablePair = getTablePair(workspaceId, tablePairName);

        return metadata.getColumnPair(tablePair, columnPairName)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Column pair with name '"
                                + columnPairName + "' for table: " + tablePairName + " for workspace: "
                                + workspaceId + " was not found"));
    }
}
