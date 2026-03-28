package com.web.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.web.daoimpl.ModifyVoterDaoImpl;
import com.web.pojo.ModifyVoter;

@SuppressWarnings("serial")
@WebServlet("/ModifyVoterServlet")
public class ModifyVoterServlet extends HttpServlet {
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		ModifyVoter v= new ModifyVoter();
		
		 v.setUsername(request.getParameter("username"));
		 v.setPassword(request.getParameter("password"));
		 v.setFullname(request.getParameter("full_name"));
		 try
		 {
			 v.setAge(Integer.parseInt(request.getParameter("age")));
		 }
		catch (NumberFormatException e) {
			e.printStackTrace();
		}
		 v.setMobile(request.getParameter("mobile"));
		try
		{
			v.setVoter_id(Integer.parseInt(request.getParameter("voter_id")));
		}
		catch (NumberFormatException e) 
		{
			e.printStackTrace();
		}
		 String op=request.getParameter("b1");
		 ModifyVoterDaoImpl daoimpl=new ModifyVoterDaoImpl();
		 if(op.equals("Add Voter"))
		 {
			 if(daoimpl.addVoter(v))
				 request.setAttribute("msg", "Voter Added Successfully");
			 else
			    request.setAttribute("msg", "Voter Could Not Be Added ");
		 }
		 if(op.equals("Update Voter"))
		 {
			 if(daoimpl.updateVoter(v))
				 request.setAttribute("msg", "Voter Updated Successfully");
			 else
			    request.setAttribute("msg", "Voter Could Not Be Updated ");
		 }
		 if(op.equals("Upgrade Voter"))
		 {
			 if(daoimpl.updateVoter(v))
				 request.setAttribute("msg", "Voter Updated Successfully");
			 else
			    request.setAttribute("msg", "Voter Could Not Be Updated ");
		 }
		 if(op.equals("Delete Voter"))
		 {
			 if(daoimpl.deleteVoter(v))
				 request.setAttribute("msg", "Voter Deleted Successfully");
			 else
			    request.setAttribute("msg", "Voter Could Not Be Deleted ");
		 }
		 request.setAttribute("full_name", v.getFullname());
		 RequestDispatcher rd=request.getRequestDispatcher("/modifyvoter.jsp");
		 rd.forward(request, response);
	}

}
