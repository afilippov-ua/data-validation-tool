package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.repository.data.WorkspaceRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public WorkspaceService(WorkspaceRepository workspaceRepository) {
        this.workspaceRepository = workspaceRepository;
    }

    public List<Workspace> getAllWorkspaces() {
        return workspaceRepository.findAll();
    }

    public Optional<Workspace> getWorkspaceById(String id) {
        return workspaceRepository.findById(id);
    }

    public Optional<Workspace> getWorkspaceByName(String name) {
        return workspaceRepository.findByName(name);
    }

    public String create(Workspace workspace) {
        final Workspace ws = workspaceRepository.save(workspace);
        return ws.getId();
    }

    public void delete(String id) {
        workspaceRepository.deleteById(id);
    }
}