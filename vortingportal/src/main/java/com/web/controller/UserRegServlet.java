package com.web.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.web.daoimpl.UserDaoImpl;
import com.web.pojo.User;


@WebServlet("/UserRegServlet")
public class UserRegServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
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
		try
		{
			u.setVoter_id(Integer.parseInt(request.getParameter("voter_id")));
		}
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		}

		UserDaoImpl daoimpl = new UserDaoImpl();
		boolean isValid = daoimpl.addNewUser(u);
		String target="";
		if(isValid)
		{
			request.setAttribute("msg", "<font size=2 color=green>Welcome to voter Login page</font>");
			target="/login.jsp";
			
		}
		else
		{
			request.setAttribute("msg","<font size=2 color=red>You're Already Exists</font>");
			target="/register.jsp";
		}
		RequestDispatcher rd = 
				getServletContext().getRequestDispatcher(target);
					rd.forward(request, response);
	}

}
