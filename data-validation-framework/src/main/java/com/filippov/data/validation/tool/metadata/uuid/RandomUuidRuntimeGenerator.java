package com.filippov.data.validation.tool.metadata.uuid;

import java.util.UUID;

public class RandomUuidRuntimeGenerator implements UuidGenerator {

    @Override
    public String generateRandomUuid() {
        return UUID.randomUUID().toString();
    }
}
