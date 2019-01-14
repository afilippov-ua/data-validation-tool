package com.filippov.data.validation.tool.repository;

import com.filippov.data.validation.tool.pair.DataStoragePair;

import java.util.List;
import java.util.Optional;

public interface StoragePairRepository {

    List<DataStoragePair> get();

    Optional<DataStoragePair> get(String id);

    String put(DataStoragePair dataStoragePair);

    void delete(String id);
}
