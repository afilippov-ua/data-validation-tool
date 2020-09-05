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

package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.metadata.Metadata;
import com.filippov.data.validation.tool.metadata.MetadataBinder;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Component
public class MetadataService {
    private final Map<Workspace, Metadata> cache;
    private final MetadataBinder metadataBinder;
    private final DataStoragePairRepository dataStoragePairRepository;

    public MetadataService(MetadataBinder metadataBinder, DataStoragePairRepository dataStoragePairRepository) {
        this.cache = new ConcurrentHashMap<>();
        this.metadataBinder = metadataBinder;
        this.dataStoragePairRepository = dataStoragePairRepository;
    }

    public Metadata getMetadata(Workspace workspace) {
        final DataStoragePair dsPair = dataStoragePairRepository.getOrLoad(workspace);
        final Metadata result = cache
                .computeIfAbsent(workspace,
                        (ws) -> metadataBinder.bind(
                                dsPair.getLeftDataStorage().getDatasource().getMetadata(),
                                dsPair.getRightDataStorage().getDatasource().getMetadata()));
        return result;
    }

    public List<TablePair> getTablePairs(Workspace workspace) {
        log.debug("Getting list of table pairs for workspace: {}", workspace.getId());
        final List<TablePair> result = getMetadata(workspace).getTablePairs();
        log.debug("List of table pairs for workspace: {} has been successfully loaded", workspace.getId());
        return result;
    }

    public List<ColumnPair> getColumnPairs(Workspace workspace, String tablePairId) {
        log.debug("Getting list of column pairs for workspace: {} and table pair: {}", workspace.getId(), tablePairId);
        final Metadata metadata = getMetadata(workspace);
        final List<ColumnPair> result = metadata.getTablePairByIdOrName(tablePairId)
                .map(tablePair -> metadata.getColumnPairs(tablePair).stream()
                        .filter(columnPair -> columnPair.getTablePair().getId().equals(tablePairId))
                        .collect(Collectors.toList()))
                .orElseThrow(() -> new IllegalArgumentException("Incorrect table pair name: " + tablePairId));
        log.debug("List of column pairs for workspace: {} and table pair: {}", workspace.getId(), tablePairId);
        return result;
    }
}
