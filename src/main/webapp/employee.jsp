<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Employee</title>
  <style>
    body { font-family: Arial, sans-serif; background: #f8fafc; margin: 0; }
    header { display:flex; justify-content: space-between; align-items: center; background:#111827; color:#fff; padding:14px 20px; }
    .container { max-width: 1000px; margin: 24px auto; padding: 0 16px; }
    h1 { margin: 0; font-size: 18px; }
    a.logout { color:#fff; text-decoration:none; background:#ef4444; padding:8px 12px; border-radius:8px; }
    .card { background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:16px; margin-bottom:20px; }
    .row { display:flex; gap:12px; flex-wrap:wrap; }
    .col { flex:1 1 240px; }
    /* Utility to keep a specific row on a single line */
    .row.no-wrap { flex-wrap: nowrap; }
    .row.no-wrap .col { flex: 1 1 0; min-width: 0; }
    label { display:block; font-size:13px; color:#374151; margin-bottom:6px; font-weight:600; min-height:22px; line-height:1.25; width:100%; }
    input, select, textarea { width:100%; padding:10px 12px; border:1px solid #d1d5db; border-radius:8px; box-sizing:border-box; font-size:14px; }
    textarea { min-height: 80px; }
    .btn { background:#2563eb; color:#fff; border:none; padding:10px 14px; border-radius:8px; cursor:pointer; }
    .btn.secondary { background:#6b7280; }
    .btn.success { background:#10b981; }
    table { width:100%; border-collapse:collapse; }
    th, td { border-bottom:1px solid #e5e7eb; padding:10px; text-align:left; vertical-align:top; }
    .alert { padding:10px 12px; border-radius:8px; margin-bottom:12px; }
    .alert.error { background:#fee2e2; color:#991b1b; border:1px solid #fecaca; }
    .alert.success { background:#dcfce7; color:#166534; border:1px solid #bbf7d0; }

    /* Simple form styling */
    .styled-form { background:#f9fafb; border:1px solid #e5e7eb; border-radius:10px; padding:16px; }
    .styled-form .row { margin-bottom:12px; }
    .styled-form .btn { margin-top:4px; }

    /* Modal styles */
    .modal-backdrop { display:none; position: fixed; inset: 0; background: rgba(0,0,0,0.45); z-index: 40; }
    .modal { display:none; position: fixed; inset: 0; z-index: 50; align-items: center; justify-content: center; }
    .modal.open, .modal-backdrop.open { display:flex; }
    .modal-card { width: 100%; max-width: 680px; background:#fff; border-radius:12px; border:1px solid #e5e7eb; box-shadow: 0 10px 30px rgba(0,0,0,0.15); }
    .modal-header { padding:14px 16px; border-bottom:1px solid #e5e7eb; display:flex; justify-content: space-between; align-items:center; }
    .modal-title { margin:0; font-size:16px; }
    .close-btn { background:transparent; border:none; font-size:20px; cursor:pointer; }
    .modal-body { padding:16px; }
    .modal-footer { padding:12px 16px; border-top:1px solid #e5e7eb; display:flex; gap:8px; justify-content:flex-end; }
  </style>
</head>
<body>
  <header>
  <!-- Displays logged-in employee's name -->
    <h1>Welcome <c:out value='${sessionScope.ename}'/></h1>
    <a class="logout" href="<%=request.getContextPath()%>/logout" onclick="return confirm('Are you sure you want to log out?')">Logout</a>
  </header>

  <div class="container">
    <c:if test="${not empty success}"><div class="alert success">${success}</div></c:if>
    <c:if test="${not empty error}"><div class="alert error">${error}</div></c:if>

    <div class="card">
      <h3>Add Task</h3>
      <form class="styled-form" method="post" action="<%=request.getContextPath()%>/employee">
        <input type="hidden" name="action" value="add"/>
        <div class="row">
          <div class="col">
            <label>EID</label>
            <input type="text" value="${sessionScope.eid}"  disabled/>
          </div>
          <div class="col">
            <label>Name</label>
            <input type="text" value="${sessionScope.ename}" disabled />
          </div>
          <div class="col">
            <label>Email</label>
            <input type="text" value="${sessionScope.userEmail}" disabled />
          </div>
          <div class="col">
            <label>Phone</label>
            <input type="text" value="${sessionScope.ephno}" disabled />
          </div>
          <div class="col">
            <label>Role</label>
            <input type="text" value="EMPLOYEE" disabled />
          </div>
        </div>
        <div class="row">
          <div class="col">
            <label>Task Assigned</label>
            <input type="text" name="taskAssigned" required />
          </div>
          <div class="col">
            <label>Status</label>
            <select name="status" required>
              <option value="">Select status</option>
              <option value="PENDING">Pending</option>
              <option value="IN_PROGRESS">In Progress</option>
              <option value="COMPLETED">Completed</option>
            </select>
          </div>
        </div>
        <div class="row">
          <div class="col">
            <label>Task Description</label>
            <textarea name="taskDescription" required></textarea>
          </div>
        </div>
        <div class="row no-wrap">
          <div class="col">
            <label>Start Date</label>
            <input type="date" name="startDate" required />
          </div>
          <div class="col">
            <label>Start Time</label>
            <input type="time" name="startTime" required />
          </div>
          <div class="col">
            <label>End Date</label>
            <input type="date" name="endDate" required />
          </div>
          <div class="col">
            <label>End Time</label>
            <input type="time" name="endTime" required />
          </div>
        </div>
        <div style="margin-top:12px;">
          <button class="btn" type="submit">Add Task</button>
        </div>
      </form>
    </div>

    <div class="card">
      <h3>Your Tasks</h3>
      <c:choose>
        <c:when test="${empty tasks}"><div>No tasks yet.</div></c:when>
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
                <th>Update</th>
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
                  <button type="button"
                          class="btn success update-task-btn"
                          data-taskid="${t.taskId}"
                          data-taskassigned="${t.taskAssigned}"
                          data-taskdescription="${t.taskDescription}"
                          data-startdate="${t.startDate}"
                          data-starttime="${t.startTime}"
                          data-enddate="${t.endDate}"
                          data-endtime="${t.endTime}"
                          data-status="${t.status}">Update Task</button>
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
  <div id="taskBackdrop" class="modal-backdrop" aria-hidden="true"></div>

  <!-- Update Task Modal -->
  <div id="taskModal" class="modal" role="dialog" aria-modal="true" aria-labelledby="taskEditTitle">
    <div class="modal-card">
      <div class="modal-header">
        <h4 id="taskEditTitle" class="modal-title">Update Task</h4>
        <button class="close-btn" type="button" id="closeTaskModal" aria-label="Close">×</button>
      </div>
      <form id="taskForm" method="post" action="<%=request.getContextPath()%>/employee">
        <input type="hidden" name="action" value="update"/>
        <input type="hidden" id="taskId" name="taskId" />
        <div class="modal-body">
          <div class="row">
            <div class="col">
              <label for="taskAssigned">Task Assigned</label>
              <input id="taskAssigned" name="taskAssigned" type="text" />
            </div>
            <div class="col">
              <label for="status">Status</label>
              <select id="status" name="status">
                <option value="">-- Status --</option>
                <option value="PENDING">Pending</option>
                <option value="IN_PROGRESS">In Progress</option>
                <option value="COMPLETED">Completed</option>
              </select>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <label for="taskDescription">Task Description</label>
              <input id="taskDescription" name="taskDescription" type="text" />
            </div>
          </div>
          <div class="row">
            <div class="col">
              <label for="startDate">Start Date</label>
              <input id="startDate" name="startDate" type="date" />
            </div>
            <div class="col">
              <label for="startTime">Start Time</label>
              <input id="startTime" name="startTime" type="time" />
            </div>
            <div class="col">
              <label for="endDate">End Date</label>
              <input id="endDate" name="endDate" type="date" />
            </div>
            <div class="col">
              <label for="endTime">End Time</label>
              <input id="endTime" name="endTime" type="time" />
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn secondary" id="cancelTask">Cancel</button>
          <button type="submit" class="btn">Save</button>
        </div>
      </form>
    </div>
  </div>

  <script>
    (function(){
      const modal = document.getElementById('taskModal');
      const backdrop = document.getElementById('taskBackdrop');
      const closeBtn = document.getElementById('closeTaskModal');
      const cancelBtn = document.getElementById('cancelTask');
      const form = document.getElementById('taskForm');
      const idEl = document.getElementById('taskId');
      const titleEl = document.getElementById('taskAssigned');
      const descEl = document.getElementById('taskDescription');
      const sDateEl = document.getElementById('startDate');
      const sTimeEl = document.getElementById('startTime');
      const eDateEl = document.getElementById('endDate');
      const eTimeEl = document.getElementById('endTime');
      const statusEl = document.getElementById('status');

      function openModal(){ modal.classList.add('open'); backdrop.classList.add('open'); }
      function closeModal(){ modal.classList.remove('open'); backdrop.classList.remove('open'); }

      document.querySelectorAll('.update-task-btn').forEach(btn => {
        btn.addEventListener('click', function(){
          idEl.value = this.getAttribute('data-taskid');
          titleEl.value = this.getAttribute('data-taskassigned') || '';
          descEl.value = this.getAttribute('data-taskdescription') || '';
          sDateEl.value = this.getAttribute('data-startdate') || '';
          sTimeEl.value = this.getAttribute('data-starttime') || '';
          eDateEl.value = this.getAttribute('data-enddate') || '';
          eTimeEl.value = this.getAttribute('data-endtime') || '';
          const st = this.getAttribute('data-status') || '';
          statusEl.value = st;
          openModal();
        });
      });

      closeBtn.addEventListener('click', closeModal);
      cancelBtn.addEventListener('click', closeModal);
      backdrop.addEventListener('click', closeModal);

      form.addEventListener('submit', function(){
        if(!idEl.value){
          alert('Missing Task ID');
          return false;
        }
      });
    })();
  </script>
</body>
</html>