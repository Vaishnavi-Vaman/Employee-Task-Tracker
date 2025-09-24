package com.etrack.dao;

import com.etrack.model.User;
import com.etrack.util.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.regex;
import java.util.regex.Pattern;

public class UserDAO {
    private final MongoCollection<Document> users;
    private final MongoCollection<Document> employees;

    public UserDAO() {
        MongoDatabase db = DBConnection.getDatabase();
        this.users = db.getCollection("users");
        this.employees = db.getCollection("employees");
    }

    public User findByEmail(String email) {
        if (email == null) return null;
        String trimmed = email.trim();
        // Case-insensitive exact match using regex with anchors
        Document d = users.find(regex("email", "^" + Pattern.quote(trimmed) + "$", "i")).first();
        if (d == null) {
            // Fallback: try exact (case-sensitive) just in case
            d = users.find(eq("email", trimmed)).first();    //it will check for exact mail
        }
        if (d == null) {
            System.out.println("[UserDAO] No user found for email: " + trimmed);
        }
        return User.fromDocument(d);    //maps mongodb feild to java class 
    }

    /**
     * Attempt to find a user by email; if not found in users, look in employees collection.
     * If an employee with matching email exists and has a password field, create a users entry on the fly.
     */
    public User findOrProvisionByEmail(String email) {
        User u = findByEmail(email);
        if (u != null) return u;

        if (email == null) return null;
        String trimmed = email.trim();
        // Look in employees by email (case-insensitive)
        Document emp = employees.find(regex("email", "^" + Pattern.quote(trimmed) + "$", "i")).first();
        if (emp == null) return null;

        // Extract fields from employee doc
        String pwd = emp.getString("password");
        if (pwd == null) {
            System.out.println("[UserDAO] Employee found but no password field; cannot provision user.");
            return null;
        }
        Object eidObj = emp.get("eid"); //fetching id from emp db
        Integer eid = null;
        if (eidObj != null) {
            eid = (eidObj instanceof Integer) ? (Integer) eidObj : Integer.valueOf(eidObj.toString());
        }
        String role = emp.getString("role");
        // Normalize role to uppercase expected values
        String normRole = role == null ? "EMPLOYEE" : role.trim().toUpperCase();
        if (!"ADMIN".equals(normRole) && !"EMPLOYEE".equals(normRole)) {
            normRole = "EMPLOYEE";
        }

        User provisioned = new User(null, trimmed, pwd, normRole, eid);
        createUser(provisioned);
        System.out.println("[UserDAO] Auto-provisioned user from employees for email: " + trimmed + ", role=" + normRole + ", eid=" + eid);
        return provisioned;
    }

    public void createUser(User user) {
        if (user.getId() == null) {
            // Let Mongo assign _id
            users.insertOne(user.toDocument());
        } else {
            users.insertOne(user.toDocument());
        }
    }

    public boolean verifyCredentials(String email, String password) {
        User u = findOrProvisionByEmail(email);
        boolean ok = u != null && u.getPassword() != null && u.getPassword().equals(password);
        if (!ok) {
            System.out.println("[UserDAO] Credential check failed for email: " + (email == null ? "<null>" : email.trim()));
        }
        return ok;
    }

    public void createAdminIfMissing(String email, String password) {
        User existing = findByEmail(email);
        if (existing == null) {
            User admin = new User(null, email, password, "ADMIN", null);
            createUser(admin);
        }
    }
//    public boolean deleteByEid(int eid) {
//        return users.deleteOne(eq("eid", eid)).getDeletedCount() > 0;
//    }


    /**
     * Sync user record for an employee when employee details change.
     * This updates the user's email and role for the given eid. Password is not changed here.
     * If no user exists with that eid, this method is a no-op.
     */
    public void updateForEmployee(Integer eid, String newEmail, String newRole) {
        if (eid == null) return;
        Document setDoc = new Document();
        if (newEmail != null) setDoc.append("email", newEmail);
        if (newRole != null) setDoc.append("role", newRole);
        if (setDoc.isEmpty()) return;
        users.updateOne(eq("eid", eid), new Document("$set", setDoc));
    }
}
