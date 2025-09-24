<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
<style>
  body { font-family: Arial, sans-serif; background: #f5f7fb; margin: 0; }
  .container { max-width: 420px; margin: 8vh auto; background: #fff; padding: 24px 28px; border-radius: 10px; box-shadow: 0 10px 25px rgba(0,0,0,0.08); }
  h2 { margin: 0 0 16px; font-weight: 600; color: #1f2937; }
  p.sub { margin: 0 0 24px; color: #6b7280; font-size: 14px; }
  label { display: block; margin: 12px 0 6px; color: #374151; font-size: 14px; }
  input[type="email"], input[type="password"] { width: 100%; padding: 12px 14px; border: 1px solid #d1d5db; border-radius: 8px; font-size: 14px; outline: none; transition: border-color .15s ease; box-sizing: border-box; }
  input[type="email"]:focus, input[type="password"]:focus { border-color: #6366f1; }
  .btn { width: 100%; margin-top: 16px; background: #4f46e5; color: #fff; border: none; padding: 12px 16px; border-radius: 8px; font-size: 15px; cursor: pointer; font-weight: 600; }
  .btn:hover { background: #4338ca; }
  .error { background: #fee2e2; color: #b91c1c; padding: 10px 12px; border-radius: 8px; margin-bottom: 14px; font-size: 14px; border: 1px solid #fecaca; }
  .footer { text-align: center; margin-top: 16px; color: #9ca3af; font-size: 12px; }
</style>
</head>
<body>
  <div class="container">
    <h2>Sign in</h2>
    <p class="sub">Use your email and password to continue.</p>

    <% String err = (String) request.getAttribute("error"); if (err != null && !err.isEmpty()) { %>
      <div class="error"><%= err %></div>     <!-- is there any error msg sent by servlet -->
    <% } %>

    <form method="post" action="<%=request.getContextPath()%>/login"> 
      <label for="email">Email</label>
      <input type="email" id="email" name="email" placeholder="you@example.com"  />

      <label for="password">Password</label>
      <input type="password" id="password" name="password" placeholder="••••••••"  />

      <button type="submit" class="btn">Login</button>
    </form>

    <div class="footer">Employee Task Tracker</div>
  </div>
</body>
</html>