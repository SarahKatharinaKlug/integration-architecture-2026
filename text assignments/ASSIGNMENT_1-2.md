# Assignment 2 – Managing Salesmen with MongoDB

## Run it
1. Install and start a local MongoDB (`mongod`, port 27017).
2. `mvn test` runs the JUnit tests, `org.hbrs.ia.code.Main` runs a small demo.
   (Tested against the pom's driver `mongodb-driver-legacy 4.1.0`, JUnit 5.8.1, Java 11.)

## a) Implementation and schema
`ManagePersonalImpl` implements `ManagePersonal` with the plain MongoDB driver (no Spring).
Two collections, records point to their salesman via `sid`:

| Collection          | Fields                                                        | Index            |
|---------------------|---------------------------------------------------------------|------------------|
| `salesmen`          | `firstname`, `lastname`, `sid`                                | `sid` unique     |
| `socialperformance` | `sid`, `goalDescription`, `targetValue`, `actualValue`, `year`| `(sid, year)`    |

**Single collection instead?** Possible: one `salesmen` document with an embedded array
`records: [{goalDescription, targetValue, actualValue, year}]`, extended with `$push`.
Pros: one read returns everything, no second lookup, atomic per salesman.
Cons: the document grows with every year, records cannot easily be queried on their own
(e.g. all records of 2026), and deleting/changing one record means array updates.
I chose two collections because records accumulate over the years and are queried by year.

## b) CRUD check
The original interface had Create and Read, but **no Delete**, and Update was only
partly covered. Changes to `ManagePersonal`:
- added `deleteSalesMan(int sid)` (also removes his records),
- added `deleteSocialPerformanceRecords(SalesMan)`,
- added `readSocialPerformanceRecord(SalesMan, int year)` to integrate the year,
- `addSocialPerformanceRecord` is the only "U" (as the task allows).

## c) Tests
`ManagePersonalTest` creates, reads, adds records, filters by year and deletes again.
It uses its own database, which is dropped before and after each test, and `tearDown`
asserts that both collections are empty, so a test that leaves data behind fails.
