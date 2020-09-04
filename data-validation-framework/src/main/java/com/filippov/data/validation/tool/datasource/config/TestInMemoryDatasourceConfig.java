package com.filippov.data.validation.tool.datasource.config;

import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.datastorage.RelationType;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static com.filippov.data.validation.tool.datasource.model.DatasourceType.TEST_IN_MEMORY_DATASOURCE;

@ToString
@RequiredArgsConstructor
public class TestInMemoryDatasourceConfig implements DatasourceConfig {

    private final RelationType relationType;

    @Override
    public DatasourceType getDatasourceType() {
        return TEST_IN_MEMORY_DATASOURCE;
    }

    @Override
    public int getMaxConnections() {
        return 1;
    }

    public RelationType getRelation() {
        return relationType;
    }
}
