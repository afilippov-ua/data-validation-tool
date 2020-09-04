package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.validation.DefaultDataValidator;
import com.filippov.data.validation.tool.validation.ValidationResult;
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
