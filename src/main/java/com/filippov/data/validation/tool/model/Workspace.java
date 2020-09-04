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
@ToString(of = {"id", "name"})
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
public class Workspace {
    @Id
    private final String id;
    private final String name;
    private final DatasourceConfig leftDatasourceConfig;
    private final DatasourceConfig rightDatasourceConfig;
}
