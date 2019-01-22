package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datasource.DatasourceType;

import java.util.List;

public interface DatasourceService {

    List<DatasourceType> getDatasourceTypes();
}
