package com.etrack.model;

import org.bson.Document;

public class User {
    private String id; // Mongo _id string or UUID
    private String email;
    private String password; // for simplicity; consider hashing later
    private String role; // "ADMIN" or "EMPLOYEE"
    private Integer eid; // link to Employee.eid when role is EMPLOYEE

    public User() {}

    public User(String id, String email, String password, String role, Integer eid) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.eid = eid;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Integer getEid() { return eid; }
    public void setEid(Integer eid) { this.eid = eid; }

    public Document toDocument() {
        Document doc = new Document();
        if (id != null) doc.append("_id", id);
        doc.append("email", email)
           .append("password", password)
           .append("role", role)
           .append("eid", eid);
        return doc;
    }

    public static User fromDocument(Document d) {
        if (d == null) return null;
        User u = new User();
        Object oid = d.get("_id");
        u.setId(oid == null ? null : oid.toString());
        u.setEmail(d.getString("email"));
        u.setPassword(d.getString("password"));
        u.setRole(d.getString("role"));
        Object e = d.get("eid");
        u.setEid(e == null ? null : (e instanceof Integer ? (Integer)e : Integer.valueOf(e.toString())));
        return u;
    }
}
