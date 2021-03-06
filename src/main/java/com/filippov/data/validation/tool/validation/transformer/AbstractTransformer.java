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

package com.filippov.data.validation.tool.validation.transformer;

import com.filippov.data.validation.tool.model.Transformer;

public abstract class AbstractTransformer<Input, Output> implements Transformer<Input, Output> {
    private Transformer<Output, ?> next;

    public AbstractTransformer() {
    }

    @Override
    public Transformer<Output, ?> getNext() {
        return next;
    }

    @Override
    public void setNext(Transformer<Output, ?> nextTransformer) {
        this.next = nextTransformer;
    }

    @Override
    public Transformer<?, ?> getLastTransformer() {
        if (next == null) {
            return this;
        } else {
            return next.getLastTransformer();
        }
    }
}
