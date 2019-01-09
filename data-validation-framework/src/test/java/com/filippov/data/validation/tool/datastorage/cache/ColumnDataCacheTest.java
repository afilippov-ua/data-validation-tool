package com.filippov.data.validation.tool.datastorage.cache;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.FileUtils;
import com.filippov.data.validation.tool.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.datasource.DatasourceTable;
import com.filippov.data.validation.tool.model.ColumnData;
import lombok.SneakyThrows;
import net.sf.ehcache.CacheManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ColumnDataCacheTest extends AbstractTest {
    private static final DatasourceTable TABLE;
    private static final DatasourceColumn PK;
    private static final DatasourceColumn TEST_COLUMN;
    private static final List<Integer> IDS;
    private static final List<String> VALUES;
    private static final ColumnData<Integer, ?> TEST_DATA;

    static {
        TABLE = DatasourceTable.builder().name("table1").build();

        PK = DatasourceColumn.builder().name("pk").tableName(TABLE.getName()).build();
        TEST_COLUMN = DatasourceColumn.builder().name("test_column").tableName(TABLE.getName()).build();

        TABLE.setPrimaryKey(PK.getName());
        TABLE.setColumns(asList(PK.getName(), TEST_COLUMN.getName()));

        IDS = asList(1, 2, 3, 4, 5, 6, 7);
        VALUES = asList("str1", "str2", "str3", "str4", "str5", "str6", "str7");
        TEST_DATA = ColumnData.<Integer, String>builder().primaryKey(PK).column(TEST_COLUMN).keys(IDS).values(VALUES).build();
    }

    @AfterAll
    @SneakyThrows
    static void cleanUp() {
        FileUtils.delete(CACHE_PATH);
    }

    static Object[][] cacheProvider() {
        return new Object[][]{
                {newCache(EHColumnDataCache.class)},
                {newCache(KryoColumnDataCache.class)}
        };
    }

    @SneakyThrows
    static ColumnDataCache newCache(Class cacheClass) {
        if (cacheClass.equals(EHColumnDataCache.class)) {
            return new EHColumnDataCache(CacheManager.newInstance("src/test/resources/ehcache.xml").getCache("test"));
        } else if (cacheClass.equals(KryoColumnDataCache.class)) {
            return new KryoColumnDataCache(CACHE_PATH);
        } else {
            throw new IllegalArgumentException("Unsupported cache class: " + cacheClass.getName());
        }
    }

    @ParameterizedTest()
    @MethodSource("cacheProvider")
    void getOrLoadExceptionTest(ColumnDataCache cache) {
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();

        assertThrows(RuntimeException.class, () ->
                cache.getOrLoad(TEST_COLUMN, () -> {
                    throw new RuntimeException("test exception");
                }));

        cache.cleanUp();
        cache.close();
    }

    @ParameterizedTest()
    @MethodSource("cacheProvider")
    void getOrLoadTest(ColumnDataCache cache) {
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();

        ColumnData<Integer, ?> data = cache.getOrLoad(TEST_COLUMN, () -> TEST_DATA);
        assertThat(data).isNotNull();
        assertThat(data.getPrimaryKey()).isEqualTo(PK);
        assertThat(data.getColumn()).isEqualTo(TEST_COLUMN);
        assertThat(data.getKeys()).isEqualTo(IDS);
        assertThat(data.getValues()).isEqualTo(VALUES);

        cache.cleanUp();
        cache.close();
    }

    @ParameterizedTest()
    @MethodSource("cacheProvider")
    void putAndGetTest(ColumnDataCache cache) {
        cache.put(TEST_COLUMN, TEST_DATA);

        final Optional<ColumnData<Integer, String>> optionalData = cache.get(TEST_COLUMN);
        assertThat(optionalData).isNotEmpty();
        optionalData.ifPresent(data -> {
            assertThat(data.getPrimaryKey()).isEqualTo(PK);
            assertThat(data.getColumn()).isEqualTo(TEST_COLUMN);
            assertThat(data.getKeys()).isEqualTo(IDS);
            assertThat(data.getValues()).isEqualTo(VALUES);
        });

        cache.cleanUp();
        cache.close();
    }

    @ParameterizedTest()
    @MethodSource("cacheProvider")
    void putIfNotExistAndGetTest(ColumnDataCache cache) {
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();

        cache.putIfNotExist(TEST_COLUMN, () -> TEST_DATA);

        final Optional<ColumnData<Integer, String>> optionalData = cache.get(TEST_COLUMN);
        assertThat(optionalData).isNotEmpty();
        optionalData.ifPresent(data -> {
            assertThat(data.getPrimaryKey()).isEqualTo(PK);
            assertThat(data.getColumn()).isEqualTo(TEST_COLUMN);
            assertThat(data.getKeys()).isEqualTo(IDS);
            assertThat(data.getValues()).isEqualTo(VALUES);
        });

        cache.cleanUp();
        cache.close();
    }

    @ParameterizedTest()
    @MethodSource("cacheProvider")
    void isCacheExistTest(ColumnDataCache cache) {
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();
        cache.put(TEST_COLUMN, TEST_DATA);
        assertThat(cache.exist(TEST_COLUMN)).isTrue();

        cache.cleanUp();
        cache.close();
    }

    @ParameterizedTest()
    @MethodSource("cacheProvider")
    void deleteCacheTest(ColumnDataCache cache) {
        if (!cache.exist(TEST_COLUMN)) {
            cache.put(TEST_COLUMN, TEST_DATA);
        }
        assertThat(cache.exist(TEST_COLUMN)).isTrue();
        cache.delete(TEST_COLUMN);
        assertThat(cache.exist(TEST_COLUMN)).isFalse();
        assertThat(cache.get(TEST_COLUMN)).isEmpty();

        cache.cleanUp();
        cache.close();
    }
}
