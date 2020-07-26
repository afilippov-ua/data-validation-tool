package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.storage.repository.WorkspaceRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public List<Workspace> get() {
        return workspaceRepository.findAll();
    }

    public Optional<Workspace> get(String id) {
        return workspaceRepository.findById(id);
    }

    public String create(Workspace workspace) {
        final Workspace ws = workspaceRepository.save(workspace);
        return ws.getId();
    }

    public void delete(String id) {
        workspaceRepository.deleteById(id);
    }
}