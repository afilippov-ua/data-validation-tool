package com.filippov.data.validation.tool.repository.data;

import com.filippov.data.validation.tool.model.Workspace;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WorkspaceRepository extends MongoRepository<Workspace, String> {

}
