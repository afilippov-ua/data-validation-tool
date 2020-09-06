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

package com.filippov.data.validation.tool.dto;

import com.filippov.data.validation.tool.datasource.config.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.dto.cache.ColumnCacheDetailsDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceColumnDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceTableDto;
import com.filippov.data.validation.tool.dto.validation.TransformerDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.model.ColumnDataInfo;
import com.filippov.data.validation.tool.pair.ColumnDataInfoPair;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.validation.transformer.Transformer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

// TODO: decompose to a set of separate mappers: too large
@Component
public class DtoMapper {
    private static final String METADATA_FILE_PATH = "metadataFilePath";
    private static final String DATA_FILE_PATH = "dataFilePath";
    private static final String RELATION = "relation";

    public WorkspaceDto toDto(Workspace workspace) {
        return WorkspaceDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .left(toDto(workspace.getLeftDatasourceConfig()))
                .right(toDto(workspace.getRightDatasourceConfig()))
                .build();
    }

    public DatasourceDefinitionDto toDto(DatasourceConfig datasourceConfig) {
        final Map<String, Object> configParams = new HashMap<>();
        switch (datasourceConfig.getDatasourceType()) {
            case JSON_DATASOURCE:
                configParams.put(METADATA_FILE_PATH, ((JsonDatasourceConfig) datasourceConfig).getMetadataFilePath());
                configParams.put(DATA_FILE_PATH, ((JsonDatasourceConfig) datasourceConfig).getDataFilePath());
                return DatasourceDefinitionDto.builder()
                        .datasourceType(datasourceConfig.getDatasourceType())
                        .maxConnections(datasourceConfig.getMaxConnections())
                        .configParams(configParams)
                        .build();
            case TEST_IN_MEMORY_DATASOURCE:
                configParams.put(RELATION, ((TestInMemoryDatasourceConfig) datasourceConfig).getRelation());
                return DatasourceDefinitionDto.builder()
                        .datasourceType(datasourceConfig.getDatasourceType())
                        .maxConnections(datasourceConfig.getMaxConnections())
                        .configParams(configParams)
                        .build();
            default:
                throw new UnsupportedOperationException("Unsupported datasource type: " + datasourceConfig.getDatasourceType());
        }
    }

    public Workspace fromDto(WorkspaceDto workspaceDTO) {
        return Workspace.builder()
                .id(workspaceDTO.getId())
                .name(workspaceDTO.getName())
                .leftDatasourceConfig(fromDto(workspaceDTO.getLeft()))
                .rightDatasourceConfig(fromDto(workspaceDTO.getRight()))
                .build();
    }

    public DatasourceConfig fromDto(DatasourceDefinitionDto datasourceDefinitionDto) {
        switch (datasourceDefinitionDto.getDatasourceType()) {
            case JSON_DATASOURCE:
                return JsonDatasourceConfig.builder()
                        .metadataFilePath((String) datasourceDefinitionDto.getConfigParams().get(METADATA_FILE_PATH))
                        .dataFilePath((String) datasourceDefinitionDto.getConfigParams().get(DATA_FILE_PATH))
                        .maxConnections(datasourceDefinitionDto.getMaxConnections())
                        .build();
            case TEST_IN_MEMORY_DATASOURCE:
                return new TestInMemoryDatasourceConfig(
                        RelationType.parse((String) datasourceDefinitionDto
                                .getConfigParams().get("relation")),
                        datasourceDefinitionDto.getMaxConnections());
            default:
                throw new UnsupportedOperationException("Unsupported datasource type: " + datasourceDefinitionDto.getDatasourceType());
        }
    }

    public WorkspaceMetadataDto toDto(Metadata metadata) {
        return WorkspaceMetadataDto.builder()
                .tablePairs(metadata.getTablePairs().stream()
                        .map(this::toDto)
                        .collect(toMap(
                                TablePairDto::getName,
                                Function.identity())))
                .columnPairs(metadata.getTablePairs().stream()
                        .collect(toMap(
                                TablePair::getName,
                                tablePair -> metadata.getColumnPairs(tablePair).stream()
                                        .collect(toMap(
                                                ColumnPair::getName,
                                                this::toDto)))))
                .build();
    }

    public TablePairDto toDto(TablePair tablePair) {
        return TablePairDto.builder()
                .id(tablePair.getId())
                .name(tablePair.getName())
                .leftDatasourceTable(toDto(tablePair.getLeftDatasourceTable()))
                .rightDatasourceTable(toDto(tablePair.getRightDatasourceTable()))
                .build();
    }

    public ColumnPairDto toDto(ColumnPair columnPair) {
        return ColumnPairDto.builder()
                .id(columnPair.getId())
                .tablePairId(columnPair.getTablePair().getId())
                .name(columnPair.getName())
                .leftDatasourceColumn(toDto(columnPair.getLeftDatasourceColumn()))
                .rightDatasourceColumn(toDto(columnPair.getRightDatasourceColumn()))
                .leftTransformers(toDto(columnPair.getLeftTransformer()))
                .rightTransformers(toDto(columnPair.getRightTransformer()))
                .leftTransformedDataType(columnPair.getLeftTransformer().getLastTransformer().getOutputDataType())
                .rightTransformedDataType(columnPair.getRightTransformer().getLastTransformer().getOutputDataType())
                .build();
    }

    private List<TransformerDto> toDto(Transformer transformer) {
        final List<TransformerDto> result = new ArrayList<>();
        if (transformer == null) {
            return null;
        }

        while (transformer != null) {
            result.add(TransformerDto.builder()
                    .name(transformer.getClass().getSimpleName())
                    .inputDataType(transformer.getInputDataType())
                    .outputDataType(transformer.getOutputDataType())
                    .build());
            transformer = transformer.getNext();
        }
        return result;
    }

    public DatasourceTableDto toDto(DatasourceTable datasourceTable) {
        return DatasourceTableDto.builder()
                .name(datasourceTable.getName())
                .primaryKey(datasourceTable.getPrimaryKeyName())
                .columns(datasourceTable.getColumns())
                .build();
    }

    public DatasourceColumnDto toDto(DatasourceColumn datasourceColumn) {
        return DatasourceColumnDto.builder()
                .tableName(datasourceColumn.getTableName())
                .name(datasourceColumn.getName())
                .dataType(datasourceColumn.getDataType())
                .build();
    }

    public ColumnCacheDetailsDto toDto(ColumnDataInfo columnDataInfo) {
        return ColumnCacheDetailsDto.builder()
                .cached(columnDataInfo.isCached())
                .date(columnDataInfo.getDate())
                .build();
    }

    public ColumnPairCacheDetailsDto toDto(ColumnDataInfoPair columnDataInfoPair) {
        return ColumnPairCacheDetailsDto.builder()
                .columnPair(toDto(columnDataInfoPair.getColumnPair()))
                .leftCacheInfo(toDto(columnDataInfoPair.getLeftColumnDataInfo()))
                .rightCacheInfo(toDto(columnDataInfoPair.getRightColumnDataInfo()))
                .build();
    }
}
