<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Error</title>
<style>
  body { font-family: Arial, sans-serif; background:#f8fafc; margin:0; }
  .container { max-width: 640px; margin: 80px auto; background:#fff; border:1px solid #e5e7eb; border-radius:12px; padding:24px; text-align:center; }
  .msg { color:#991b1b; background:#fee2e2; border:1px solid #fecaca; padding:12px; border-radius:8px; }
  a { display:inline-block; margin-top:12px; text-decoration:none; background:#2563eb; color:#fff; padding:10px 14px; border-radius:8px; }
</style>
</head>
<body>
  <div class="container">
    <h2>Something went wrong</h2>
    <div class="msg">${requestScope.error != null ? requestScope.error : 'An unexpected error occurred.'}</div>
    <a href="<%=request.getContextPath()%>/login">Back to Login</a>
  </div>
</body>
</html>