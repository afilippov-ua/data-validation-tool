package com.filippov.data.validation.tool.datastorage.cache;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.filippov.data.validation.tool.FileUtils;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnData;
import com.filippov.data.validation.tool.model.DataType;
import de.javakaffee.kryoserializers.ArraysAsListSerializer;
import de.javakaffee.kryoserializers.SynchronizedCollectionsSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.objenesis.strategy.StdInstantiatorStrategy;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Optional;

@Slf4j
public class KryoColumnDataCache implements ColumnDataCache {
    private static final Class[] classes = {ColumnData.class, DatasourceTable.class, DatasourceColumn.class, DataType.class};
    private static final ThreadLocal<Kryo> kryo = ThreadLocal.withInitial(() -> {
        Kryo kryo = new Kryo();
        kryo.setInstantiatorStrategy(new Kryo.DefaultInstantiatorStrategy(new StdInstantiatorStrategy()));
        Arrays.stream(classes).forEach(kryo::register);

        kryo.register(Arrays.asList("").getClass(), new ArraysAsListSerializer());
        UnmodifiableCollectionsSerializer.registerSerializers(kryo);
        SynchronizedCollectionsSerializer.registerSerializers(kryo);
        return kryo;
    });

    private final Path cachePath;

    public KryoColumnDataCache(Path cachePath) {
        this.cachePath = cachePath;
    }

    @Override
    public <K, V> Optional<ColumnData<K, V>> get(DatasourceColumn column) {
        if (exist(column)) {
            final Path path = resolvePath(column);
            try (Input input = new Input(Files.newInputStream(path, StandardOpenOption.READ))) {
                return Optional.ofNullable(kryo.get().readObject(input, ColumnData.class));
            } catch (IOException e) {
                log.error("Exception while reading data from disk: {}", path);
                log.debug(e.getMessage(), e);
            }
        }
        return Optional.empty();
    }

    @Override
    public <K, V> void put(DatasourceColumn column, ColumnData<K, V> columnData) {
        final Path path = resolvePath(column);
        mkdirs(path.getParent());
        try (Output output = new Output(Files.newOutputStream(path))) {
            kryo.get().writeObject(output, columnData);
        } catch (IOException e) {
            log.error("Exception while writing data to disk: {}", path);
            log.debug(e.getMessage(), e);
        }
    }

    @Override
    public boolean exist(DatasourceColumn column) {
        return Files.exists(resolvePath(column));
    }

    @Override
    public void delete(DatasourceColumn column) {
        final Path path = resolvePath(column);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            log.error("Exception while deleting data from disk: {}", path);
            log.debug(e.getMessage(), e);
        }
    }

    @Override
    public void flush() {
    }

    @Override
    @SneakyThrows
    public void cleanUp() {
        FileUtils.delete(cachePath);
    }

    @Override
    public void close() {
    }

    private Path resolvePath(DatasourceColumn column) {
        return cachePath.resolve(column.getTableName() + "/" + column.getName());
    }

    private void mkdirs(Path path) {
        try {
            if (!Files.exists(path)) {
                Files.createDirectories(path);
            }
        } catch (IOException e) {
            log.error("Exception while creating parent directories: {}", path);
            log.debug(e.getMessage(), e);
        }
    }
}
