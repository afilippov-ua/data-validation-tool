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

package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.model.datasource.ColumnData;
import com.filippov.data.validation.tool.model.datasource.DataType;
import com.filippov.data.validation.tool.model.datasource.DatasourceColumn;
import com.filippov.data.validation.tool.repository.redis.model.RedisColumnData;
import com.filippov.data.validation.tool.repository.redis.repository.RedisColumnDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import static java.util.Arrays.asList;

//@Component
@RequiredArgsConstructor
public class TestCompo {
//    private final RedisColumnDataRepository redisColumnDataRepository;
//
//    @PostConstruct
//    public void init() {
//        final RedisColumnData columnData = new RedisColumnData(null, ColumnData.builder()
//                .keyColumn(DatasourceColumn.builder()
//                        .tableName("table")
//                        .name("keyColumn")
//                        .dataType(DataType.INTEGER)
//                        .build())
//                .dataColumn(DatasourceColumn.builder()
//                        .tableName("table")
//                        .name("valueColumn")
//                        .dataType(DataType.STRING)
//                        .build())
//                .keys(asList(1, 2, 3))
//                .data(asList("str1", "str2", "str3"))
//                .build());
//        final RedisColumnData result = redisColumnDataRepository.save(columnData);
//        System.out.println(result);
//    }
}
