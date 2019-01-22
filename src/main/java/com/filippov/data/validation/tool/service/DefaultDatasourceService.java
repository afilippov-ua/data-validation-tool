package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datasource.DatasourceType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DefaultDatasourceService implements DatasourceService {

    @Override
    public List<DatasourceType> getDatasourceTypes() {
        return Arrays.stream(DatasourceType.values())
                .sorted()
                .collect(Collectors.toList());
    }
}
