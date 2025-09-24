package com.etrack.controller;

import com.etrack.dao.EmployeeDAO;
import com.etrack.dao.UserDAO;
import com.etrack.model.Employee;
import com.etrack.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})    //registers servlet with container
public class LoginServlet extends HttpServlet {

    private transient UserDAO userDAO;
    private transient EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();   //instance
        employeeDAO = new EmployeeDAO();
        // Seed a default admin if missing
        userDAO.createAdminIfMissing("admin@example.com", "admin123");
        //will check admin exists or not ,if not add a default admin
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Forward to login page
        req.getRequestDispatcher("/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (email != null) email = email.trim();
        if (password != null) password = password.trim();

        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        if (!userDAO.verifyCredentials(email, password)) {
            System.out.println("[LoginServlet] Invalid credentials for email: " + email);
            req.setAttribute("error", "Invalid credentials.");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            return;
        }

        User user = userDAO.findByEmail(email);
        HttpSession session = req.getSession(true);
        session.setAttribute("userEmail", user.getEmail());
        session.setAttribute("role", user.getRole());

        if ("ADMIN".equalsIgnoreCase(user.getRole())) {
            resp.sendRedirect(req.getContextPath() + "/admin");
        } else {
            // Employee: load employee details
            Employee emp = null;
            if (user.getEid() != null) {
                emp = employeeDAO.findByEid(user.getEid());
            }
            if (emp == null) {
                emp = employeeDAO.findByEmail(user.getEmail());
            }
            if (emp != null) {
                session.setAttribute("eid", emp.getEid());
                session.setAttribute("ename", emp.getEname());
                session.setAttribute("ephno", emp.getEphno());
            }
            resp.sendRedirect(req.getContextPath() + "/employee");
        }
    }
}
