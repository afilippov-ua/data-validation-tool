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