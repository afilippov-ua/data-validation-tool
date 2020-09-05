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

package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.JsonDatasource;
import com.filippov.data.validation.tool.datasource.TestInMemoryDatasource;
import com.filippov.data.validation.tool.datasource.config.DatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.JsonDatasourceConfig;
import com.filippov.data.validation.tool.datasource.config.TestInMemoryDatasourceConfig;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultDatasourceFactory implements DatasourceFactory {

    @Override
    public Datasource create(DatasourceConfig datasourceConfig) {
        switch (datasourceConfig.getDatasourceType()) {
            case JSON_DATASOURCE:
                log.debug("Creating 'JsonDatasource' for datasource config: {}", datasourceConfig);
                return new JsonDatasource((JsonDatasourceConfig) datasourceConfig);
            case TEST_IN_MEMORY_DATASOURCE:
                log.debug("Creating 'TestInMemoryDatasource' for datasource config: {}", datasourceConfig);
                return new TestInMemoryDatasource((TestInMemoryDatasourceConfig) datasourceConfig);
            default:
                throw new IllegalArgumentException("Datasource type: " + datasourceConfig.getDatasourceType() + " doesn't supported.");
        }
    }
}
