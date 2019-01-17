package com.filippov.data.validation.tool.storage.mapper;

import com.filippov.data.validation.tool.datasource.Datasource;
import com.filippov.data.validation.tool.datasource.EmptyDatasource;
import com.filippov.data.validation.tool.storage.dto.DatasourceDto;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import org.bson.Document;

public class MongoDtoBsonMapper {
    private static final String PAIR = "pair";
    private static final String LEFT = "left";
    private static final String RIGHT = "right";
    private static final String DATASOURCE_CLASS = "datasourceClass";
    private static final String CONNECTION_STRING = "connectionString";

    public DatasourceDto toDto(Datasource datasource) {
        return DatasourceDto.builder()
                .datasourceClass(datasource.getClass().getSimpleName())
                .connectionString(datasource.getConnectionString())
                .build();
    }

    public Datasource fromDto(DatasourceDto datasourceDto) {
        if (datasourceDto.getDatasourceClass().equals(EmptyDatasource.class.getSimpleName())) {
            return new EmptyDatasource(datasourceDto.getConnectionString());
        } else {
            throw new IllegalArgumentException("Unsupported datasource class: " + datasourceDto.getDatasourceClass());
        }
    }

    public Document toBson(DatasourceDto datasourceDto) {
        final Document document = new Document();
        document.put(DATASOURCE_CLASS, datasourceDto.getDatasourceClass());
        document.put(CONNECTION_STRING, datasourceDto.getConnectionString());
        return document;
    }

    public Document toBson(DatasourcePairDto datasourcePairDto) {
        final Document document = new Document();
        document.put(LEFT, toBson(datasourcePairDto.getLeft()));
        document.put(RIGHT, toBson(datasourcePairDto.getRight()));
        return document;
    }

    public DatasourceDto toDatasourceDto(Document doc) {
        return DatasourceDto.builder()
                .datasourceClass((String) doc.get(DATASOURCE_CLASS))
                .connectionString((String) doc.get(CONNECTION_STRING))
                .build();
    }

    public DatasourcePairDto toDatasourcePairDto(Document doc) {
        return DatasourcePairDto.builder()
                .left(toDatasourceDto((Document) ((Document) doc.get(PAIR)).get(LEFT)))
                .right(toDatasourceDto((Document) ((Document) doc.get(PAIR)).get(RIGHT)))
                .build();
    }
}
