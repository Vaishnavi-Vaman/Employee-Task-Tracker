package com.etrack.dao;

import com.etrack.model.Task;
import com.etrack.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class TaskDAO {
    private final MongoCollection<Document> tasks;

    public TaskDAO() {
        MongoDatabase db = DBConnection.getDatabase();
        this.tasks = db.getCollection("tasks");
    }

    public void create(Task t) {
        tasks.insertOne(t.toDocument());
    }

    public Task findByTaskId(int taskId) {
        Document d = tasks.find(eq("taskId", taskId)).first();
        return Task.fromDocument(d);
    }

    public List<Task> listByEid(int eid) {
        List<Task> list = new ArrayList<>();
        FindIterable<Document> docs = tasks.find(eq("eid", eid));
        for (Document d : docs) {
            list.add(Task.fromDocument(d));
        }
        return list;
    }

    public boolean deleteByTaskId(int taskId) {
        return tasks.deleteOne(eq("taskId", taskId)).getDeletedCount() > 0;
    }

    public boolean updatePartial(Task t) {
        // Build update sets only for non-null fields
        List<org.bson.conversions.Bson> updates = new ArrayList<>();
        if (t.getEname() != null) updates.add(set("ename", t.getEname()));
        if (t.getEmail() != null) updates.add(set("email", t.getEmail()));
        if (t.getRole() != null) updates.add(set("role", t.getRole()));
        if (t.getTaskAssigned() != null) updates.add(set("taskAssigned", t.getTaskAssigned()));
        if (t.getTaskDescription() != null) updates.add(set("taskDescription", t.getTaskDescription()));
        if (t.getStartDate() != null) updates.add(set("startDate", t.getStartDate()));
        if (t.getStartTime() != null) updates.add(set("startTime", t.getStartTime()));
        if (t.getEndDate() != null) updates.add(set("endDate", t.getEndDate()));
        if (t.getEndTime() != null) updates.add(set("endTime", t.getEndTime()));
        if (t.getStatus() != null) updates.add(set("status", t.getStatus()));

        if (updates.isEmpty()) return false;
        return tasks.updateOne(eq("taskId", t.getTaskId()), combine(updates)).getModifiedCount() > 0;
    }
}
