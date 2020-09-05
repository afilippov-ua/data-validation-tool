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

package com.filippov.data.validation.tool.storage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ApplicationStorageTest {
//    private final List<String> createdIds = new ArrayList<>();
//
//    @Autowired
//    private ApplicationStorage applicationStorage;
//    @Autowired
//    private DtoMapper dtoMapper;
//
//
//    @Test
//    public void getByIncorrectIdFormatTest() {
//        assertThrows(IllegalArgumentException.class,
//                () -> applicationStorage.getDatasourcePair("incorrect object id"));
//    }
//
//    @Test
//    public void getByNonExistentIdTest() {
//        assertThat(applicationStorage.getDatasourcePair("5c40e54f9a234a2f0f65e32d")).isEmpty();
//    }
//
//    @Test
//    public void getDatasourcePairByNullIdTest() {
//        assertThrows(IllegalArgumentException.class,
//                () -> applicationStorage.getDatasourcePair(null));
//    }
//
//    @Test
//    public void putNullDatasourcePairTest() {
//        assertThrows(IllegalArgumentException.class,
//                () -> applicationStorage.putDatasourcePair(null));
//    }
//
//    @Test
//    public void deleteDatasourcePairByNullIdTest() {
//        assertThrows(IllegalArgumentException.class,
//                () -> applicationStorage.deleteDatasourcePair(null));
//    }
//
//    @Test
//    public void saveAndLoadDataStoragePairTest() {
//        final DatasourcePairDto datasourcePairDto = newDatasourcePairDto();
//        final String id = applicationStorage.putDatasourcePair(datasourcePairDto);
//        assertThat(id).isNotNull().isNotEmpty();
//
//        createdIds.add(id);
//
//        final Optional<DatasourcePairDto> datasourcePair = applicationStorage.getDatasourcePair(id);
//        assertThat(datasourcePair).isNotEmpty().hasValue(datasourcePairDto);
//    }
//
//    @Test
//    public void saveAndDeleteDataStoragePairTest() {
//        final DatasourcePairDto datasourcePairDto = newDatasourcePairDto();
//        final String id = applicationStorage.putDatasourcePair(datasourcePairDto);
//        assertThat(id).isNotNull().isNotEmpty();
//
//        createdIds.add(id);
//
//        final Optional<DatasourcePairDto> datasourcePair = applicationStorage.getDatasourcePair(id);
//        assertThat(datasourcePair).isNotEmpty().hasValue(datasourcePairDto);
//
//        applicationStorage.deleteDatasourcePair(id);
//
//        final Optional<DatasourcePairDto> emptyResult = applicationStorage.getDatasourcePair(id);
//        assertThat(emptyResult).isEmpty();
//    }
//
//    @AfterEach
//    public void cleanUp() {
//        createdIds.forEach(applicationStorage::deleteDatasourcePair);
//    }
//
//    private DataStoragePair newDataStoragePair() {
//        return DataStoragePair.builder()
//                .left(new DefaultDataStorage(LEFT, new EmptyDatasource("localhost:10001"), new InMemoryColumnDataCache(), 1))
//                .right(new DefaultDataStorage(RIGHT, new EmptyDatasource("localhost:10002"), new InMemoryColumnDataCache(), 1))
//                .build();
//    }
//
//    private DatasourcePairDto newDatasourcePairDto() {
//        final DataStoragePair dataStoragePair = newDataStoragePair();
//        return DatasourcePairDto.builder()
//                .left(dtoMapper.toDto(dataStoragePair.getLeft().getDatasource()))
//                .right(dtoMapper.toDto(dataStoragePair.getRight().getDatasource()))
//                .build();
//    }
}
