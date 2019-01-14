package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;

import java.util.List;
import java.util.Optional;

public interface ApplicationStorage {

    String putDatasourcePair(DatasourcePairDto datasourcePairDto);

    List<DatasourcePairDto> getDatasourcePairs();

    Optional<DatasourcePairDto> getDatasourcePair(String id);

    void deleteDatasourcePair(String id);
}
