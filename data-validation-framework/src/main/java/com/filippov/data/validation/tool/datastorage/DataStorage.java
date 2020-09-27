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

package com.filippov.data.validation.tool.datastorage;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.model.CachingStatus;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.ColumnDataInfo;

public interface DataStorage {

    DataStorageConfig getConfig();

    Datasource getDatasource();

    <K, V> ColumnData<K, V> getData(Query query);

    ColumnDataInfo getColumnDataInfo(Query query);

    void preloadInBackground(Query query);

    void stopPreloadInBackground(Query query);

    CachingStatus getCachingStatus(Query query);

    void deleteCache(Query query);

    void deleteCache();
}
