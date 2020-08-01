package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.cache.CacheRequestDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.model.CacheStatus;
import com.filippov.data.validation.tool.service.CacheService;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.MetadataService;
import com.filippov.data.validation.tool.service.WorkspaceService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping("workspaces")
public class CacheController extends AbstractController {

    private final DtoMapper dtoMapper;
    private final CacheService cacheService;

    public CacheController(WorkspaceService workspaceService, MetadataService metadataService, DataStoragePairRepository dataStoragePairRepository, CacheService cacheService,
                           DtoMapper dtoMapper) {
        super(workspaceService, metadataService, dataStoragePairRepository);
        this.cacheService = cacheService;
        this.dtoMapper = dtoMapper;
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cacheDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ColumnPairCacheDetailsDto> getTablePairCacheDetails(@PathVariable("workspaceId") String workspaceId,
                                                                    @PathVariable("tablePairId") String tablePairId) {
        return cacheService.getTablePairCacheDetails(getWorkspace(workspaceId), getTablePair(workspaceId, tablePairId))
                .stream()
                .map(dtoMapper::toDto)
                .collect(toList());
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cacheDetails", produces = MediaType.APPLICATION_JSON_VALUE)
    public ColumnPairCacheDetailsDto getColumnPairCacheDetails(@PathVariable("workspaceId") String workspaceId,
                                                               @PathVariable("tablePairId") String tablePairId,
                                                               @PathVariable("columnPairId") String columnPairId) {
        return dtoMapper.toDto(
                cacheService.getColumnPairCacheDetails(
                        getWorkspace(workspaceId),
                        getTablePair(workspaceId, tablePairId),
                        getColumnPair(workspaceId, tablePairId, columnPairId)));
    }

    @PostMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache")
    public CacheStatus processCachingCommand(@PathVariable("workspaceId") String workspaceId,
                                             @PathVariable("tablePairId") String tablePairId,
                                             @PathVariable("columnPairId") String columnPairId,
                                             @RequestBody CacheRequestDto cacheRequestDto) {
        return cacheService.processCachingCommand(
                getWorkspace(workspaceId),
                getTablePair(workspaceId, tablePairId),
                getColumnPair(workspaceId, tablePairId, columnPairId),
                cacheRequestDto.getCacheFetchingCommand());
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, CacheStatus> getTablePairCacheStatus(@PathVariable("workspaceId") String workspaceId,
                                                            @PathVariable("tablePairId") String tablePairId) {
        return cacheService.getTablePairCacheStatus(
                getWorkspace(workspaceId),
                getTablePair(workspaceId, tablePairId));
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CacheStatus getColumnPairCacheStatus(@PathVariable("workspaceId") String workspaceId,
                                                @PathVariable("tablePairId") String tablePairId,
                                                @PathVariable("columnPairId") String columnPairId) {
        return cacheService.getColumnPairCacheStatus(
                getWorkspace(workspaceId),
                getTablePair(workspaceId, tablePairId),
                getColumnPair(workspaceId, tablePairId, columnPairId));
    }

    @DeleteMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache")
    public boolean deleteCache(@PathVariable("workspaceId") String workspaceId,
                               @PathVariable("tablePairId") String tablePairId,
                               @PathVariable("columnPairId") String columnPairId) {
        return cacheService.deleteCacheForColumnPair(
                getWorkspace(workspaceId),
                getTablePair(workspaceId, tablePairId),
                getColumnPair(workspaceId, tablePairId, columnPairId));
    }
}
