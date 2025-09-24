<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee Tasks</title>
<style>
  body { font-family: Arial, sans-serif; background: #f8fafc; margin: 0; }
  header { display:flex; justify-content: space-between; align-items: center; background:#111827; color:#fff; padding:14px 20px; }
  .container { max-width: 1000px; margin: 24px auto; padding: 0 16px; }
  a.btn { text-decoration:none; background:#2563eb; color:#fff; padding:8px 12px; border-radius:8px; }
  a.back { background:#6b7280; }
  .card { background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:16px; margin-bottom:20px; }
  table { width:100%; border-collapse:collapse; }
  th, td { border-bottom:1px solid #e5e7eb; padding:10px; text-align:left; vertical-align:top; }
  form.inline { display:inline; }
  button.danger { background:#ef4444; color:#fff; border:none; padding:8px 10px; border-radius:8px; cursor:pointer; }
</style>
</head>
<body>
  <header>
    <div>Employee Tasks</div>
    <div>
      <a class="btn back" href="<%=request.getContextPath()%>/admin">Back to Admin</a>
      <a class="btn" href="<%=request.getContextPath()%>/logout" onclick="return confirm('Are you sure you want to log out?')">Logout</a>
    </div>
  </header>
  <div class="container">
    <div class="card">
      <h3>Employee Details</h3>
      <p>
        <strong>EID:</strong> <c:out value='${employee.eid}'/> &nbsp;|
        <strong>Name:</strong> <c:out value='${employee.ename}'/> &nbsp;|
        <strong>Email:</strong> <c:out value='${employee.email}'/> &nbsp;|
        <strong>Phone:</strong> <c:out value='${employee.ephno}'/> &nbsp;|
        <strong>Role:</strong> <c:out value='${employee.role}'/>
      </p>
    </div>

    <div class="card">
      <h3>Tasks</h3>
      <c:choose>
        <c:when test="${empty tasks}"><div>No tasks for this employee.</div></c:when>
        <c:otherwise>
          <table>
            <thead>
              <tr>
                <th>ID</th>
                <th>Task</th>
                <th>Description</th>
                <th>Start</th>
                <th>End</th>
                <th>Status</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="t" items="${tasks}">
                <tr>
                  <td><c:out value='${t.taskId}'/></td>
                  <td><c:out value='${t.taskAssigned}'/></td>
                  <td><c:out value='${t.taskDescription}'/></td>
                  <td><c:out value='${t.startDate}'/> <br/> <c:out value='${t.startTime}'/></td>
                  <td><c:out value='${t.endDate}'/> <br/> <c:out value='${t.endTime}'/></td>
                  <td><c:out value='${t.status}'/></td>
                  <td>
                    <form class="inline" method="post" action="<%=request.getContextPath()%>/tasks" onsubmit="return confirm('Are you sure you want to delete this task?')">
                      <input type="hidden" name="action" value="delete"/>
                      <input type="hidden" name="eid" value="${employee.eid}"/>
                      <input type="hidden" name="taskId" value="${t.taskId}"/>
                      <button type="submit" class="danger">Delete</button>
                    </form>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</body>
</html>