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

package com.filippov.data.validation.tool.model.pair;


import com.filippov.data.validation.tool.AbstractUnitTest;
import com.filippov.data.validation.tool.model.DataStorage;
import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.datastorage.Query;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class DataStoragePairTest extends AbstractUnitTest {
    private static final DataStorage LEFT_STORAGE = Mockito.mock(DataStorage.class);
    private static final DataStorage RIGHT_STORAGE = Mockito.mock(DataStorage.class);

    static Object[][] incorrectInputProvider() {
        return new Object[][]{
                {null, RIGHT_STORAGE},
                {LEFT_STORAGE, null}
        };
    }

    @ParameterizedTest
    @MethodSource("incorrectInputProvider")
    void shouldThrowAnExceptionInCaseOfIncorrectInput(DataStorage leftStorage, DataStorage rightStorage) {
        Assertions.assertThatThrownBy(() -> new DataStoragePair(leftStorage, rightStorage))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void dataStoragePairConstructorTest() {
        final DataStoragePair columnDataPair = new DataStoragePair(LEFT_STORAGE, RIGHT_STORAGE);

        assertThat(columnDataPair).isNotNull();
        assertThat(columnDataPair.getLeftDataStorage()).isNotNull().isEqualTo(LEFT_STORAGE);
        assertThat(columnDataPair.getRightDataStorage()).isNotNull().isEqualTo(RIGHT_STORAGE);
    }

    @Test
    void DataStoragePairBuilderTest() {
        final DataStoragePair columnDataPair = DataStoragePair.builder()
                .leftDataStorage(LEFT_STORAGE)
                .rightDataStorage(RIGHT_STORAGE)
                .build();

        assertThat(columnDataPair).isNotNull();
        assertThat(columnDataPair.getLeftDataStorage()).isNotNull().isEqualTo(LEFT_STORAGE);
        assertThat(columnDataPair.getRightDataStorage()).isNotNull().isEqualTo(RIGHT_STORAGE);
    }

    @Test
    void getDataTest() {
        final DataStorage leftDataStorage = Mockito.mock(DataStorage.class);
        final DataStorage rightDataStorage = Mockito.mock(DataStorage.class);

        final Query query = Query.builder()
                .tablePair(USERS_TABLE_PAIR)
                .columnPair(USERS_USERNAME_COLUMN_PAIR)
                .build();

        final ColumnData<Object, Object> leftData = ColumnData.builder()
                .keyColumn(USERS_ID_COLUMN)
                .dataColumn(USERS_USERNAME_COLUMN)
                .keys(asList(1,2,3))
                .data(asList("u1","u2","u3"))
                .build();
        final ColumnData<Object, Object> rightData = ColumnData.builder()
                .keyColumn(USERS_ID_COLUMN)
                .dataColumn(USERS_USERNAME_COLUMN)
                .keys(asList(1,2,3))
                .data(asList("u1","u2","u3"))
                .build();

        when(leftDataStorage.getData(query)).thenReturn(leftData);
        when(rightDataStorage.getData(query)).thenReturn(rightData);

        final DataStoragePair sut = new DataStoragePair(leftDataStorage, rightDataStorage);
        final ColumnDataPair<Integer, String, String> columnData = sut.getColumnData(query);

        assertThat(columnData).isNotNull();
        assertThat(columnData.getLeftColumnData()).isEqualTo(leftData);
        assertThat(columnData.getRightColumnData()).isEqualTo(rightData);
    }
}
