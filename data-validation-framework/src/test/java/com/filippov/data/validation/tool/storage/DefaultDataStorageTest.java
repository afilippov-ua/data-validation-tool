package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.AbstractTest;
import com.filippov.data.validation.tool.datastorage.DataStorage;
import com.filippov.data.validation.tool.datastorage.DefaultDataStorage;
import org.junit.jupiter.api.Test;

class DefaultDataStorageTest extends AbstractTest {
    private final DataStorage sut = new DefaultDataStorage(TEST_DATASOURCE_1);
    private final DataStorage sut = new DefaultDataStorage(TEST_DATASOURCE_2);

    @Test
    void test() {

    }
}
