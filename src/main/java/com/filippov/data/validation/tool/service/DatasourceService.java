package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DatasourceService {

    public List<DatasourceType> getDatasourceTypes() {
        return Arrays.stream(DatasourceType.values())
                .sorted()
                .collect(Collectors.toList());
    }
}
