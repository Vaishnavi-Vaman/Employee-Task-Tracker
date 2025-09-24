package com.etrack.controller;

import com.etrack.dao.EmployeeDAO;
import com.etrack.dao.TaskDAO;
import com.etrack.model.Employee;
import com.etrack.model.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(name = "TaskServlet", urlPatterns = {"/tasks"})
public class TaskServlet extends HttpServlet {
    private transient TaskDAO taskDAO;
    private transient EmployeeDAO employeeDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

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
        String eidStr = req.getParameter("eid");
        if (eidStr == null) {
            resp.sendRedirect(req.getContextPath() + "/admin");
            return;
        }
        int eid = Integer.parseInt(eidStr);
        Employee emp = employeeDAO.findByEid(eid);
        List<Task> tasks = taskDAO.listByEid(eid);
        req.setAttribute("employee", emp);
        req.setAttribute("tasks", tasks);
        req.getRequestDispatcher("/viewTasks.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!ensureAdmin(req, resp)) return;
        String action = req.getParameter("action");
        if ("delete".equalsIgnoreCase(action)) {
            String taskIdStr = req.getParameter("taskId");
            String eidStr = req.getParameter("eid");
            if (taskIdStr != null) {
                int taskId = Integer.parseInt(taskIdStr);
                taskDAO.deleteByTaskId(taskId);
            }
            // Redirect back to view for same employee
            resp.sendRedirect(req.getContextPath() + "/tasks?eid=" + (eidStr == null ? "" : eidStr));
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Unknown action");
        }
    }
}
