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


import com.filippov.data.validation.tool.model.datastorage.RelationType;

import java.util.Map;

public class TestInMemoryDatasourceConfigBuilder {
    private Map<String, Object> configParams;
    private RelationType relationType;
    private Integer maxConnections;

    TestInMemoryDatasourceConfigBuilder() {
    }

    public TestInMemoryDatasourceConfigBuilder relationType(RelationType relationType) {
        this.relationType = relationType;
        return this;
    }

    public TestInMemoryDatasourceConfigBuilder configParams(Map<String, Object> configParams) {
        this.configParams = configParams;
        return this;
    }

    public TestInMemoryDatasourceConfigBuilder maxConnections(Integer maxConnections) {
        this.maxConnections = maxConnections;
        return this;
    }

    public TestInMemoryDatasourceConfig build() {
        if (configParams != null) {
            return new TestInMemoryDatasourceConfig(
                    RelationType.parse((String) this.configParams.get(TestInMemoryDatasourceConfig.RELATION_TYPE)),
                    this.maxConnections);
        } else {
            return new TestInMemoryDatasourceConfig(this.relationType, this.maxConnections);
        }
    }
}
