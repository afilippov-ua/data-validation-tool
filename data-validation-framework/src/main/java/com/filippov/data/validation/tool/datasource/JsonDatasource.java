package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.filippov.data.validation.tool.JsonDataLoader;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.model.DatasourceMetadata;
import com.filippov.data.validation.tool.datasource.model.DatasourceTable;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.datastorage.RelationType;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.HashMap;
import java.util.Map;

public class JsonDatasource implements Datasource {
    private JsonDatasourceConfig datasourceConfig;
    private DatasourceMetadata metadata;
    private Map<DatasourceColumn, ColumnData<?, ?>> dataMap = new HashMap<>();

    public JsonDatasource(JsonDatasourceConfig datasourceConfig) {
        this.datasourceConfig = datasourceConfig;
    }

    @Override
    public DatasourceMetadata getMetadata() {
        if (metadata == null) {
            metadata = new JsonDataLoader().loadData(datasourceConfig.getMetadataFilePath(), new TypeReference<DatasourceMetadata>() {
            });
        }
        return metadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query) {
        if (dataMap.isEmpty()) {
            loadData();
        }
        return (ColumnData<K, V>) dataMap.get(query.getDataColumn());
    }

    @Override
    public JsonDatasourceConfig getConfig() {
        return datasourceConfig;
    }

    private void loadData() {
        final Map<String, Map<String, ColumnData>> data = new JsonDataLoader()
                .loadData(datasourceConfig.getDataFilePath(), new TypeReference<Map<String, Map<String, ColumnData>>>() {
                });

        for (DatasourceTable table : getMetadata().getTables()) {
            if (data.containsKey(table.getName())) {
                final Map<String, ColumnData> columnDataMap = data.get(table.getName());
                for (String columnName : table.getColumns()) {
                    DatasourceColumn column = getMetadata().getColumnByName(table.getName(), columnName);
                    if (columnDataMap.containsKey(column.getName())) {
                        dataMap.put(column, columnDataMap.get(column.getName()));
                    } else {
                        throw new IllegalArgumentException("Incorrect json data for the table: " + table + " and column: " + column);
                    }
                }
            } else {
                throw new IllegalArgumentException("Incorrect json data for the table: " + table);
            }
        }
    }

    @Override
    public DatasourceQuery toDatasourceQuery(Query query, RelationType relationType) {
        return DatasourceQuery.builder()
                .table(query.getTablePair().getDatasourceTableFor(relationType))
                .keyColumn(query.getTablePair().getKeyColumnPair().getColumnFor(relationType))
                .dataColumn(query.getColumnPair().getColumnFor(relationType))
                .build();
    }
}
