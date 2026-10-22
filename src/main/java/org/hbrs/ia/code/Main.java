package org.hbrs.ia.code;

import com.mongodb.MongoClient;
import org.hbrs.ia.model.SalesMan;
import org.hbrs.ia.model.SocialPerformanceRecord;

/**
 * Small demo: needs a local MongoDB on localhost:27017 (start it with 'mongod').
 */
public class Main {

    public static void main(String[] args) {
        try (MongoClient client = new MongoClient("localhost", 27017)) {
            ManagePersonal manager = ManagePersonalImpl.connectToLocalhost(client, "highperformance");

            SalesMan salesMan = new SalesMan("Leslie", "Malton", 90444);
            manager.createSalesMan(salesMan);
            manager.addSocialPerformanceRecord(
                    new SocialPerformanceRecord("Leadership Competence", 4, 5, 2026), salesMan);
            manager.addSocialPerformanceRecord(
                    new SocialPerformanceRecord("Openness to Employee", 4, 3, 2026), salesMan);

            System.out.println(manager.readAllSalesMen());
            System.out.println(manager.readSocialPerformanceRecord(salesMan, 2026));

            manager.deleteSalesMan(salesMan.getId());
        }
    }
}
