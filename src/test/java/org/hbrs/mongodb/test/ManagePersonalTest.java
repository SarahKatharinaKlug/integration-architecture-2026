package org.hbrs.mongodb.test;
import java.util.List;

import org.hbrs.ia.code.ManagePersonal;
import org.hbrs.ia.code.ManagePersonalImpl;
import org.hbrs.ia.model.SalesMan;
import org.hbrs.ia.model.SocialPerformanceRecord;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Roundtrip tests for ManagePersonalImpl. Needs a local MongoDB on port 27017.
 * Every test works in a dedicated database that is dropped afterwards;
 * tearDown additionally checks that the collections are empty, so a test
 * that forgets to delete its data fails ("no data waste").
 */
class ManagePersonalTest {

    private static final String TEST_DATABASE = "highperformanceManagePersonalTest";

    private MongoClient client;
    private MongoDatabase database;
    private ManagePersonal manager;

    @BeforeEach
    void setUp() {
        client = new MongoClient("localhost", 27017);
        database = client.getDatabase(TEST_DATABASE);
        database.drop(); // start from a clean state, even after an aborted earlier run
        manager = new ManagePersonalImpl(database);
    }

    @AfterEach
    void tearDown() {
        try {
            assertEquals(0, database.getCollection("salesmen").countDocuments(),
                    "test left salesmen behind");
            assertEquals(0, database.getCollection("socialperformance").countDocuments(),
                    "test left performance records behind");
        } finally {
            database.drop();
            client.close();
        }
    }

    @Test
    void createAndReadSalesMan() {
        SalesMan salesMan = new SalesMan("Sascha", "Alda", 90133);
        manager.createSalesMan(salesMan);

        SalesMan found = manager.readSalesMan(90133);
        assertNotNull(found);
        assertEquals("Sascha", found.getFirstname());
        assertEquals("Alda", found.getLastname());
        assertEquals(90133, found.getId());

        manager.deleteSalesMan(90133);
    }

    @Test
    void readUnknownSalesManReturnsNull() {
        assertNull(manager.readSalesMan(1));
    }

    @Test
    void readAllSalesMen() {
        manager.createSalesMan(new SalesMan("Leslie", "Malton", 1));
        manager.createSalesMan(new SalesMan("Sascha", "Alda", 2));

        List<SalesMan> all = manager.readAllSalesMen();
        assertEquals(2, all.size());

        manager.deleteSalesMan(1);
        manager.deleteSalesMan(2);
        assertTrue(manager.readAllSalesMen().isEmpty());
    }

    @Test
    void sidMustBeUnique() {
        manager.createSalesMan(new SalesMan("Leslie", "Malton", 1));
        assertThrows(com.mongodb.MongoWriteException.class,
                () -> manager.createSalesMan(new SalesMan("Other", "Person", 1)));

        manager.deleteSalesMan(1);
    }

    @Test
    void addAndReadPerformanceRecordsWithYear() {
        SalesMan salesMan = new SalesMan("Leslie", "Malton", 90444);
        manager.createSalesMan(salesMan);

        manager.addSocialPerformanceRecord(
                new SocialPerformanceRecord("Leadership Competence", 4, 5, 2025), salesMan);
        manager.addSocialPerformanceRecord(
                new SocialPerformanceRecord("Openness to Employee", 4, 3, 2026), salesMan);
        manager.addSocialPerformanceRecord(
                new SocialPerformanceRecord("Social Behaviour", 5, 5, 2026), salesMan);

        assertEquals(3, manager.readSocialPerformanceRecord(salesMan).size());

        List<SocialPerformanceRecord> year2026 = manager.readSocialPerformanceRecord(salesMan, 2026);
        assertEquals(2, year2026.size());
        assertTrue(year2026.stream().allMatch(record -> record.getYear() == 2026));

        assertEquals(1, manager.readSocialPerformanceRecord(salesMan, 2025).size());
        assertTrue(manager.readSocialPerformanceRecord(salesMan, 2020).isEmpty());

        manager.deleteSalesMan(salesMan.getId());
    }

    @Test
    void performanceRecordKeepsAllFields() {
        SalesMan salesMan = new SalesMan("Leslie", "Malton", 90444);
        manager.createSalesMan(salesMan);
        manager.addSocialPerformanceRecord(
                new SocialPerformanceRecord("Communication Skills", 4, 2, 2026), salesMan);

        SocialPerformanceRecord record = manager.readSocialPerformanceRecord(salesMan).get(0);
        assertEquals("Communication Skills", record.getGoalDescription());
        assertEquals(4, record.getTargetValue());
        assertEquals(2, record.getActualValue());
        assertEquals(2026, record.getYear());

        manager.deleteSalesMan(salesMan.getId());
    }

    @Test
    void addRecordToUnknownSalesManFails() {
        SalesMan ghost = new SalesMan("No", "Body", 999);
        assertThrows(IllegalArgumentException.class, () ->
                manager.addSocialPerformanceRecord(
                        new SocialPerformanceRecord("Goal", 1, 1, 2026), ghost));
    }

    @Test
    void deleteRecordsKeepsSalesMan() {
        SalesMan salesMan = new SalesMan("Leslie", "Malton", 90444);
        manager.createSalesMan(salesMan);
        manager.addSocialPerformanceRecord(
                new SocialPerformanceRecord("Goal", 4, 4, 2026), salesMan);

        manager.deleteSocialPerformanceRecords(salesMan);

        assertTrue(manager.readSocialPerformanceRecord(salesMan).isEmpty());
        assertNotNull(manager.readSalesMan(90444));

        manager.deleteSalesMan(90444);
    }

    @Test
    void deleteSalesManAlsoDeletesHisRecordsButNotOthers() {
        SalesMan first = new SalesMan("Leslie", "Malton", 1);
        SalesMan second = new SalesMan("Sascha", "Alda", 2);
        manager.createSalesMan(first);
        manager.createSalesMan(second);
        manager.addSocialPerformanceRecord(new SocialPerformanceRecord("Goal", 4, 4, 2026), first);
        manager.addSocialPerformanceRecord(new SocialPerformanceRecord("Goal", 4, 5, 2026), second);

        manager.deleteSalesMan(1);

        assertNull(manager.readSalesMan(1));
        assertTrue(manager.readSocialPerformanceRecord(first).isEmpty());
        assertEquals(1, manager.readSocialPerformanceRecord(second).size());

        manager.deleteSalesMan(2);
    }
}
