package com.web.controller;

import java.io.IOException;
import java.rmi.ServerException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.web.daoimpl.AdminDaoImpl;
import com.web.pojo.Admin;
@SuppressWarnings("serial")
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet
{
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServerException ,IOException, ServletException
{
	Admin a= new Admin();
	a.setUsername(request.getParameter("username"));
	a.setPassword(request.getParameter("password"));
	AdminDaoImpl daoimpl=new AdminDaoImpl();
	boolean isValid =daoimpl.checkUserCredential(a);
	String target="";
	if(isValid)
	{
		request.setAttribute("username", a.getUsername());
		target="/admin.jsp";
		HttpSession session = request.getSession(false);  

       if(session!=null)
       {
    	   session.invalidate();
       }
       session=request.getSession(true);
       session.setAttribute("username", a.getUsername());
		
	}
	else
	{
		request.setAttribute("msg","<font size=2 color=red>invalid username/password</font>");
		target="/adminlogin.jsp";
	}
	RequestDispatcher rd = 
			getServletContext().getRequestDispatcher(target);
				rd.forward(request, response);
}

}
