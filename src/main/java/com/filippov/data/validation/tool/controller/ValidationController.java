/*
 *   Copyright 2018-2020 the original author or authors.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        https://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.binder.DataBinder;
import com.filippov.data.validation.tool.controller.validation.InputValidator;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.validation.ValidationDataDto;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.ValidationService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import com.filippov.data.validation.tool.validation.ValidationResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static java.util.stream.Collectors.toList;

@Slf4j
@RestController
@RequestMapping("validationResults")
public class ValidationController extends AbstractController {
    private final DataBinder dataBinder;
    private final ValidationService validationService;

    public ValidationController(WorkspaceService workspaceService, MetadataService metadataService, DataStoragePairRepository dataStoragePairRepository,
                                DtoMapper dtoMapper, DataBinder dataBinder, ValidationService validationService) {
        super(workspaceService, metadataService, dataStoragePairRepository, dtoMapper);
        this.dataBinder = dataBinder;
        this.validationService = validationService;
    }

    @GetMapping(path = "/workspaces/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ValidationDataDto getValidationResults(@PathVariable("workspaceId") String workspaceId,
                                                  @PathVariable("tablePairId") String tablePairId,
                                                  @PathVariable("columnPairId") String columnPairId,
                                                  @RequestParam("offset") Integer offset,
                                                  @RequestParam("limit") Integer limit) {

        log.debug("Calling 'getValidationResults' endpoint for workspaceId: {}, tablePairId: {}, columnPairId: {}, offset: {}, limit: {}",
                workspaceId, tablePairId, columnPairId, offset, limit);
        final Timer timer = Timer.start();

        InputValidator.builder()
                .withWorkspaceId(workspaceId)
                .withTablePairId(tablePairId)
                .withColumnPairId(columnPairId)
                .withOffset(offset)
                .withLimit(limit)
                .validate();

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspace, tablePairId);
        final ColumnPair columnPair = getColumnPairByIdOrName(workspace, tablePair, columnPairId);

        final ValidationResult<?> validationResult = validationService.validate(
                workspace,
                Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());

        final ColumnDataPair<Object, Object, Object> columnDataPair = dataStoragePairRepository.getOrLoad(workspace)
                .getColumnData(Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());

        final ValidationDataDto result = ValidationDataDto.builder()
                .tablePair(dtoMapper.toDto(validationResult.getTablePair()))
                .keyColumnPair(dtoMapper.toDto(validationResult.getKeyColumnPair()))
                .dataColumnPair(dtoMapper.toDto(validationResult.getDataColumnPair()))
                .failedRows(validationResult.getFailedKeys().stream()
                        .map(id -> dataBinder.bind(columnDataPair, columnPair, id))
                        .skip(offset)
                        .limit(limit)
                        .collect(toList()))
                .build();

        log.debug("Validation finished for workspaceId: {}, tablePairId: {}, columnPairId: {}. Execution time: {}",
                workspaceId, tablePairId, columnPairId, timer.stop());
        return result;
    }
}
