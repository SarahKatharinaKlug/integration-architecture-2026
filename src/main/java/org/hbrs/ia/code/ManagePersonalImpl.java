package org.hbrs.ia.code;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.IndexOptions;
import com.mongodb.client.model.Indexes;
import org.bson.Document;
import org.hbrs.ia.model.SalesMan;
import org.hbrs.ia.model.SocialPerformanceRecord;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

/**
 * MongoDB implementation of {@link ManagePersonal} using the plain driver.
 *
 * Schema (two collections):
 *   salesmen:           { firstname, lastname, sid }            -- sid is unique
 *   socialperformance:  { sid, goalDescription, targetValue, actualValue, year }
 * A performance record references its salesman by sid. Records are kept in a
 * separate collection so that a salesman can collect many records over the
 * years without his document growing, and so that records can be queried on
 * their own (e.g. all records of one year).
 */
public class ManagePersonalImpl implements ManagePersonal {

    static final String SALESMEN = "salesmen";
    static final String PERFORMANCE = "socialperformance";

    private final MongoCollection<Document> salesmen;
    private final MongoCollection<Document> performance;

    /** The database is injected, so tests can use a throw-away database. */
    public ManagePersonalImpl(MongoDatabase database) {
        this.salesmen = database.getCollection(SALESMEN);
        this.performance = database.getCollection(PERFORMANCE);
        this.salesmen.createIndex(Indexes.ascending("sid"), new IndexOptions().unique(true));
        this.performance.createIndex(Indexes.ascending("sid", "year"));
    }

    /** Convenience factory for a local MongoDB on the standard port. */
    public static ManagePersonalImpl connectToLocalhost(MongoClient client, String databaseName) {
        return new ManagePersonalImpl(client.getDatabase(databaseName));
    }

    @Override
    public void createSalesMan(SalesMan salesMan) {
        salesmen.insertOne(salesMan.toDocument());
    }

    @Override
    public void addSocialPerformanceRecord(SocialPerformanceRecord record, SalesMan salesMan) {
        if (readSalesMan(salesMan.getId()) == null) {
            throw new IllegalArgumentException("Unknown salesman with sid " + salesMan.getId());
        }
        performance.insertOne(record.toDocument(salesMan.getId()));
    }

    @Override
    public SalesMan readSalesMan(int sid) {
        Document document = salesmen.find(eq("sid", sid)).first();
        return document == null ? null : SalesMan.fromDocument(document);
    }

    @Override
    public List<SalesMan> readAllSalesMen() {
        List<SalesMan> result = new ArrayList<>();
        for (Document document : salesmen.find()) {
            result.add(SalesMan.fromDocument(document));
        }
        return result;
    }

    @Override
    public List<SocialPerformanceRecord> readSocialPerformanceRecord(SalesMan salesMan) {
        return toRecords(performance.find(eq("sid", salesMan.getId())));
    }

    @Override
    public List<SocialPerformanceRecord> readSocialPerformanceRecord(SalesMan salesMan, int year) {
        return toRecords(performance.find(and(eq("sid", salesMan.getId()), eq("year", year))));
    }

    @Override
    public void deleteSalesMan(int sid) {
        performance.deleteMany(eq("sid", sid));
        salesmen.deleteOne(eq("sid", sid));
    }

    @Override
    public void deleteSocialPerformanceRecords(SalesMan salesMan) {
        performance.deleteMany(eq("sid", salesMan.getId()));
    }

    private List<SocialPerformanceRecord> toRecords(Iterable<Document> documents) {
        List<SocialPerformanceRecord> result = new ArrayList<>();
        for (Document document : documents) {
            result.add(SocialPerformanceRecord.fromDocument(document));
        }
        return result;
    }
}
