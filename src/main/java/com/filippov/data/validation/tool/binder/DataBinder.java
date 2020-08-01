package com.filippov.data.validation.tool.binder;

import com.filippov.data.validation.tool.dto.validation.DataRowDto;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;

public interface DataBinder {

    DataRowDto bind(Workspace workspace, TablePair tablePair, ColumnPair columnPair, Object id);
}
