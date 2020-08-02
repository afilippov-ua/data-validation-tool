package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datasource.config.DatasourceConfig;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Workspace {
    @Id
    private String id;
    private String name;
    private DatasourceConfig leftDatasourceConfig;
    private DatasourceConfig rightDatasourceConfig;
}