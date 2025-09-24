package com.etrack.util;



import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    // Change these according to your setup
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DB_NAME = "employee_task_tracker";

    private DBConnection() {
        // private constructor to prevent instantiation
    }

    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            try {
                mongoClient = MongoClients.create(CONNECTION_STRING);
                database = mongoClient.getDatabase(DB_NAME);
                System.out.println("✅ Connected to MongoDB: " + DB_NAME);
            } catch (Exception e) {
                System.err.println("❌ MongoDB Connection Failed: " + e.getMessage());
            }
        }
        return database;
    }

    public static void closeConnection() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
            System.out.println("🔒 MongoDB Connection Closed");
        }
    }
    
    // Main method to test connection
    public static void main(String[] args) {
        MongoDatabase db = DBConnection.getDatabase();

        if (db != null) {
            System.out.println("Database Name: " + db.getName());
        } else {
            System.out.println("Failed to connect to MongoDB");
        }

        DBConnection.closeConnection();
    }
}
