<%@page import="com.web.daoimpl.AdminDaoImpl"%>
<%@page import="com.web.pojo.Admin"%>

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
Admin a = new Admin();
a.setUsername(request.getParameter("name")); 
a.setPassword(request.getParameter("pass"));
a.setFullName(request.getParameter("fname"));
a.setVoterid(request.getParameter("id"));
try
{
	a.setAge(Integer.parseInt(request.getParameter("age")));
}
catch(NumberFormatException e)
{
	e.printStackTrace();
}

a.setMobile(request.getParameter("mno"));  

AdminDaoImpl daoimpl = new AdminDaoImpl();
boolean isAdded = daoimpl.addNewUser(a);

if (isAdded) {
    request.setAttribute("msg", "Registration successful! You can now login.");
%><jsp:forward page="adminlogin.jsp" /><%
} else {
    request.setAttribute("msg", "Registration failed! Username already exists.");
    %><jsp:forward page="adminregister.jsp" /><%
}
%>
</body>
</html>