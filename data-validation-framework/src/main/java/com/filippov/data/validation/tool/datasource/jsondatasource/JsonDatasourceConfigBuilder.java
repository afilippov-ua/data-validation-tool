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

import java.util.Map;

public class JsonDatasourceConfigBuilder {
    private Map<String, Object> configParams;
    private String metadataFilePath;
    private String dataFilePath;
    private int maxConnections;

    JsonDatasourceConfigBuilder() {
    }

    public JsonDatasourceConfigBuilder metadataFilePath(String metadataFilePath) {
        this.metadataFilePath = metadataFilePath;
        return this;
    }

    public JsonDatasourceConfigBuilder dataFilePath(String dataFilePath) {
        this.dataFilePath = dataFilePath;
        return this;
    }

    public JsonDatasourceConfigBuilder configParams(Map<String, Object> configParams) {
        this.configParams = configParams;
        return this;
    }

    public JsonDatasourceConfigBuilder maxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public JsonDatasourceConfig build() {
        if (this.configParams != null) {
            return new JsonDatasourceConfig(
                    (String) this.configParams.get(JsonDatasourceConfig.METADATA_FILE_PATH),
                    (String) this.configParams.get(JsonDatasourceConfig.DATA_FILE_PATH),
                    this.maxConnections);
        } else {
            return new JsonDatasourceConfig(this.metadataFilePath, this.dataFilePath, this.maxConnections);
        }
    }
}
