package com.filippov.data.validation.tool.dto.workspace;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class WorkspaceDto {
    private final String id;
    private final String name;
    private final DatasourceDefinitionDto left;
    private final DatasourceDefinitionDto right;

    @JsonCreator
    public WorkspaceDto(String id, String name, DatasourceDefinitionDto left, DatasourceDefinitionDto right) {
        this.id = id;
        this.name = name;
        this.left = left;
        this.right = right;
    }
}
