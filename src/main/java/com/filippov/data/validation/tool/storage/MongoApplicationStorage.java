package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.MongoBsonMapper;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Optional;

@Slf4j
public class MongoApplicationStorage implements ApplicationStorage {
    private static final String DATA_STORAGE_PAIR_COLLECTION_NAME = "dataStoragePairs";
    private static final String PAIR = "pair";
    private static final String OBJECT_ID = "_id";

    private final MongoDatabase db;
    private final MongoBsonMapper mapper;

    public MongoApplicationStorage(MongoDatabase db, MongoBsonMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public String putDatasourcePair(DatasourcePairDto datasourcePairDto) {
        if (datasourcePairDto == null) {
            throw new IllegalArgumentException("Datasource pair is null");
        }

        Timer timer = Timer.start();
        final Document document = new Document();
        document.put(PAIR, mapper.toBson(datasourcePairDto));
        final String id = save(document, DATA_STORAGE_PAIR_COLLECTION_NAME);
        log.info("Data storage pair was added to mongo storage (id: '{}'). Execution time: {}", id, timer.stop());
        return id;
    }

    @Override
    public List<DatasourcePairDto> getDatasourcePairs() {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO
    }

    @Override
    public Optional<DatasourcePairDto> getDatasourcePair(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Datasource pair ID is null");
        }

        Timer timer = Timer.start();
        final Document filter = new Document();
        filter.put(OBJECT_ID, new ObjectId(id));
        final Optional<DatasourcePairDto> result =
                findOne(filter, DATA_STORAGE_PAIR_COLLECTION_NAME)
                        .map(mapper::toDatasourcePairDto);
        log.info("Data storage pair with id: '{}' was loaded from mongo storage. Execution time: {}", id, timer.stop());
        return result;
    }

    @Override
    public void deleteDatasourcePair(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Datasource pair ID is null");
        }

        Timer timer = Timer.start();
        final Document filter = new Document();
        filter.put(OBJECT_ID, new ObjectId(id));
        delete(filter, DATA_STORAGE_PAIR_COLLECTION_NAME);
        log.info("Data storage pair by id: '{}' was deleted from mongo storage. Execution time: {}", id, timer.stop());
    }

    private Optional<Document> findOne(Document filter, String collectionName) {
        final MongoCollection<Document> collection = db.getCollection(collectionName);
        return Optional.ofNullable(collection.find(filter).first());
    }

    private String save(Document document, String collectionName) {
        final MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(document);
        return document.get(OBJECT_ID).toString();
    }

    private void delete(Document filter, String collectionName) {
        final MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.deleteOne(filter);
    }
}
