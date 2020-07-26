package com.filippov.data.validation.tool.cache;

import com.filippov.data.validation.tool.FileUtils;
import com.filippov.data.validation.tool.datasource.model.DatasourceColumn;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Slf4j
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
        final Path cachePath = Paths.get(cache.getCacheManager().getConfiguration().getDiskStoreConfiguration().getOriginalPath());
        try {
            FileUtils.delete(cachePath);
        } catch (IOException ex) {
            log.error("EH cache path hasn't been deleted! Path: {}", cachePath, ex);
        }
    }

    @Override
    public void close() {
        flush();
    }
}
