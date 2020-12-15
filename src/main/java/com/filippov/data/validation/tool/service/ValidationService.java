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

import com.filippov.data.validation.tool.model.datastorage.Query;
import com.filippov.data.validation.tool.model.pair.ColumnDataPair;
import com.filippov.data.validation.tool.model.pair.DataStoragePair;
import com.filippov.data.validation.tool.model.validation.ValidationResult;
import com.filippov.data.validation.tool.model.workspace.Workspace;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.validation.DefaultDataValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ValidationService {

    private final DataStoragePairRepository dataStoragePairRepository;

    public <K, LV, RV> ValidationResult<K> validate(Workspace workspace, Query query) {
        log.debug("Starting validation execution for workspace: {} and query: {}", workspace.getId(), query);
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        final ColumnDataPair<K, LV, RV> columnDataPair = dataStoragePair.getColumnData(query);
        final ValidationResult<K> result = DefaultDataValidator.builder()
                .tablePair(query.getTablePair())
                .keyColumnPair(query.getTablePair().getKeyColumnPair())
                .dataColumnPair(query.getColumnPair())
                .build()
                .validate(columnDataPair.getLeftColumnData(), columnDataPair.getRightColumnData());
        log.debug("Validation has been successfully executed for workspace: {} and query: {}", workspace.getId(), query);
        return result;
    }
}
