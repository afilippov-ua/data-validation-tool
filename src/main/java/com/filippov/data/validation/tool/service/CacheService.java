package com.filippov.data.validation.tool.service;

import com.filippov.data.validation.tool.cache.ColumnDataCache;
import com.filippov.data.validation.tool.model.CacheFetchingCommand;
import com.filippov.data.validation.tool.model.CacheStatus;
import com.filippov.data.validation.tool.model.ColumnPairCacheDetails;
import com.filippov.data.validation.tool.model.Workspace;
import com.filippov.data.validation.tool.pair.ColumnPair;
import com.filippov.data.validation.tool.pair.TablePair;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class CacheService {
    private ColumnDataCache columnDataCache;

    public List<ColumnPairCacheDetails> getTablePairCacheDetails(Workspace workspace, TablePair tablePair) {
        throw new RuntimeException("Not implemented yet");
    }

    public ColumnPairCacheDetails getColumnPairCacheDetails(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        throw new RuntimeException("Not implemented yet");
    }

    public boolean deleteCacheForColumnPair(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        throw new RuntimeException("Not implemented yet");
    }

    public CacheStatus processCachingCommand(Workspace workspace, TablePair tablePair, ColumnPair columnPair, CacheFetchingCommand cacheFetchingCommand) {
        switch (cacheFetchingCommand) {
            case START:
                return startCaching(workspace, tablePair, columnPair);
            case STOP:
                return stopCaching(workspace, tablePair, columnPair);
            default:
                throw new UnsupportedOperationException("Unsupported cache fetching command: " + cacheFetchingCommand);
        }
    }

    public Map<String, CacheStatus> getTablePairCacheStatus(Workspace workspace, TablePair tablePair) {
        throw new RuntimeException("Not implemented yet");
    }

    public CacheStatus getColumnPairCacheStatus(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        throw new RuntimeException("Not implemented yet");
    }

    public CacheStatus startCaching(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        throw new RuntimeException("Not implemented yet");
    }

    public CacheStatus stopCaching(Workspace workspace, TablePair tablePair, ColumnPair columnPair) {
        throw new RuntimeException("Not implemented yet");
    }
}
