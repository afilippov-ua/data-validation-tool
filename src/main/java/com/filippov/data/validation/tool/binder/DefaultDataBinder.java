package com.filippov.data.validation.tool.binder;

import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultDataBinder implements DataBinder {

    @Override
    public DataRowDto bind(ColumnDataPair<Object, Object, Object> columnDataPair, ColumnPair columnPair, Object key) {
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
