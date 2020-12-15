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

package com.filippov.data.validation.tool.datasource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.filippov.data.validation.tool.datasource.utils.JsonDataLoader;
import com.filippov.data.validation.tool.model.Datasource;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.datasource.DatasourceMetadata;
import com.filippov.data.validation.tool.model.datasource.DatasourceQuery;
import com.filippov.data.validation.tool.model.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.datastorage.Query;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class JsonDatasource implements Datasource {
    private final JsonDatasourceConfig datasourceConfig;

    private DatasourceMetadata metadata;
    private Map<DatasourceColumn, ColumnData<?, ?>> dataMap; // TODO : how and where to store the loaded data?

    @Override
    public DatasourceMetadata getMetadata() {
        if (metadata == null) {
            metadata = loadMetadata(); // TODO: not thread-safe. Fix before release
        }
        return metadata;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> ColumnData<K, V> getColumnData(DatasourceQuery query) {
        log.debug("Loading data from JSON-datasource with query: {}", query);
        if (dataMap == null) {
            dataMap = loadData(); // TODO: not thread-safe. Fix before release
        }
        final ColumnData<K, V> result = (ColumnData<K, V>) dataMap.get(query.getDataColumn());
        log.debug("Data from JSON-datasource has been successfully loaded with query: {}", query);
        return result;
    }

    @Override
    public JsonDatasourceConfig getConfig() {
        return datasourceConfig;
    }

    @Override
    public DatasourceQuery toDatasourceQuery(Query query, RelationType relationType) {
        return DatasourceQuery.builder()
                .table(query.getTablePair().getDatasourceTableFor(relationType))
                .keyColumn(query.getTablePair().getKeyColumnPair().getColumnFor(relationType))
                .dataColumn(query.getColumnPair().getColumnFor(relationType))
                .build();
    }

    public String toString() {
        return "JsonDatasource(datasourceConfig=" + this.datasourceConfig + ")";
    }

    private DatasourceMetadata loadMetadata() {
        return new JsonDataLoader().loadData(datasourceConfig.getMetadataFilePath(), new TypeReference<>() {
        });
    }

    private Map<DatasourceColumn, ColumnData<?, ?>> loadData() {
        final Map<DatasourceColumn, ColumnData<?, ?>> result = new HashMap<>();

        final Map<String, Map<String, ColumnData<?, ?>>> data = new JsonDataLoader()
                .loadData(datasourceConfig.getDataFilePath(), new TypeReference<>() {
                });

        for (DatasourceTable table : getMetadata().getTables()) {
            if (data.containsKey(table.getName())) {
                final Map<String, ColumnData<?, ?>> columnDataMap = data.get(table.getName());
                for (String columnName : table.getColumns()) {
                    DatasourceColumn column = getMetadata().getColumnByName(table.getName(), columnName);
                    if (columnDataMap.containsKey(column.getName())) {
                        result.put(column, columnDataMap.get(column.getName()));
                    } else {
                        throw new IllegalArgumentException("Incorrect json data for the table: " + table + " and column: " + column);
                    }
                }
            } else {
                throw new IllegalArgumentException("Incorrect json data for the table: " + table);
            }
        }
        return result;
    }
}
