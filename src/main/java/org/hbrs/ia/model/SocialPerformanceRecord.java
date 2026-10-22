package org.hbrs.ia.model;

import org.bson.Document;

/**
 * One line of part B ("social performance") of a bonus computation sheet.
 * A salesman has one such record per goal and year.
 */
public class SocialPerformanceRecord {
    private String goalDescription;
    private int targetValue;
    private int actualValue;
    private int year;

    public SocialPerformanceRecord(String goalDescription, int targetValue, int actualValue, int year) {
        this.goalDescription = goalDescription;
        this.targetValue = targetValue;
        this.actualValue = actualValue;
        this.year = year;
    }

    public String getGoalDescription() {
        return goalDescription;
    }

    public void setGoalDescription(String goalDescription) {
        this.goalDescription = goalDescription;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }

    public int getActualValue() {
        return actualValue;
    }

    public void setActualValue(int actualValue) {
        this.actualValue = actualValue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Maps this record to a MongoDB document. The owning salesman is
     * referenced by his sid, because records live in their own collection.
     */
    public Document toDocument(int sid) {
        return new Document()
                .append("sid", sid)
                .append("goalDescription", goalDescription)
                .append("targetValue", targetValue)
                .append("actualValue", actualValue)
                .append("year", year);
    }

    public static SocialPerformanceRecord fromDocument(Document document) {
        return new SocialPerformanceRecord(
                document.getString("goalDescription"),
                document.getInteger("targetValue"),
                document.getInteger("actualValue"),
                document.getInteger("year"));
    }

    @Override
    public String toString() {
        return "SocialPerformanceRecord{" + year + ", " + goalDescription
                + ", target=" + targetValue + ", actual=" + actualValue + "}";
    }
}
