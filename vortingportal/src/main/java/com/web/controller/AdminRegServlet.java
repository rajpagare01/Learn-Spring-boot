package com.web.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.daoimpl.AdminDaoImpl;
import com.web.pojo.Admin;


@WebServlet("/AdminRegServlet")
public class AdminRegServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		Admin a= new Admin();
		a.setUsername(request.getParameter("name")); 
		a.setPassword(request.getParameter("pass"));
		a.setFullName(request.getParameter("fname"));
		
		try
		{
			a.setAge(Integer.parseInt(request.getParameter("age")));
		}
		catch(NumberFormatException e)
		{
			
		}

		a.setMobile(request.getParameter("mno"));  
		a.setAdminid(request.getParameter("adminid"));

		AdminDaoImpl daoimpl = new AdminDaoImpl();
		boolean isValid = daoimpl.addNewUser(a);
		String target="";
		if(isValid)
		{
			request.setAttribute("msg", "<font size=2 color=green>Welcome to Admin Login page</font>");
			target="/adminlogin.jsp";
			
		}
		else
		{
			request.setAttribute("msg","<font size=2 you are already register  </font>");
			target="/adminregister.jsp";
		}
		RequestDispatcher rd = 
				getServletContext().getRequestDispatcher(target);
					rd.forward(request, response);

	}

}
