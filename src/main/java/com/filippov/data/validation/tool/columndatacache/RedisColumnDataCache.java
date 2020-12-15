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

package com.filippov.data.validation.tool.columndatacache;

import com.filippov.data.validation.tool.model.ColumnDataCache;
import com.filippov.data.validation.tool.model.cache.CacheConfig;
import com.filippov.data.validation.tool.model.cache.CacheInfo;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.datastorage.RelationType;
import com.filippov.data.validation.tool.model.workspace.Workspace;
import com.filippov.data.validation.tool.repository.redis.model.RedisCacheInfo;
import com.filippov.data.validation.tool.repository.redis.model.RedisColumnData;
import com.filippov.data.validation.tool.repository.redis.repository.RedisCacheInfoRepository;
import com.filippov.data.validation.tool.repository.redis.repository.RedisColumnDataRepository;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.Optional;

@Slf4j
public class RedisColumnDataCache implements ColumnDataCache {
    private static final String GROUP_DELIMITER = "/";

    private final Workspace workspace;
    private final RelationType relationType;
    private final CacheConfig cacheConfig;
    private final RedisColumnDataRepository redisColumnDataRepository;
    private final RedisCacheInfoRepository redisCacheInfoRepository;

    @NonNull
    public RedisColumnDataCache(Workspace workspace, RelationType relationType, CacheConfig cacheConfig,
                                RedisColumnDataRepository redisColumnDataRepository, RedisCacheInfoRepository redisCacheInfoRepository) {
        if (workspace.getId() == null) {
            throw new IllegalArgumentException("Workspace id is null");
        }
        this.workspace = workspace;
        this.relationType = relationType;
        this.cacheConfig = cacheConfig;
        this.redisColumnDataRepository = redisColumnDataRepository;
        this.redisCacheInfoRepository = redisCacheInfoRepository;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column) {
        return redisColumnDataRepository.findById(getColumnDataKey(column))
                .map(RedisColumnData::getColumnData);
    }

    @Override
    public CacheInfo getColumnCacheDetails(DatasourceColumn column) {
        return redisCacheInfoRepository.findById(getCacheInfoKey(column))
                .map(RedisCacheInfo::getCacheInfo)
                .orElse(CacheInfo.builder()
                        .cached(false)
                        .build());
    }

    @Override
    public <K, V> void put(DatasourceColumn column, ColumnData<K, V> columnData) {
        //TODO: the simplest way for now
        redisColumnDataRepository.save(RedisColumnData.<K, V>builder()
                .id(getColumnDataKey(column))
                .columnData(columnData)
                .build());
        redisCacheInfoRepository.save(RedisCacheInfo.builder()
                .id(getCacheInfoKey(column))
                .cacheInfo(CacheInfo.builder()
                        .date(Instant.now())
                        .cached(true)
                        .build())
                .build());
    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return redisColumnDataRepository.existsById(getColumnDataKey(column));
    }

    @Override
    public void delete(DatasourceColumn column) {
        //TODO: the simplest way for now
        redisColumnDataRepository.findById(getColumnDataKey(column))
                .ifPresent(redisColumnDataRepository::delete);
        redisCacheInfoRepository.findById(getCacheInfoKey(column))
                .ifPresent(redisCacheInfoRepository::delete);
    }

    @Override
    public void deleteAll() {
        //TODO: the simplest way for now
        redisColumnDataRepository.deleteAll();
        redisCacheInfoRepository.deleteAll();
    }

    private String getColumnDataKey(DatasourceColumn column) {
        return workspace.getId()
                + GROUP_DELIMITER + relationType
                + GROUP_DELIMITER + column.getTableName()
                + GROUP_DELIMITER + column.getName();
    }

    private String getCacheInfoKey(DatasourceColumn column) {
        return workspace.getId()
                + GROUP_DELIMITER + relationType
                + GROUP_DELIMITER + column.getTableName()
                + GROUP_DELIMITER + column.getName();
    }
}