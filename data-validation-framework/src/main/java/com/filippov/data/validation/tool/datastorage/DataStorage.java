package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.model.ColumnData;

public interface DataStorage {

    RelationType getRelationType();

    Datasource getDatasource();

    ColumnData getData(Query query);

    void preloadInBackground(Query query);

    void stopBackgroundPreloading();

    void deleteCache(Query query);
}
