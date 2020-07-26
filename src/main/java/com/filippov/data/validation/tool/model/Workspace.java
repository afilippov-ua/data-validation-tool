package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.datasource.model.DatasourceConfig;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@Builder(toBuilder = true)
@EqualsAndHashCode(of = "id")
@ToString
public class Workspace {
    @Id
    private String id;
    private String name;
    private DatasourceConfig left;
    private DatasourceConfig right;
}
