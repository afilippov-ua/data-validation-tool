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

package com.filippov.data.validation.tool.factory;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.cache.InMemoryColumnDataCache;
import com.filippov.data.validation.tool.datasource.Datasource;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DefaultColumnDataCacheFactory implements ColumnDataCacheFactory {

    @Override
    public ColumnDataCache getOrCreateForDatasource(Datasource datasource) {
        log.debug("Creating column data cache for datasource: {}. Using: InMemoryColumnDataCache", datasource);
        return new InMemoryColumnDataCache(); // TODO: use in-memory only for now
    }
}
