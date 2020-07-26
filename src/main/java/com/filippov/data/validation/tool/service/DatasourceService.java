package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.model.DatasourceType;
import com.filippov.data.validation.tool.factory.DatasourceFactory;
import com.filippov.data.validation.tool.model.Workspace;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class DatasourceService {
    private final Map<Workspace, Datasource> leftCache;
    private final Map<Workspace, Datasource> rightCache;
    private final DatasourceFactory datasourceFactory;

    public DatasourceService(DatasourceFactory datasourceFactory) {
        this.leftCache = new ConcurrentHashMap<>();
        this.rightCache = new ConcurrentHashMap<>();
        this.datasourceFactory = datasourceFactory;
    }

    public List<DatasourceType> getDatasourceTypes() {
        return Arrays.stream(DatasourceType.values())
                .sorted()
                .collect(Collectors.toList());
    }

    public Datasource getLeftOrCreate(Workspace workspace) {
        return leftCache
                .computeIfAbsent(workspace,
                        (ws) -> datasourceFactory.create(ws.getLeft()));
    }

    public Datasource getRightOrCreate(Workspace workspace) {
        return rightCache
                .computeIfAbsent(workspace,
                        (ws) -> datasourceFactory.create(ws.getRight()));
    }

    public void deleteByWorkspace(Workspace workspace) {
        leftCache.remove(workspace);
        rightCache.remove(workspace);
    }
}
