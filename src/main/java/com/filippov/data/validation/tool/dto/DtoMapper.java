package com.filippov.data.validation.tool.dto;

import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.dto.cache.ColumnCacheDetailsDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceColumnDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceDefinitionDto;
import com.filippov.data.validation.tool.dto.datasource.DatasourceTableDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceDto;
import com.filippov.data.validation.tool.dto.workspace.WorkspaceMetadataDto;
import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.model.ColumnCacheDetails;
import com.filippov.data.validation.tool.model.ColumnPairCacheDetails;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.springframework.stereotype.Component;

import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Component
public class DtoMapper {

    public WorkspaceDto toDto(Workspace workspace) {
        return WorkspaceDto.builder()
                .id(workspace.getId())
                .name(workspace.getName())
                .left(toDto(workspace.getLeft()))
                .right(toDto(workspace.getRight()))
                .build();
    }

    public DatasourceDefinitionDto toDto(DatasourceConfig datasourceConfig) {
        return DatasourceDefinitionDto.builder()
                .datasourceType(datasourceConfig.getDatasourceType())
                .config(datasourceConfig.getConfig())
                .build();
    }

    public Workspace fromDto(WorkspaceDto workspaceDTO) {
        return Workspace.builder()
                .id(workspaceDTO.getId())
                .name(workspaceDTO.getName())
                .left(fromDto(workspaceDTO.getLeft()))
                .right(fromDto(workspaceDTO.getRight()))
                .build();
    }

    public DatasourceConfig fromDto(DatasourceDefinitionDto datasourceDefinitionDto) {
        return DatasourceConfig.builder()
                .datasourceType(datasourceDefinitionDto.getDatasourceType())
                .config(datasourceDefinitionDto.getConfig())
                .build();
    }

    public WorkspaceMetadataDto toDto(Metadata metadata) {
        return WorkspaceMetadataDto.builder()
                .tablePairs(metadata.getTablePairsById().stream()
                        .map(this::toDto)
                        .collect(toMap(
                                TablePairDto::getName,
                                Function.identity())))
                .columnPairs(metadata.getTablePairsById().stream()
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
                .name(tablePair.getName())
                .left(toDto(tablePair.getLeft()))
                .right(toDto(tablePair.getRight()))
                .build();
    }

    public ColumnPairDto toDto(ColumnPair columnPair) {
        return ColumnPairDto.builder()
                .name(columnPair.getName())
                .tablePairName(columnPair.getTablePair().getName())
                .left(toDto(columnPair.getLeft()))
                .right(toDto(columnPair.getRight()))
                .build();
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

    public ColumnCacheDetailsDto toDto(ColumnCacheDetails columnCacheDetails) {
        return ColumnCacheDetailsDto.builder()
                .cached(columnCacheDetails.isCached())
                .date(columnCacheDetails.getDate())
                .build();
    }

    public ColumnPairCacheDetailsDto toDto(ColumnPairCacheDetails columnPairCacheDetails) {
        return ColumnPairCacheDetailsDto.builder()
                .columnPair(toDto(columnPairCacheDetails.getColumnPair()))
                .leftCacheInfo(toDto(columnPairCacheDetails.getLeftCacheInfo()))
                .rightCacheInfo(toDto(columnPairCacheDetails.getRightCacheInfo()))
                .build();
    }
}
