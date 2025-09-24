package com.etrack.util;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.descending;
import static com.mongodb.client.model.Updates.inc;

public class IdGenerator {

    private static MongoDatabase database = DBConnection.getDatabase();

    /**
     * Get the maximum taskId for a given employee (eid).
     * Returns 0 if no tasks exist for that employee.
     */
    public static int getTaskId(int eid) {
        MongoCollection<Document> tasks = database.getCollection("tasks"); // replace with your collection name

        // Find the task with the highest taskId for this eid
        Document doc = tasks.find(eq("eid", eid))
                            .sort(descending("taskId"))
                            .first();

        return (doc != null) ? doc.getInteger("taskId") : 0;
    }

    /**
     * Global sequence generator (like auto-increment).
     * Example: getNextSequence("employeeId") or getNextSequence("taskId")
     */
    public static int getNextSequence(String key) {
        MongoCollection<Document> counters = database.getCollection("counters");

        // Atomically increment and return old value
        Document updated = counters.findOneAndUpdate(
                eq("_id", key),
                inc("sequence_value", 1)
        );

        if (updated != null) {
            return updated.getInteger("sequence_value") + 1;
        } else {
            // If no counter exists, create it starting at 1
            Document doc = new Document("_id", key)
                    .append("sequence_value", 1);
            counters.insertOne(doc);
            return 1;
        }
    }
}
