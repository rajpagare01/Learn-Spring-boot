package com.web.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.web.daoimpl.CandidateDaoImpl;
import com.web.pojo.candidate;

@WebServlet("/CandidateServlet")
@MultipartConfig
public class CandidateServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        candidate c = new candidate();

        c.setFullname(request.getParameter("full_name"));
        try {
            c.setAge(Integer.parseInt(request.getParameter("age")));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            c.setAge(0); 
        }
        c.setPartyname(request.getParameter("party_name"));
        
        String electionType = request.getParameter("election_type");
        c.setElection_type(electionType);

        try {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                c.setId(Integer.parseInt(idStr));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        CandidateDaoImpl daoimpl = new CandidateDaoImpl();

        Part filePart = request.getPart("photo");
        String imageFileName = null;
        String existingPhoto = request.getParameter("existing_photo"); // existing photo filename hidden field

        if (filePart != null && filePart.getSize() > 0) {
            imageFileName = Path.of(filePart.getSubmittedFileName()).getFileName().toString();
            c.setPhoto(imageFileName);

            String uploadDir = request.getServletContext().getRealPath("/images");
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            Path filePath = Path.of(uploadDir, imageFileName);
            try (InputStream input = filePart.getInputStream()) {
                Files.copy(input, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                request.setAttribute("msg", "Failed to upload image file.");
            }
        } else {
            c.setPhoto(existingPhoto);
        }

        String op = request.getParameter("b2");

        if (op != null) {
            switch (op) {
                case "Add Candidate":
                    boolean added = daoimpl.addcandidate(c);
                    if (added) {
                        request.getSession().setAttribute("msg", "Candidate Added Successfully");
                    } else {
                        request.getSession().setAttribute("msg", "Candidate Could Not Be Added");
                    }
                    break;

                case "Update Candidate":
                    boolean updated = daoimpl.updatecandidate(c);
                    if (updated) {
                        request.getSession().setAttribute("msg", "Candidate Updated Successfully");
                    } else {
                        request.getSession().setAttribute("msg", "Candidate Could Not Be Updated");
                    }
                    break;

                case "Delete Candidate":
                    if (c.getPhoto() != null && !c.getPhoto().isEmpty()) {
                        String uploadDir = request.getServletContext().getRealPath("/images");
                        File photoFile = new File(uploadDir, c.getPhoto());
                        if (photoFile.exists()) {
                            photoFile.delete();
                        }
                    }
                    boolean deleted = daoimpl.deletecandidate(c);
                    if (deleted) {
                        request.getSession().setAttribute("msg", "Candidate Deleted Successfully");
                    } else {
                        request.getSession().setAttribute("msg", "Candidate Could Not Be Deleted");
                    }
                    break;

                default:
                    request.getSession().setAttribute("msg", "Invalid Operation");
                    break;
            }
        } else {
            request.getSession().setAttribute("msg", "Operation Not Specified");
        }

        response.sendRedirect("candidatemanagement.jsp");
    }
}
