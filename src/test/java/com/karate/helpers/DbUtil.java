package com.karate.helpers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Sorts;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.client.result.DeleteResult;

import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.*;

public class DbUtils {

    private static final Logger logger = LoggerFactory.getLogger(DbUtils.class);

    private MongoClient mongoClient;

    //public DbUtils(Map<String, Object> config)
    public DbUtils(String conString) {

        //String host = (String) config.get("host");
        //int port = (int) config.get("port");

        //mongoClient = new MongoClient(host, port);
        //ConnectionString connectionString = new ConnectionString(conString);
        //MongoClient mongoClient = MongoClients.create(connectionString);

        // MongoClient mongoClient = new MongoClient(String.valueOf(connectionString));

        mongoClient = MongoClients.create(conString);
    }

    public void insertValues(String database, String collection, List<Object> query) {

        List<Document> documents = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            for (Object object : query) {
                String documentString = objectMapper.writeValueAsString(object);
                Document doc = Document.parse(documentString);
                documents.add(doc);
            }

        } catch (IOException e) {
            logger.error("Error in parsing document for collection");
        }

        mongoClient.getDatabase(database).getCollection(collection).insertMany(documents);

    }

    public void deleteDocuments(String database, String collection, String field, String pattern) {
        for (int i = 0; i < 1000; i++) {
            try {
                // Create a regex pattern that starts with the given pattern
                Pattern regexPattern = Pattern.compile("^" + pattern, Pattern.CASE_INSENSITIVE);

                // Create the query using regex
                Bson query = Filters.regex(field, regexPattern);

                // Execute delete operation
                DeleteResult result = mongoClient.getDatabase(database)
                        .getCollection(collection)
                        .deleteMany(query);

                // If no documents were deleted, break the loop
                if (result.getDeletedCount() == 0) {
                    logger.info("No more documents to delete. Breaking loop.");
                    break;
                }

                logger.info("Deleted " + result.getDeletedCount() + " documents in iteration " + (i + 1));
                Thread.sleep(10000);
            } catch (Exception e) {
                logger.error("Error in deleting documents for collection: " + e.getMessage());
            }
        }
    }

    public void deleteAllDocuments(String database, String collection) {
        try {
            // Create an empty query to match all documents
            Bson query = Filters.empty();

            // Execute delete operation to delete all documents
            DeleteResult result = mongoClient.getDatabase(database)
                    .getCollection(collection)
                    .deleteMany(query);

            logger.info("Deleted " + result.getDeletedCount() + " documents from collection " + collection);
        } catch (Exception e) {
            logger.error("Error in deleting all documents from collection: " + e.getMessage());
        }
    }


    public String getId(String database, String collection, String field, String value) {
        FindIterable<Document> documents = mongoClient.getDatabase(database)
                .getCollection(collection).find(eq(field, value));

        String id = "";

        for (Document document : documents) {
            id = ((ObjectId) document.get("_id")).toString();

        }

        return id;
    }

    public void disconnect() {
        mongoClient.close();
    }

    public Document getDocumentFiltered(String database, String collection, String field, String value) {
        // this finds all documents in a given collection (note: no parameter supplied to the find() call)
        // and for each document it projects on Project_Information.Project_Description
        MongoDatabase database_mongo = mongoClient.getDatabase(database);
        MongoCollection<Document> collection_mongo = database_mongo.getCollection(collection);
        Bson projectionFields = Projections.fields(
                Projections.include("decisionFlowType", "policiesApplied"),
                Projections.excludeId());
        LocalDateTime now = LocalDateTime.now().minusMinutes(10);
        Bson filter = eq(field, value);
        Document doc = collection_mongo.find(Filters.and(gt("createdDate", now), eq(field, value)))
                .sort(Sorts.descending("responseDateTs"))
                .first();
        return doc;
        /*Document doc = collection_mongo.find(eq("createdDate", LocalDate.now()))
                .projection(projectionFields)
                .sort(Sorts.descending("responseDateTs"))
                .first();
        return doc;*/
    }

    public Document getResponseDocument(String database, String collection, String field, String value) {
        // this finds all documents in a given collection (note: no parameter supplied to the find() call)
        // and for each document it projects on Project_Information.Project_Description
        MongoDatabase database_mongo = mongoClient.getDatabase(database);
        MongoCollection<Document> collection_mongo = database_mongo.getCollection(collection);
        Bson projectionFields = Projections.fields(
                Projections.include("decisionFlowType", "policiesApplied"),
                Projections.excludeId());
        LocalDateTime now = LocalDateTime.now().minusMinutes(5);

//        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
//        String isoDate = now.format(formatter);
        System.out.println("Date sent ==> " + now);
//        Bson filter = Filters.and(Filters.gt("createdDate", now));
        Bson filter = eq(field, value);
        Document doc = collection_mongo.find(Filters.and(gt("createdDate", now), eq(field, value)))
                .sort(Sorts.descending("responseDateTs"))
                .first();
        return doc;
    }

    public Document getRequestDocument(String database, String collection, String field, String value) {
        // this finds all documents in a given collection (note: no parameter supplied to the find() call)
        // and for each document it projects on Project_Information.Project_Description
        MongoDatabase database_mongo = mongoClient.getDatabase(database);
        MongoCollection<Document> collection_mongo = database_mongo.getCollection(collection);
        /*Bson projectionFields = Projections.fields(
                Projections.include("decisionFlowType", "policiesApplied"),
                Projections.excludeId());*/
        Document doc = collection_mongo.find(eq(field, value))
                //.projection(projectionFields)
                //.sort(Sorts.descending("responseDateTs"))
                .first();
        return doc;
    }

}
