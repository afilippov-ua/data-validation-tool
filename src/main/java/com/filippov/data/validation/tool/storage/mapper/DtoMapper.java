package com.filippov.data.validation.tool.storage.mapper;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.storage.dto.DatasourceDto;

public class DtoMapper {

    public DatasourceDto toDto(Datasource datasource) {
        return DatasourceDto.builder()
                .datasourceClass(datasource.getClass().getSimpleName())
                .connectionString(datasource.getConnectionString())
                .build();
    }

    public Datasource fromDto(DatasourceDto datasourceDto) {
        // TODO : change to enum
        if (datasourceDto.getDatasourceClass().equals(EmptyDatasource.class.getSimpleName())) {
            return new EmptyDatasource(datasourceDto.getConnectionString());
        } else {
            throw new IllegalArgumentException("Unsupported datasource class: " + datasourceDto.getDatasourceClass());
        }
    }
}
