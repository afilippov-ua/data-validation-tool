package com.filippov.data.validation.tool.binder;

import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.model.ColumnDataPair;
import com.filippov.data.validation.tool.pair.ColumnPair;

public interface DataBinder {

    DataRowDto bind(ColumnDataPair<Object, Object, Object> columnDataPair, ColumnPair columnPair, Object key);
}
