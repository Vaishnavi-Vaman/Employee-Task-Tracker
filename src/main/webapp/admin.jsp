<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Admin</title>
<style>
  body { font-family: Arial, sans-serif; background: #f8fafc; margin: 0; }
  header { display:flex; justify-content: space-between; align-items: center; background:#111827; color:#fff; padding:14px 20px; }
  .container { max-width: 1000px; margin: 24px auto; padding: 0 16px; }
  h1 { margin: 0; font-size: 18px; }
  a.btn { text-decoration:none; background:#2563eb; color:#fff; padding:8px 12px; border-radius:8px; }
  a.logout { background:#ef4444; }
  .card { background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:16px; margin-bottom:20px; }
  table { width:100%; border-collapse:collapse; }
  th, td { border-bottom:1px solid #e5e7eb; padding:10px; text-align:left; }
  .alert { padding:10px 12px; border-radius:8px; margin:12px 0; }
  .alert.error { background:#fee2e2; color:#991b1b; border:1px solid #fecaca; }
  .alert.success { background:#dcfce7; color:#166534; border:1px solid #bbf7d0; }

  /* Modal styles */
  .modal-backdrop { display:none; position: fixed; inset: 0; background: rgba(0,0,0,0.45); z-index: 40; }
  .modal { display:none; position: fixed; inset: 0; z-index: 50; align-items: center; justify-content: center; }
  .modal.open, .modal-backdrop.open { display:flex; }
  .modal-card { width: 100%; max-width: 520px; background:#fff; border-radius:12px; border:1px solid #e5e7eb; box-shadow: 0 10px 30px rgba(0,0,0,0.15); }
  .modal-header { padding:14px 16px; border-bottom:1px solid #e5e7eb; display:flex; justify-content: space-between; align-items:center; }
  .modal-title { margin:0; font-size:16px; }
  .close-btn { background:transparent; border:none; font-size:20px; cursor:pointer; }
  .modal-body { padding:16px; }
  .form-row { margin-bottom:12px; display:flex; flex-direction: column; }
  .form-row label { font-size: 12px; color:#374151; margin-bottom:6px; }
  .form-row input, .form-row select { padding:8px 10px; border:1px solid #d1d5db; border-radius:8px; font-size:14px; }
  .modal-footer { padding:12px 16px; border-top:1px solid #e5e7eb; display:flex; gap:8px; justify-content:flex-end; }
  .btn { display:inline-block; text-decoration:none; padding:8px 12px; border-radius:8px; cursor:pointer; border:none; font-size:14px; }
  .btn.primary { background:#2563eb; color:#fff; }
  .btn.secondary { background:#e5e7eb; color:#111827; }
</style>
</head>
<body>
  <header>
    <h1>Admin Dashboard</h1>
    <div>
      <a class="btn" href="<%=request.getContextPath()%>/addEmployee.jsp">Add Employee</a>
      <a class="btn logout" href="<%=request.getContextPath()%>/logout" onclick="return confirm('Are you sure you want to log out?')">Logout</a>
    </div>
  </header>
  <div class="container">
    <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>

    <div class="card">
      <h3>Employees</h3>
      <c:choose>   
        <c:when test="${empty employees}"><div>No employees added yet.</div></c:when>
        <c:otherwise>
          <table>
            <thead>
              <tr>
                <th>EID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="e" items="${employees}">  <!-- iterates collections -->
                <tr>
                  <td><c:out value='${e.eid}'/></td>
                  <td><c:out value='${e.ename}'/></td>
                  <td><c:out value='${e.email}'/></td>
                  <td><c:out value='${e.ephno}'/></td>
                  <td><c:out value='${e.role}'/></td>
                  <td>
            <a class="btn" href="<%=request.getContextPath()%>/tasks?eid=${e.eid}">View</a>
                    <button type="button"
                            class="btn primary edit-btn"
                            data-eid="${e.eid}"
                            data-ename="${e.ename}"
                            data-email="${e.email}"
                            data-ephno="${e.ephno}"
                            data-role="${e.role}">Edit</button>
                  <%--  <form method="post" action="<%=request.getContextPath()%>/admin" style="display:inline;">
          <input type="hidden" name="action" value="deleteEmployee"/>
          <input type="hidden" name="eid" value="${e.eid}"/>
          <button class="btn logout" type="submit" onclick="return confirm('Are you sure you want to delete this employee?')">
            Delete
          </button>
        </form> --%>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </c:otherwise>
      </c:choose>
    </div>
  </div>

  <!-- Modal Backdrop -->
  <div id="editBackdrop" class="modal-backdrop" aria-hidden="true"></div>

  <!-- Edit Modal -->
  <div id="editModal" class="modal" role="dialog" aria-modal="true" aria-labelledby="editTitle">
    <div class="modal-card">
      <div class="modal-header">
        <h4 id="editTitle" class="modal-title">Edit Employee</h4>
        <button class="close-btn" type="button" id="closeEditModal" aria-label="Close">×</button>
      </div>
      <form id="editForm" method="post" action="<%=request.getContextPath()%>/admin">
        <input type="hidden" name="action" value="updateEmployee"/>
        <div class="modal-body">
          <div class="form-row">
            <label for="eid">EID</label>
            <input id="eid" name="eid" type="text" readonly />
          </div>
          <div class="form-row">
            <label for="ename">Name</label>
            <input id="ename" name="ename" type="text" required />
          </div>
          <div class="form-row">
            <label for="email">Email</label>
            <input id="email" name="email" type="email" required />
          </div>
          <div class="form-row">
            <label for="ephno">Phone</label>
            <input id="ephno" name="ephno" type="text" />
          </div>
          <div class="form-row">
            <label for="role">Role</label>
             <select id="role" name="role">
              <option value="EMPLOYEE">EMPLOYEE</option>
              <option value="ADMIN">ADMIN</option>
            </select>
            <!-- <input id="role" name="role"  value="EMPLOYEE" readonly/>  -->
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn secondary" id="cancelEdit">Cancel</button>
          <button type="submit" class="btn primary">Save</button>
        </div>
      </form>
    </div>
  </div>

  <script>
    (function(){
      const modal = document.getElementById('editModal');
      const backdrop = document.getElementById('editBackdrop');
      const closeBtn = document.getElementById('closeEditModal');
      const cancelBtn = document.getElementById('cancelEdit');
      const form = document.getElementById('editForm');
      const eidEl = document.getElementById('eid');
      const enameEl = document.getElementById('ename');
      const emailEl = document.getElementById('email');
      const ephnoEl = document.getElementById('ephno');
      const roleEl = document.getElementById('role');

      function openModal(){ modal.classList.add('open'); backdrop.classList.add('open'); }
      function closeModal(){ modal.classList.remove('open'); backdrop.classList.remove('open'); }

      document.querySelectorAll('.edit-btn').forEach(btn => {
        btn.addEventListener('click', function(){
          const eid = this.getAttribute('data-eid');
          const ename = this.getAttribute('data-ename');
          const email = this.getAttribute('data-email');
          const ephno = this.getAttribute('data-ephno') || '';
          const role = this.getAttribute('data-role') || 'EMPLOYEE';

          eidEl.value = eid;
          enameEl.value = ename;
          emailEl.value = email;
          ephnoEl.value = ephno;
          roleEl.value = role;                        
          

          openModal();
        });
      });

      closeBtn.addEventListener('click', closeModal);
      cancelBtn.addEventListener('click', closeModal);
      backdrop.addEventListener('click', closeModal);

      // Optional basic guard before submit
      form.addEventListener('submit', function(){
        // ensure eid is present
        if(!eidEl.value){
          alert('Missing EID.');
          return false;
        }
      });
    })();
  </script>
</body>
</html>
