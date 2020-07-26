package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class Metadata {
    private final Map<String, TablePair> tablePairsById;
    private final Map<String, TablePair> tablePairsByName; // TODO: Do we really need it?
    private final Map<TablePair, Map<String, ColumnPair>> columnPairs;

    @Builder
    public Metadata(List<TablePair> tablePairs, List<ColumnPair> columnPairs) {
        this.tablePairsById = tablePairs.stream().collect(toMap(
                TablePair::getId,
                Function.identity()));

        this.tablePairsByName = tablePairs.stream().collect(toMap(
                TablePair::getName,
                Function.identity()));

        this.columnPairs = new HashMap<>();
        for (TablePair tablePair : this.tablePairsById.values()) {
            this.columnPairs.put(tablePair, columnPairs.stream()
                    .filter(columnPair -> columnPair.getTablePair().getId().equals(tablePair.getId()))
                    .collect(toMap(
                            ColumnPair::getName,
                            Function.identity())));
        }
    }

    public List<TablePair> getTablePairsById() {
        return new ArrayList<>(tablePairsById.values());
    }

    public Optional<TablePair> getTablePairById(String tablePairId) {
        return Optional.of(tablePairsById.get(tablePairId));
    }

    public Optional<TablePair> getTablePairByName(String tablePairName) {
        return Optional.of(tablePairsByName.get(tablePairName));
    }

    public List<ColumnPair> getColumnPairs(TablePair tablePair) {
        return new ArrayList<>(columnPairs.get(tablePair).values());
    }

    public Optional<ColumnPair> getColumnPair(TablePair tablePair, String columnPairName) {
        return Optional.of(columnPairs.get(tablePair))
                .map(map -> map.get(columnPairName));
    }
}
