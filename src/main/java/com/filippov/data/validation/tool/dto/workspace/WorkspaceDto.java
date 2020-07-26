package com.filippov.data.validation.tool.dto.workspace;

import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class WorkspaceDto {
    private String id;
    private String name;
    private DatasourceDefinitionDto left;
    private DatasourceDefinitionDto right;
}
