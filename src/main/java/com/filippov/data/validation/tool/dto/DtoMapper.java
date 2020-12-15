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

import com.filippov.data.validation.tool.datasource.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasourceConfig;
import com.filippov.data.validation.tool.dto.cache.CacheInfoDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceColumnDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceConfigParamsDefinitionDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceTableDto;
import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.dto.validation.TransformerDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import com.filippov.data.validation.tool.model.DatasourceConfig;
import com.filippov.data.validation.tool.model.Transformer;
import com.filippov.data.validation.tool.model.binder.DataRow;
import com.filippov.data.validation.tool.model.cache.CacheInfo;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.datasource.DatasourceConfigParamsDefinition;
import com.filippov.data.validation.tool.model.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.metadata.Metadata;
import com.filippov.data.validation.tool.model.pair.ColumnDataInfoPair;
import com.filippov.data.validation.tool.model.pair.ColumnPair;
import com.filippov.data.validation.tool.model.pair.TablePair;
import com.filippov.data.validation.tool.model.workspace.Workspace;
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
                configParams.put(JsonDatasourceConfig.METADATA_FILE_PATH, ((JsonDatasourceConfig) datasourceConfig).getMetadataFilePath());
                configParams.put(JsonDatasourceConfig.DATA_FILE_PATH, ((JsonDatasourceConfig) datasourceConfig).getDataFilePath());
                return DatasourceDefinitionDto.builder()
                        .datasourceType(datasourceConfig.getDatasourceType())
                        .maxConnections(datasourceConfig.getMaxConnections())
                        .configParams(configParams)
                        .build();
            case TEST_IN_MEMORY_DATASOURCE:
                configParams.put(TestInMemoryDatasourceConfig.RELATION_TYPE, ((TestInMemoryDatasourceConfig) datasourceConfig).getRelation());
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
                        .maxConnections(datasourceDefinitionDto.getMaxConnections())
                        .configParams(datasourceDefinitionDto.getConfigParams())
                        .build();
            case TEST_IN_MEMORY_DATASOURCE:
                return TestInMemoryDatasourceConfig.builder()
                        .maxConnections(datasourceDefinitionDto.getMaxConnections())
                        .configParams(datasourceDefinitionDto.getConfigParams())
                        .build();
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
                .primaryKey(datasourceTable.getPrimaryKey())
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

    public CacheInfoDto toDto(CacheInfo cacheInfo) {
        return CacheInfoDto.builder()
                .cached(cacheInfo.isCached())
                .date(cacheInfo.getDate())
                .build();
    }

    public ColumnPairCacheDetailsDto toDto(ColumnDataInfoPair columnDataInfoPair) {
        return ColumnPairCacheDetailsDto.builder()
                .columnPair(toDto(columnDataInfoPair.getColumnPair()))
                .leftCacheInfo(toDto(columnDataInfoPair.getLeftCacheInfo()))
                .rightCacheInfo(toDto(columnDataInfoPair.getRightCacheInfo()))
                .build();
    }

    public DatasourceConfigParamsDefinitionDto toDto(DatasourceConfigParamsDefinition datasourceConfigParamsDefinition) {
        return DatasourceConfigParamsDefinitionDto.builder()
                .paramsDefinition(datasourceConfigParamsDefinition.getParamsDefinition())
                .build();

    }

    public DatasourceConfigParamsDefinition fromDto(DatasourceConfigParamsDefinitionDto datasourceConfigParamsDefinitionDto) {
        return DatasourceConfigParamsDefinition.builder()
                .paramsDefinition(datasourceConfigParamsDefinitionDto.getParamsDefinition())
                .build();

    }

    public DataRowDto toDto(DataRow dataRow) {
        return DataRowDto.builder()
                .key(dataRow.getKey())
                .leftOriginalValue(dataRow.getLeftOriginalValue())
                .rightOriginalValue(dataRow.getRightOriginalValue())
                .leftTransformedValue(dataRow.getLeftTransformedValue())
                .rightTransformedValue(dataRow.getRightTransformedValue())
                .build();
    }
}
