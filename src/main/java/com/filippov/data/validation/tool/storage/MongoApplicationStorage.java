package com.filippov.data.validation.tool.storage;

import com.filippov.data.validation.tool.Timer;
import com.filippov.data.validation.tool.storage.dto.DatasourcePairDto;
import com.filippov.data.validation.tool.storage.mapper.ApplicationStorageBsonMapper;
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
    private final ApplicationStorageBsonMapper mapper;

    public MongoApplicationStorage(MongoDatabase db, ApplicationStorageBsonMapper mapper) {
        this.db = db;
        this.mapper = mapper;
    }

    @Override
    public String putDatasourcePair(DatasourcePairDto datasourcePairDto) {
        final Document document = new Document();
        document.put(PAIR, mapper.toBson(datasourcePairDto));
        return save(document, DATA_STORAGE_PAIR_COLLECTION_NAME);
    }

    @Override
    public List<DatasourcePairDto> getDatasourcePairs() {
        throw new UnsupportedOperationException("Not implemented yet"); // TODO
    }

    @Override
    public Optional<DatasourcePairDto> getDatasourcePair(String id) {
        Timer timer = Timer.start();
        final Document filter = new Document();
        filter.put(OBJECT_ID, new ObjectId(id));
        Optional<Document> doc = findOne(filter, DATA_STORAGE_PAIR_COLLECTION_NAME);
        log.info("Loading of data storage pair from mongo storage was finished. Execution time: {}", timer.stop());
        return doc.map(mapper::toDatasourcePairDto);
    }

    @Override
    public void deleteDatasourcePair(String id) {
        final Document filter = new Document();
        filter.put(OBJECT_ID, id);
        delete(filter, DATA_STORAGE_PAIR_COLLECTION_NAME);
    }

    private Optional<Document> findOne(Document filter, String collectionName) {
        Timer timer = Timer.start();
        final MongoCollection<Document> collection = db.getCollection(collectionName);
        final Document first = collection.find(filter).first();
        log.info("Document (table: {}, column: {}) was removed from mongo storage (collection: {}). Execution time: {}", collectionName, timer.stop());
        return Optional.ofNullable(first);
    }

    private String save(Document document, String collectionName) {
        Timer timer = Timer.start();
        final MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.insertOne(document);
        log.info("New document was added to mongo storage (collection: {}). Execution time: {}", collectionName, timer.stop());
        return document.get(OBJECT_ID).toString();
    }

    private void delete(Document filter, String collectionName) {
        Timer timer = Timer.start();
        final MongoCollection<Document> collection = db.getCollection(collectionName);
        collection.deleteOne(filter);
        log.info("Document (table: {}, column: {}) was removed from mongo storage (collection: {}). Execution time: {}", collectionName, timer.stop());
    }
}
