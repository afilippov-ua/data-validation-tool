package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.service.DatasourceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("datasources")
@RequiredArgsConstructor
public class DatasourceController {
    private final DatasourceService datasourceService;

    @GetMapping(path = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DatasourceType> getAllDatasourceTypes() {
        log.debug("Calling 'getAllDatasourceTypes' endpoint");
        final Timer timer = Timer.start();

        final List<DatasourceType> result = datasourceService.getDatasourceTypes();

        log.debug("Returning datasource types list. Execution time: {}", timer.stop());
        return result;
    }
}
