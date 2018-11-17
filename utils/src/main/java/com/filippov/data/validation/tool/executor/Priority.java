package com.filippov.data.validation.tool.executor;

import lombok.Getter;

public enum Priority {
    LOW(1), HIGH(10);

    @Getter
    private int intValue;

    Priority(int intValue) {
        this.intValue = intValue;
    }
}
