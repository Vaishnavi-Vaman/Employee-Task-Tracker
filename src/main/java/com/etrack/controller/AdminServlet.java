package com.etrack.controller;

import com.etrack.dao.EmployeeDAO;
import com.etrack.dao.UserDAO;
import com.etrack.model.Employee;
import com.etrack.model.User;
import com.etrack.util.IdGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "AdminServlet", urlPatterns = {"/admin"})
public class AdminServlet extends HttpServlet {
    private transient EmployeeDAO employeeDAO;
    private transient UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        employeeDAO = new EmployeeDAO();
        userDAO = new UserDAO();
    }
  //non-admin users
    private boolean ensureAdmin(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("role") == null ||
                !"ADMIN".equalsIgnoreCase(session.getAttribute("role").toString())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        List<Employee> employees = employeeDAO.listAll();
        req.setAttribute("employees", employees);
        req.getRequestDispatcher("/admin.jsp").forward(req, resp);
    }

    @Override
    //handles adding or updating employees based on action
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;

        String action = req.getParameter("action");
        if ("addEmployee".equalsIgnoreCase(action)) {
            String ename = req.getParameter("ename");
            String email = req.getParameter("email");
            String role = req.getParameter("role");
            String ephno = req.getParameter("ephno");
            String password = req.getParameter("password");

            if (isBlank(ename) || isBlank(email) || isBlank(role) || isBlank(password)) {
                req.setAttribute("error", "All fields except phone are mandatory.");
                doGet(req, resp);    //reloads form page
                return;
            }

            // Normalize email and role
            String normEmail = email.trim().toLowerCase();
            String normRole = role == null ? "EMPLOYEE" : role.trim().toUpperCase();
            if (!"ADMIN".equals(normRole) && !"EMPLOYEE".equals(normRole)) {
                normRole = "EMPLOYEE";
            }

            // Prevent duplicate emails (check both employees and users collections)
            boolean emailExistsInEmployees = employeeDAO.findByEmail(normEmail) != null;
            boolean emailExistsInUsers = userDAO.findByEmail(normEmail) != null;
            if (emailExistsInEmployees || emailExistsInUsers) {
                req.setAttribute("error", "Email already exists. Please use a different email.");
                // Forward back to add employee form so the user sees the popup there
                req.getRequestDispatcher("/addEmployee.jsp").forward(req, resp);
                return;
            }

            int eid = IdGenerator.getNextSequence("employeeId");
            Employee e = new Employee(eid, ename, normEmail, ephno, normRole);
            employeeDAO.create(e);

            // Create login user for employee
            User u = new User(null, normEmail, password, normRole, eid);
            userDAO.createUser(u);

            req.setAttribute("success", "Employee added successfully (EID: " + eid + ")");
            doGet(req, resp);
        } else if ("updateEmployee".equalsIgnoreCase(action)) {
            String eidStr = req.getParameter("eid");
            String ename = req.getParameter("ename");
            String email = req.getParameter("email");
            String ephno = req.getParameter("ephno");
            String role = req.getParameter("role");

            Integer eid = null;
            try {
                eid = Integer.valueOf(eidStr);
            } catch (Exception ex) {
                req.setAttribute("error", "Invalid EID provided for update.");
                doGet(req, resp);
                return;
            }

            if (eid == null || isBlank(ename) || isBlank(email) || isBlank(role)) {
                req.setAttribute("error", "EID, name, email and role are mandatory.");
                doGet(req, resp);
                return;
            }
            // Normalize email and role
            String normEmail = email.trim().toLowerCase();
            String normRole = role == null ? "EMPLOYEE" : role.trim().toUpperCase();
            if (!"ADMIN".equals(normRole) && !"EMPLOYEE".equals(normRole)) {
                normRole = "EMPLOYEE";
            }

            // Prevent changing to an email that belongs to a different employee
            Employee existingByEmail = employeeDAO.findByEmail(normEmail);
            if (existingByEmail != null && existingByEmail.getEid() != null && !existingByEmail.getEid().equals(eid)) {
                req.setAttribute("error", "Another employee already uses this email.");
                doGet(req, resp);
                return;
            }

            Employee e = new Employee(eid, ename, normEmail, ephno, normRole);
            employeeDAO.update(e);

            // Sync changes to users collection as well (email and role), keeping password and eid unchanged
            userDAO.updateForEmployee(eid, normEmail, normRole);

            req.setAttribute("success", "Employee updated successfully (EID: " + eid + ")");
            doGet(req, resp);
        }
        
//        else if ("deleteEmployee".equalsIgnoreCase(action)) {
//            String eidStr = req.getParameter("eid");
//            Integer eid = null;
//            try {
//                eid = Integer.valueOf(eidStr);
//            } catch (Exception ex) {
//                req.setAttribute("error", "Invalid EID provided for deletion.");
//                doGet(req, resp);
//                return;
//            }
//
//            boolean deleted = employeeDAO.deleteByEid(eid);
//            if (deleted) {
//                // Also delete from users collection to avoid orphaned logins
//                userDAO.deleteByEid(eid);
//                req.setAttribute("success", "Employee deleted successfully (EID: " + eid + ")");
//            } else {
//                req.setAttribute("error", "Employee not found or could not be deleted.");
//            }
//            doGet(req, resp);
//        }

        else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}

