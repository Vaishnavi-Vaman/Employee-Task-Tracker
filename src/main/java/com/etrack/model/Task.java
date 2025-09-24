package com.etrack.model;

import org.bson.Document;

public class Task {
    private Integer taskId; // auto-increment
    private Integer eid; // employee id
    private String ename;
    private String email;
    private String role; // EMPLOYEE
    private String taskAssigned;
    private String taskDescription;
    private String startDate; // store as ISO date string for simplicity
    private String startTime;
    private String endDate;
    private String endTime;
    private String status; // COMPLETED / PENDING / IN_PROGRESS

    public Task() {}

    public Task(Integer taskId, Integer eid, String ename, String email, String role,
                String taskAssigned, String taskDescription,
                String startDate, String startTime, String endDate, String endTime,
                String status) {
        this.taskId = taskId;
        this.eid = eid;
        this.ename = ename;
        this.email = email;
        this.role = role;
        this.taskAssigned = taskAssigned;
        this.taskDescription = taskDescription;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.status = status;
    }

    public Integer getTaskId() { return taskId; }
    public void setTaskId(Integer taskId) { this.taskId = taskId; }
    public Integer getEid() { return eid; }
    public void setEid(Integer eid) { this.eid = eid; }
    public String getEname() { return ename; }
    public void setEname(String ename) { this.ename = ename; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getTaskAssigned() { return taskAssigned; }
    public void setTaskAssigned(String taskAssigned) { this.taskAssigned = taskAssigned; }
    public String getTaskDescription() { return taskDescription; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }
    public String getStartDate() { return startDate; }
    public void setStartDate(String startDate) { this.startDate = startDate; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public String getEndDate() { return endDate; }
    public void setEndDate(String endDate) { this.endDate = endDate; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Document toDocument() {
        return new Document("taskId", taskId)
                .append("eid", eid)
                .append("ename", ename)
                .append("email", email)
                .append("role", role)
                .append("taskAssigned", taskAssigned)
                .append("taskDescription", taskDescription)
                .append("startDate", startDate)
                .append("startTime", startTime)
                .append("endDate", endDate)
                .append("endTime", endTime)
                .append("status", status);
    }

    public static Task fromDocument(Document d) {
        if (d == null) return null;
        Task t = new Task();
        Object tid = d.get("taskId");
        t.setTaskId(tid == null ? null : (tid instanceof Integer ? (Integer) tid : Integer.valueOf(tid.toString())));
        Object eidObj = d.get("eid");
        t.setEid(eidObj == null ? null : (eidObj instanceof Integer ? (Integer) eidObj : Integer.valueOf(eidObj.toString())));
        t.setEname(d.getString("ename"));
        t.setEmail(d.getString("email"));
        t.setRole(d.getString("role"));
        t.setTaskAssigned(d.getString("taskAssigned"));
        t.setTaskDescription(d.getString("taskDescription"));
        t.setStartDate(d.getString("startDate"));
        t.setStartTime(d.getString("startTime"));
        t.setEndDate(d.getString("endDate"));
        t.setEndTime(d.getString("endTime"));
        t.setStatus(d.getString("status"));
        return t;
    }
}
