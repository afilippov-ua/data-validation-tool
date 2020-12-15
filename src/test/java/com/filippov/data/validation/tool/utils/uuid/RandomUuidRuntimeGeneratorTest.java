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

package com.filippov.data.validation.tool.utils.uuid;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class RandomUuidRuntimeGeneratorTest {

    @Test
    void generateRandomUuidSimpleTest() {
        final RandomUuidRuntimeGenerator uuidGenerator = new RandomUuidRuntimeGenerator();
        final String first = uuidGenerator.generateRandomUuid();
        final String second = uuidGenerator.generateRandomUuid();
        final String third = uuidGenerator.generateRandomUuid();

        assertThat(first).isNotNull().isNotEmpty();
        assertThat(second).isNotNull().isNotEmpty();
        assertThat(third).isNotNull().isNotEmpty();

        assertThat(first).isNotEqualTo(second);
        assertThat(first).isNotEqualTo(third);
        assertThat(second).isNotEqualTo(third);
    }
}
