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

package com.filippov.data.validation.tool.model;

import com.filippov.data.validation.tool.model.datasource.DataType;

public interface Transformer<Input, Output> {
    Output transform(Input value);

    Transformer<Output, ?> getNext();

    void setNext(Transformer<Output, ?> nextTransformer);

    Transformer<?, ?> getLastTransformer();

    DataType getInputDataType();

    DataType getOutputDataType();

}
