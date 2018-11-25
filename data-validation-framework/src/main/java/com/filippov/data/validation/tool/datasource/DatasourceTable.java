package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter // TODO: remove
@Builder
@EqualsAndHashCode(of = {"name"})
@JsonDeserialize(builder = DatasourceTable.DatasourceTableBuilder.class)
public class DatasourceTable {
    private String name;
    private DatasourceColumn primaryKey;
    private List<DatasourceColumn> columns;
}
