package com.filippov.data.validation.tool.metadata;

import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class Metadata {
    private final Map<String, TablePair> tablePairsById;
    private final Map<String, TablePair> tablePairsByName;
    private final Map<TablePair, Map<String, ColumnPair>> columnPairsById;
    private final Map<TablePair, Map<String, ColumnPair>> columnPairsByName;

    @Builder
    public Metadata(List<TablePair> tablePairs, List<ColumnPair> columnPairs) {
        this.tablePairsById = tablePairs.stream().collect(toMap(
                TablePair::getId,
                Function.identity()));

        this.tablePairsByName = tablePairs.stream().collect(toMap(
                TablePair::getName,
                Function.identity()));

        this.columnPairsById = tablePairs.stream()
                .collect(toMap(
                        Function.identity(),
                        tablePair -> columnPairs.stream()
                                .filter(columnPair -> columnPair.getTablePair().equals(tablePair))
                                .collect(toMap(
                                        ColumnPair::getId,
                                        Function.identity()))));

        this.columnPairsByName = tablePairs.stream()
                .collect(toMap(
                        Function.identity(),
                        tablePair -> columnPairs.stream()
                                .filter(columnPair -> columnPair.getTablePair().equals(tablePair))
                                .collect(toMap(
                                        ColumnPair::getName,
                                        Function.identity()))));

    }

    public List<TablePair> getTablePairs() {
        return new ArrayList<>(tablePairsById.values());
    }

    public Optional<TablePair> getTablePairById(String tablePairId) {
        return Optional.ofNullable(tablePairsById.get(tablePairId));
    }

    public Optional<TablePair> getTablePairByName(String tablePairName) {
        return Optional.ofNullable(tablePairsByName.get(tablePairName));
    }

    public Optional<TablePair> getTablePairByIdOrName(String tablePair) {
        return Optional.ofNullable(
                Optional.ofNullable(tablePairsById.get(tablePair))
                        .orElseGet(() -> tablePairsByName.get(tablePair)));
    }

    public List<ColumnPair> getColumnPairs(TablePair tablePair) {
        return new ArrayList<>(columnPairsById.get(tablePair).values());
    }

    public Optional<ColumnPair> getColumnPairById(TablePair tablePair, String columnPairId) {
        return Optional.ofNullable(columnPairsById.get(tablePair))
                .map(map -> map.get(columnPairId));
    }

    public Optional<ColumnPair> getColumnPairByName(TablePair tablePair, String columnPairName) {
        return Optional.ofNullable(columnPairsByName.get(tablePair))
                .map(map -> map.get(columnPairName));
    }

    public Optional<ColumnPair> getColumnPairByIdOrName(TablePair tablePair, String columnPair) {
        return Optional.ofNullable(
                Optional.ofNullable(columnPairsById.get(tablePair))
                        .map(map -> map.get(columnPair))
                        .orElseGet(() ->
                                Optional.ofNullable(columnPairsByName.get(tablePair))
                                        .map(map -> map.get(columnPair))
                                        .orElse(null)));

    }
}
