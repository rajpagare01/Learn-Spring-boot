<%@page import="com.web.daoimpl.UserDaoImpl"%>
<%@page import="com.web.pojo.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
User u = new User();
u.setUsername(request.getParameter("email"));
u.setPassword(request.getParameter("pass"));
UserDaoImpl daoimpl = new UserDaoImpl();
boolean isValid = daoimpl.checkUserCredential(u);
	if(isValid==true)
	{
		request.setAttribute("msg","<font size=2 color=green>Welcome To MatdaanPortal</font>");
		%>
		<jsp:forward page="index-2.jsp"></jsp:forward>
		<%
	}
	else
	{
		request.setAttribute("msg","<font size=2 color=red>Invalid Username / Password</font>");
		%>
		<jsp:forward page="login.jsp"></jsp:forward>
		<%
	}

%>
</body>
</html>