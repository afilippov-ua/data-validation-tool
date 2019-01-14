package com.filippov.data.validation.tool;

import com.filippov.data.validation.tool.repository.StoragePairRepository;

public class Context {
    private final StoragePairRepository storagePairRepository;

    public Context(StoragePairRepository storagePairRepository) {
        this.storagePairRepository = storagePairRepository;
    }
}
