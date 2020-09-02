package com.filippov.data.validation.tool.controller;

import com.filippov.data.validation.tool.dto.ColumnPairDto;
import com.filippov.data.validation.tool.dto.DtoMapper;
import com.filippov.data.validation.tool.dto.cache.CacheRequestDto;
import com.filippov.data.validation.tool.dto.cache.ColumnPairCacheDetailsDto;
import com.filippov.data.validation.tool.model.CachingStatus;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.TablePair;
import com.filippov.data.validation.tool.repository.DataStoragePairRepository;
import com.filippov.data.validation.tool.service.CacheService;
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
import static java.util.stream.Collectors.toMap;

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
        return cacheService.getTablePairCacheDetails(getWorkspaceByIdOrName(workspaceId), getTablePairByIdOrName(workspaceId, tablePairId))
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
                        getWorkspaceByIdOrName(workspaceId),
                        getTablePairByIdOrName(workspaceId, tablePairId),
                        getColumnPairByIdOrName(workspaceId, tablePairId, columnPairId)));
    }

    @PostMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache")
    public CachingStatus processCachingCommand(@PathVariable("workspaceId") String workspaceId,
                                               @PathVariable("tablePairId") String tablePairId,
                                               @RequestBody CacheRequestDto cacheRequestDto) {

        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspaceId, tablePairId);
        final List<CachingStatus> result = metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair)
                .stream()
                .map(columnPair ->
                        cacheService.processCachingCommand(
                                workspace,
                                tablePair,
                                columnPair,
                                cacheRequestDto.getCacheFetchingCommand()))
                .collect(toList());

        return result.size() > 0 ? result.get(0) : CachingStatus.NON_DEFINED;
    }

    @PostMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache")
    public CachingStatus processCachingCommand(@PathVariable("workspaceId") String workspaceId,
                                               @PathVariable("tablePairId") String tablePairId,
                                               @PathVariable("columnPairId") String columnPairId,
                                               @RequestBody CacheRequestDto cacheRequestDto) {
        return cacheService.processCachingCommand(
                getWorkspaceByIdOrName(workspaceId),
                getTablePairByIdOrName(workspaceId, tablePairId),
                getColumnPairByIdOrName(workspaceId, tablePairId, columnPairId),
                cacheRequestDto.getCacheFetchingCommand());
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, CachingStatus> getTablePairCacheStatus(@PathVariable("workspaceId") String workspaceId,
                                                                     @PathVariable("tablePairId") String tablePairId) {
        return cacheService.getTablePairCacheStatus(
                getWorkspaceByIdOrName(workspaceId),
                getTablePairByIdOrName(workspaceId, tablePairId))
                .entrySet()
                .stream()
                .collect(toMap(
                        entry -> entry.getKey().getName(),
                        Map.Entry::getValue));
    }

    @GetMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache/status", produces = MediaType.APPLICATION_JSON_VALUE)
    public CachingStatus getColumnPairCacheStatus(@PathVariable("workspaceId") String workspaceId,
                                                  @PathVariable("tablePairId") String tablePairId,
                                                  @PathVariable("columnPairId") String columnPairId) {
        return cacheService.getColumnPairCacheStatus(
                getWorkspaceByIdOrName(workspaceId),
                getTablePairByIdOrName(workspaceId, tablePairId),
                getColumnPairByIdOrName(workspaceId, tablePairId, columnPairId));
    }

    @DeleteMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/cache")
    public void deleteTableCache(@PathVariable("workspaceId") String workspaceId,
                                 @PathVariable("tablePairId") String tablePairId) {
        final Workspace workspace = getWorkspaceByIdOrName(workspaceId);
        final TablePair tablePair = getTablePairByIdOrName(workspaceId, tablePairId);
        metadataService.getMetadata(workspace)
                .getColumnPairs(tablePair)
                .forEach(columnPair -> cacheService.deleteCacheForColumnPair(workspace, tablePair, columnPair));
    }

    @DeleteMapping(path = "/{workspaceId}/tablePairs/{tablePairId}/columnPairs/{columnPairId}/cache")
    public void deleteColumnCache(@PathVariable("workspaceId") String workspaceId,
                                  @PathVariable("tablePairId") String tablePairId,
                                  @PathVariable("columnPairId") String columnPairId) {
        cacheService.deleteCacheForColumnPair(
                getWorkspaceByIdOrName(workspaceId),
                getTablePairByIdOrName(workspaceId, tablePairId),
                getColumnPairByIdOrName(workspaceId, tablePairId, columnPairId));
    }
}
