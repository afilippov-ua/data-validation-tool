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

package com.filippov.data.validation.tool.datastorage;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class DataStorageConfig {
    private final RelationType relationType;
    private final Integer maxConnections;

    DataStorageConfig(RelationType relationType, Integer maxConnections) {
        if (relationType == null) {
            throw new IllegalArgumentException("Incorrect input: relation type is null");
        }
        if (maxConnections == null) {
            throw new IllegalArgumentException("Incorrect input: maxConnections is null");
        }
        if (maxConnections <= 0) {
            throw new IllegalArgumentException("Incorrect input: maxConnections is less or equal zero");
        }
        this.relationType = relationType;
        this.maxConnections = maxConnections;
    }
}
