<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Add Employee</title>
<style>
  body { font-family: Arial, sans-serif; background:#f8fafc; margin:0; }
  .container { max-width: 640px; margin: 40px auto; background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:20px; }
  h2 { margin-top:0; }
  label { display:block; font-size:13px; color:#374151; margin:10px 0 6px; }
  input, select { width:100%; padding:10px 12px; border:1px solid #d1d5db; border-radius:8px; box-sizing:border-box; }
  .row { display:flex; gap:12px; }
  .col { flex:1; }
  .btn { background:#2563eb; color:#fff; border:none; padding:10px 14px; border-radius:8px; cursor:pointer; }
  a.link { text-decoration:none; color:#2563eb; margin-left:8px; }
</style>
</head>
<body>
  <% 
     String err = (String) request.getAttribute("error"); 
     if (err != null) {
         String alertMsg = err.replace("\\", "\\\\").replace("'", "\\'").replace("\r", " ").replace("\n", " ");
  %>
  <script>
    window.addEventListener('DOMContentLoaded', function() {
      alert('<%= alertMsg %>');
    });
  </script>
  <% } %>
  <div class="container">
    <h2>Add Employee</h2>
    <form method="post" action="<%=request.getContextPath()%>/admin">
      <input type="hidden" name="action" value="addEmployee"/>
      <label for="ename">Name</label>
      <input type="text" id="ename" name="ename" value="<%= request.getParameter("ename") == null ? "" : request.getParameter("ename") %>" required />
      <div class="row">
        <div class="col">
          <label for="email">Email</label>
          <input type="email" id="email" name="email" value="<%= request.getParameter("email") == null ? "" : request.getParameter("email") %>" required />
        </div>
        <div class="col">
          <label for="ephno">Phone (optional)</label>
          <input type="text" id="ephno" name="ephno" value="<%= request.getParameter("ephno") == null ? "" : request.getParameter("ephno") %>" />
        </div>
      </div>
      <div class="row">
        <div class="col">
          <label for="role">Role</label>
          <select id="role" name="role" required>
            <option value="EMPLOYEE" <%= "EMPLOYEE".equalsIgnoreCase(request.getParameter("role")) ? "selected" : "" %>>EMPLOYEE</option>
            <option value="ADMIN" <%= "ADMIN".equalsIgnoreCase(request.getParameter("role")) ? "selected" : "" %>>ADMIN</option>
          </select>
        </div>
        <div class="col">
          <label for="password">Password</label>
          <input type="password" id="password" name="password" required />
        </div>
      </div>
      <div style="margin-top:12px;">
        <button class="btn" type="submit">Add Employee</button>
        <a class="link" href="<%=request.getContextPath()%>/admin">Back to Admin</a>
      </div>
    </form>
  </div>
</body>
</html>