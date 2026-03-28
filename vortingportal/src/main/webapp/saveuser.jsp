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
u.setUsername(request.getParameter("name")); 
u.setPassword(request.getParameter("pass"));
u.setFullName(request.getParameter("fname"));

try
{
	u.setAge(Integer.parseInt(request.getParameter("age")));
}
catch(NumberFormatException e)
{
	
}

u.setMobile(request.getParameter("mno"));  

UserDaoImpl daoimpl = new UserDaoImpl();
boolean isAdded = daoimpl.addNewUser(u);

if (isAdded) {
    request.setAttribute("msg", "Registration successful! You can now login.");
    %><jsp:forward page="login.jsp" /><%
} else {
    request.setAttribute("msg", "Registration failed! Username already exists.");
    %><jsp:forward page="voter-register.jsp" /><%
}
%>
</body>
</html>