package org.hbrs.ia.code;

import org.hbrs.ia.model.SalesMan;
import org.hbrs.ia.model.SocialPerformanceRecord;

import java.util.List;

/**
 * Control class interface for managing salesmen and their social performance
 * records (part B of a bonus computation sheet).
 *
 * CRUD check of the original interface:
 *  - C: createSalesMan, addSocialPerformanceRecord         (was present)
 *  - R: readSalesMan, readAllSalesMen, readSocialPerformanceRecord (was present)
 *  - U: only "add a further record" is required            (addSocialPerformanceRecord)
 *  - D: was MISSING entirely -> deleteSalesMan, deleteSocialPerformanceRecords added
 * The year is integrated as a second, optional read criterion.
 */
public interface ManagePersonal {

    /** Create: stores a new salesman. The sid must be unique. */
    void createSalesMan(SalesMan salesMan);

    /** Create/Update: adds a further performance record to an existing salesman. */
    void addSocialPerformanceRecord(SocialPerformanceRecord record, SalesMan salesMan);

    /** Read: returns the salesman with the given sid, or null if there is none. */
    SalesMan readSalesMan(int sid);

    /** Read: returns all stored salesmen. */
    List<SalesMan> readAllSalesMen();

    /** Read: returns all performance records of a salesman, over all years. */
    List<SocialPerformanceRecord> readSocialPerformanceRecord(SalesMan salesMan);

    /** Read: returns the performance records of a salesman for one year. */
    List<SocialPerformanceRecord> readSocialPerformanceRecord(SalesMan salesMan, int year);

    /** Delete: removes a salesman together with all of his performance records. */
    void deleteSalesMan(int sid);

    /** Delete: removes all performance records of a salesman, the salesman stays. */
    void deleteSocialPerformanceRecords(SalesMan salesMan);
}
