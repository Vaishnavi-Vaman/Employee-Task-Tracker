package com.etrack.dao;

import com.etrack.model.Employee;
import com.etrack.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;

public class EmployeeDAO {
    private final MongoCollection<Document> employees;

    public EmployeeDAO() {
        MongoDatabase db = DBConnection.getDatabase();
        this.employees = db.getCollection("employees");
    }

    public void create(Employee e) {
        employees.insertOne(e.toDocument());
    }

    public List<Employee> listAll() {
        List<Employee> list = new ArrayList<>();
        FindIterable<Document> docs = employees.find();
        for (Document d : docs) {
            list.add(Employee.fromDocument(d));
        }
        return list;
    }

    public Employee findByEid(int eid) {
        Document d = employees.find(eq("eid", eid)).first();
        return Employee.fromDocument(d);
    }

    public Employee findByEmail(String email) {
        if (email == null) return null;
        String trimmed = email.trim();
        // Case-insensitive exact match using regex with anchors
        Document d = employees.find(regex("email", "^" + Pattern.quote(trimmed) + "$", "i")).first();
        if (d == null) {
            // Fallback exact match
            d = employees.find(eq("email", trimmed)).first();
        }
        return Employee.fromDocument(d);
    }
    
//    public boolean deleteByEid(int eid) {
//        return employees.deleteOne(eq("eid", eid)).getDeletedCount() > 0;
//    }


    /**
     * Update an existing employee identified by eid.
     * Fields that can be modified: ename, email, ephno, role. EID is not changed.
     */
    public void update(Employee e) {
        if (e == null || e.getEid() == null) return;
        // Build the $set document without changing eid
        Document setDoc = new Document()
                .append("ename", e.getEname())
                .append("email", e.getEmail())
                .append("ephno", e.getEphno())
                .append("role", e.getRole());
        employees.updateOne(eq("eid", e.getEid()), new Document("$set", setDoc));
    }
}
