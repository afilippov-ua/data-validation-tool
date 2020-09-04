package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.DatasourceService;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
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
public class DatasourceController extends AbstractController {
    private final DatasourceService datasourceService;

    public DatasourceController(WorkspaceService workspaceService, MetadataService metadataService,
                                DataStoragePairRepository dataStoragePairRepository, DtoMapper dtoMapper,
                                DatasourceService datasourceService) {
        super(workspaceService, metadataService, dataStoragePairRepository, dtoMapper);
        this.datasourceService = datasourceService;
    }

    @GetMapping(path = "/types", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DatasourceType> getAllDatasourceTypes() {
        log.debug("Calling 'getAllDatasourceTypes' endpoint");
        final Timer timer = Timer.start();

        final List<DatasourceType> result = datasourceService.getDatasourceTypes();

        log.debug("Returning datasource types list. Execution time: {}", timer.stop());
        return result;
    }
}
