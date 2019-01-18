package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.filippov.data.validation.tool.JsonDataLoader;
import com.filippov.data.validation.tool.datasource.query.DatasourceQuery;
import com.filippov.data.validation.tool.model.ColumnData;

import java.util.HashMap;
import java.util.Map;

public class TestJsonDatasource implements Datasource {
    private final String metadataFilePath;
    private final String dataFilePath;
    private DatasourceMetadata metadata;
    private Map<DatasourceColumn, ColumnData<?, ?>> dataMap = new HashMap<>();

    public TestJsonDatasource(String metadataFilePath, String dataFilePath) {
        this.metadataFilePath = metadataFilePath;
        this.dataFilePath = dataFilePath;
    }

    @Override
    public String getConnectionString() {
        return "metadata_file:" + metadataFilePath + ";data_file:" + dataFilePath;
    }

    @Override
    public DatasourceMetadata getMetadata() {
        if (metadata == null) {
            metadata = new JsonDataLoader().loadData(metadataFilePath, new TypeReference<DatasourceMetadata>() {
            });
        }
        return metadata;
    }

    @Override
    public <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query) {
        if (dataMap.isEmpty()) {
            loadData();
        }
        return (ColumnData<K, V>) dataMap.get(query.getColumn());
    }

    private void loadData() {
        final Map<String, Map<String, ColumnData>> data = new JsonDataLoader().loadData(dataFilePath, new TypeReference<Map<String, Map<String, ColumnData>>>() {
        });

        for (DatasourceTable table : getMetadata().getTables()) {
            if (data.containsKey(table.getName())) {
                final Map<String, ColumnData> columnDataMap = data.get(table.getName());
                for (String columnName : table.getColumns()) {
                    DatasourceColumn column = getMetadata().getColumnByName(table.getName(), columnName).get();
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
}
