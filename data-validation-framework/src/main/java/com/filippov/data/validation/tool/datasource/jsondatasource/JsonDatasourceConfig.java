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

package com.filippov.data.validation.tool.datasource.jsondatasource;

import com.filippov.data.validation.tool.datasource.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.model.DatasourceConfigParamsDefinition;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import lombok.Getter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

@Getter
@ToString
public class JsonDatasourceConfig implements DatasourceConfig {
    public static final String METADATA_FILE_PATH = "metadataFilePath";
    public static final String DATA_FILE_PATH = "dataFilePath";

    private final String metadataFilePath;
    private final String dataFilePath;
    private final int maxConnections;

    JsonDatasourceConfig(String metadataFilePath, String dataFilePath, int maxConnections) {
        if (metadataFilePath == null || dataFilePath == null) {
            throw new IllegalArgumentException("Incorrect input: metadata file path or data file path is null");
        }
        if (StringUtils.isEmpty(metadataFilePath) || StringUtils.isEmpty(dataFilePath)) {
            throw new IllegalArgumentException("Incorrect input: metadata file path or data file path is empty");
        }
        if (maxConnections <= 0) {
            throw new IllegalArgumentException("Incorrect input: max connections is less or equal zero");
        }
        this.metadataFilePath = metadataFilePath;
        this.dataFilePath = dataFilePath;
        this.maxConnections = maxConnections;
    }

    public static DatasourceConfigParamsDefinition getDatasourceConfigParamsDefinition() {
        return DatasourceConfigParamsDefinition.builder()
                .paramsDefinition(Map.of(
                        METADATA_FILE_PATH, "Metadata file path. Data type: String",
                        DATA_FILE_PATH, "Data file path. Data type: String"
                ))
                .build();
    }

    public static JsonDatasourceConfigBuilder builder() {
        return new JsonDatasourceConfigBuilder();
    }

    @Override
    public DatasourceType getDatasourceType() {
        return DatasourceType.JSON_DATASOURCE;
    }

    @Override
    public int getMaxConnections() {
        return maxConnections;
    }
}
