**Employee Task Tracker** 📝
A simple web application to manage employees and track their daily tasks. Built with Java , JSP/Servlets, and MongoDB.

🚀 Features

- User Authentication → Admin & Employee login system.
- Admin Dashboard → Add employees, edit employees,view all tasks.
- Employee Dashboard → Add task, update task status.
- Task Management → Create, update, delete, and view tasks.
- Role-based Access → Separate views for Admin and Employees.
- Auto Increment IDs → Managed using counters collection.

🛠 Tech Stack
- Java: 17
- Servlet & JSP: Jakarta Servlet API 5.0.0
- Frontend: JSP + JSTL + CSS
- Database: MongoDB (with MongoDB Java Driver)
- Build Tool: Maven
- Server: Apache Tomcat 10.1

⚙️ Setup Instructions

1. Prerequisites
Install Java 17
Install Apache Tomcat 9+
Install Maven
Install MongoDB Community Edition
IDE: Eclipse / IntelliJ IDEA

2. Database Setup (MongoDB)
    1.Start MongoDB server:
   - mongod
   2.Open Mongo Shell or Compass.
  - Create database and collections:
         Database name:employee_task_tracker
         collections: counters,employees,tasks,users
    
3. Import Project
- Clone or download this repo.
- Open Eclipse/IntelliJ → Import as Maven Project.
- Ensure Java 17 SDK is configured.

4. Build & Deploy

5.Run Application
Open server:
- Login as Admin → email: admin@gmail.com, Password: admin123
- Login as Employee → email: emp1, Password: emp123
