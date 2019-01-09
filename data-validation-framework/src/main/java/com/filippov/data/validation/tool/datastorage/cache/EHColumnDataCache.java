package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.FileUtils;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.SneakyThrows;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.nio.file.Paths;
import java.util.Optional;

public class EHColumnDataCache implements ColumnDataCache {
    private final Ehcache cache;

    public EHColumnDataCache(Ehcache cache) {
        this.cache = cache;
    }

    @Override
    @SneakyThrows
    public <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column) {
        if (exist(column)) {
            return Optional.of((ColumnData<K, V>) cache.get(column).getObjectValue());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public <K, V> void put(DatasourceColumn column, ColumnData<K, V> columnData) {
        cache.put(new Element(column, columnData));
    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return cache.isKeyInCache(column);
    }

    @Override
    public void delete(DatasourceColumn column) {
        cache.remove(column);
    }

    @Override
    public void flush() {
        cache.flush();
    }

    @Override
    @SneakyThrows
    public void cleanUp() {
        FileUtils.delete(Paths.get(cache.getCacheManager().getConfiguration().getDiskStoreConfiguration().getOriginalPath()));
    }

    @Override
    public void close() {
        flush();
    }
}
