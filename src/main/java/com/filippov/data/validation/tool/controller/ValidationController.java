package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.binder.DataBinder;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.validation.ValidationDataDto;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.ValidationService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import com.filippov.data.validation.tool.validation.ValidationResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("validationResults")
public class ValidationController extends AbstractController {
    private final DtoMapper dtoMapper;
    private final DataBinder dataBinder;
    private final ValidationService validationService;

    public ValidationController(WorkspaceService workspaceService, MetadataService metadataService, DataStoragePairRepository dataStoragePairRepository,
                                DtoMapper dtoMapper, DataBinder dataBinder, ValidationService validationService) {
        super(workspaceService, metadataService, dataStoragePairRepository);
        this.dtoMapper = dtoMapper;
        this.dataBinder = dataBinder;
        this.validationService = validationService;
    }

    @GetMapping(path = "/workspaces/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ValidationDataDto getValidationResults(@PathVariable("workspaceId") String workspaceId,
                                                  @PathVariable("tablePairId") String tablePairId,
                                                  @PathVariable("columnPairId") String columnPairId,
                                                  @RequestParam("offset") int offset,
                                                  @RequestParam("limit") int limit) {

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspaceId, tablePairId);
        final ColumnPair columnPair = getColumnPairByIdOrName(workspaceId, tablePairId, columnPairId);

        final ValidationResult<?> validationResult = validationService.validate(
                workspace,
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());

        return ValidationDataDto.builder()
                .tablePair(dtoMapper.toDto(validationResult.getTablePair()))
                .keyColumnPair(dtoMapper.toDto(validationResult.getKeyColumnPair()))
                .dataColumnPair(dtoMapper.toDto(validationResult.getDataColumnPair()))
                .failedRows(validationResult.getFailedKeys().stream()
                        .map(id -> dataBinder.bind(workspace, tablePair, columnPair, id))
                        .skip(offset)
                        .limit(limit)
                        .collect(toList()))
                .build();
    }
}
