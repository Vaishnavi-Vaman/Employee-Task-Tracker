package com.etrack.model;

import org.bson.Document;

public class Employee {
    private Integer eid; // auto-increment
    private String ename;
    private String email;
    private String ephno;
    private String role; // usually EMPLOYEE

    public Employee() {}

    public Employee(Integer eid, String ename, String email, String ephno, String role) {
        this.eid = eid;
        this.ename = ename;
        this.email = email;
        this.ephno = ephno;
        this.role = role;
    }

    public Integer getEid() { return eid; }
    public void setEid(Integer eid) { this.eid = eid; }

    public String getEname() { return ename; }
    public void setEname(String ename) { this.ename = ename; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getEphno() { return ephno; }
    public void setEphno(String ephno) { this.ephno = ephno; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Document toDocument() {
        return new Document("eid", eid)
                .append("ename", ename)
                .append("email", email)
                .append("ephno", ephno)
                .append("role", role);
    }

    public static Employee fromDocument(Document d) {
        if (d == null) return null;
        Employee e = new Employee();
        Object id = d.get("eid");
        e.setEid(id == null ? null : (id instanceof Integer ? (Integer) id : Integer.valueOf(id.toString())));
        e.setEname(d.getString("ename"));
        e.setEmail(d.getString("email"));
        e.setEphno(d.getString("ephno"));
        e.setRole(d.getString("role"));
        return e;
    }
}
