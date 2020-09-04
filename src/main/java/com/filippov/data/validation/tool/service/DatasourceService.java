package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class DatasourceService {

    public List<DatasourceType> getDatasourceTypes() {
        log.debug("Getting all datasource types");
        return Arrays.stream(DatasourceType.values())
                .sorted()
                .collect(Collectors.toList());
    }
}
