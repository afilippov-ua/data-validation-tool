package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.DataStoragePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.validation.DefaultDataValidator;
import com.filippov.data.validation.tool.validation.ValidationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ValidationService {

    private final DataStoragePairRepository dataStoragePairRepository;

    public <K, LV, RV> ValidationResult<K> validate(Workspace workspace, Query query) {
        final DataStoragePair dataStoragePair = dataStoragePairRepository.getOrLoad(workspace);
        final ColumnDataPair<K, LV, RV> columnDataPair = dataStoragePair.getColumnData(query);
        return DefaultDataValidator.builder()
                .tablePair(query.getTablePair())
                .keyColumnPair(query.getTablePair().getKeyColumnPair())
                .dataColumnPair(query.getColumnPair())
                .build()
                .validate(columnDataPair.getLeftColumnData(), columnDataPair.getRightColumnData());
    }
}
