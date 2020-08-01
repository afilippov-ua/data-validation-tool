package com.filippov.data.validation.tool.binder;

import com.filippov.data.validation.tool.datastorage.Query;
import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultDataBinder implements DataBinder {

    private final DataStoragePairRepository dataStoragePairRepository;

    @Override
    public DataRowDto bind(Workspace workspace, TablePair tablePair, ColumnPair columnPair, Object key) {

        final ColumnDataPair<Object, Object, Object> columnDataPair = dataStoragePairRepository.getOrLoad(workspace)
                .getColumnData(Query.builder()
                        .tablePair(tablePair)
                        .columnPair(columnPair)
                        .build());

        final Object left = columnDataPair.getLeftColumnData().getValueByKey(key);
        final Object right = columnDataPair.getRightColumnData().getValueByKey(key);
        return DataRowDto.builder()
                .key(key)
                .leftOriginalValue(left)
                .rightOriginalValue(right)
                .leftTransformedValue(columnPair.getLeftTransformer().transform(left))
                .rightTransformedValue(columnPair.getRightTransformer().transform(right))
                .build();
    }
}
