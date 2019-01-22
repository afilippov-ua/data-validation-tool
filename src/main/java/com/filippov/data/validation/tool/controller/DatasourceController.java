package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.datasource.DatasourceType;
import com.filippov.data.validation.tool.service.DatasourceService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("datasources")
public class DatasourceController {

    private final DatasourceService datasourceService;

    public DatasourceController(DatasourceService datasourceService) {
        this.datasourceService = datasourceService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    List<DatasourceType> getAllDatasources() {
        return datasourceService.getDatasourceTypes();
    }
}
