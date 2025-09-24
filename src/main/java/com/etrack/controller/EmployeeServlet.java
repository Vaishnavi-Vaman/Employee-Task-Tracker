package com.etrack.controller;

import com.etrack.dao.TaskDAO;
import com.etrack.model.Task;
import com.etrack.util.IdGenerator;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "EmployeeServlet", urlPatterns = {"/employee"})
public class EmployeeServlet extends HttpServlet {
    private transient TaskDAO taskDAO;
     int taskId;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
    }

    private boolean ensureEmployee(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession(false);  //current HTTP session without creating a new one
        if (session == null || session.getAttribute("role") == null ||
                !"EMPLOYEE".equalsIgnoreCase(session.getAttribute("role").toString())) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        return true;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureEmployee(req, resp)) return;
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        if (eid != null) {
            List<Task> tasks = taskDAO.listByEid(eid);
            req.setAttribute("tasks", tasks);
        }
        req.getRequestDispatcher("/employee.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureEmployee(req, resp)) return;
        String action = req.getParameter("action");
        Integer eid = (Integer) req.getSession().getAttribute("eid");
        String ename = (String) req.getSession().getAttribute("ename");
        String email = (String) req.getSession().getAttribute("userEmail");

        if ("add".equalsIgnoreCase(action)) {
            String role = "EMPLOYEE";
            String taskAssigned = req.getParameter("taskAssigned");
            String taskDescription = req.getParameter("taskDescription");
            String startDate = req.getParameter("startDate");
            String startTime = req.getParameter("startTime");
            String endDate = req.getParameter("endDate");
            String endTime = req.getParameter("endTime");
            String status = req.getParameter("status");

            if (isBlank(ename) || eid == null || isBlank(email) || isBlank(role) ||
                    isBlank(taskAssigned) || isBlank(taskDescription) ||
                    isBlank(startDate) || isBlank(startTime) || isBlank(endDate) || isBlank(endTime) ||
                    isBlank(status)) {
                req.setAttribute("error", "All fields are mandatory.");
                doGet(req, resp);
                return;
            }

//           int taskId = IdGenerator.getNextSequence("taskId");
             taskId = IdGenerator.getTaskId(eid)+1;
            
            Task t = new Task(taskId, eid, ename, email, role, taskAssigned, taskDescription,
                    startDate, startTime, endDate, endTime, status);
            taskDAO.create(t);
            req.setAttribute("success", "Task added successfully (ID: " + taskId + ")");
            doGet(req, resp);
           
        } else if ("update".equalsIgnoreCase(action)) {
            String taskIdStr = req.getParameter("taskId");
            if (isBlank(taskIdStr)) {
                req.setAttribute("error", "Task ID is required to update.");
                doGet(req, resp);
                return;
            }
            int taskId = Integer.parseInt(taskIdStr);

            // Gather possible updates; only non-empty will be applied
            Task patch = new Task();
            patch.setTaskId(taskId);
            setIfPresent(patch::setTaskAssigned, req.getParameter("taskAssigned"));  //form input
            setIfPresent(patch::setTaskDescription, req.getParameter("taskDescription"));
            setIfPresent(patch::setStartDate, req.getParameter("startDate"));
            setIfPresent(patch::setStartTime, req.getParameter("startTime"));
            setIfPresent(patch::setEndDate, req.getParameter("endDate"));
            setIfPresent(patch::setEndTime, req.getParameter("endTime"));
            setIfPresent(patch::setStatus, req.getParameter("status"));

            boolean updated = taskDAO.updatePartial(patch);
            if (updated) {
                req.setAttribute("success", "Update successful.");
            } else {
                req.setAttribute("error", "No updates done.");
            }
            doGet(req, resp);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }

    private interface Setter { void set(String v); }
    private void setIfPresent(Setter setter, String value) { if (value != null && !value.trim().isEmpty()) setter.set(value.trim()); }
}
