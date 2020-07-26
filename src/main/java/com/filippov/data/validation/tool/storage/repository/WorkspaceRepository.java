package com.filippov.data.validation.tool.storage.repository;

import com.filippov.data.validation.tool.model.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {

}
