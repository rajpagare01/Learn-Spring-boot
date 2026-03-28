package com.voting.controller;

import com.voting.dao.CandidateDAO;
import com.voting.dao.CategoryDAO;
import com.voting.dao.ElectionDAO;
import com.voting.dao.ElectionEligibilityDAO;
import com.voting.dao.UserDAO;
import com.voting.dao.VoteDAO;
import com.voting.model.Candidate;
import com.voting.model.Election;
import com.voting.model.User;
import com.voting.service.AuditService;
import com.voting.util.FileUploadUtil;
import com.voting.util.PageResult;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/admin/*")
@MultipartConfig(maxFileSize = 5_242_880L, maxRequestSize = 12_582_912L, fileSizeThreshold = 65536)
public class AdminServlet extends HttpServlet {

    private final ElectionDAO            electionDAO    = new ElectionDAO();
    private final CandidateDAO           candidateDAO   = new CandidateDAO();
    private final VoteDAO                voteDAO        = new VoteDAO();
    private final CategoryDAO            categoryDAO    = new CategoryDAO();
    private final ElectionEligibilityDAO eligibilityDAO = new ElectionEligibilityDAO();
    private final UserDAO                userDAO        = new UserDAO();

    private static final DateTimeFormatter DT_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = getPath(req);

        switch (path) {
            case "/dashboard":
            case "":
                showDashboard(req, resp);
                break;
            case "/elections":
                showElections(req, resp);
                break;
            case "/elections/new":
                req.setAttribute("categories", categoryDAO.findAll());
                req.setAttribute("voters", userDAO.findAllVoters());
                req.setAttribute("selectedCategoryIds", List.of());
                req.setAttribute("eligibleUserIds", List.of());
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
                break;
            case "/elections/edit":
                showEditElection(req, resp);
                break;
            case "/candidates":
                showCandidates(req, resp);
                break;
            case "/users":
                showUsers(req, resp);
                break;
            case "/categories":
                req.setAttribute("categories", categoryDAO.findAll());
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-categories.jsp").forward(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = getPath(req);

        User admin = (User) req.getSession().getAttribute("user");

        switch (path) {
            case "/elections/create":
                createElection(req, resp, admin);
                break;
            case "/elections/update":
                updateElection(req, resp, admin);
                break;
            case "/elections/delete":
                deleteElection(req, resp, admin);
                break;
            case "/elections/status":
                updateStatus(req, resp, admin);
                break;
            case "/elections/promote-round":
                promoteRound(req, resp, admin);
                break;
            case "/candidates/add":
                addCandidate(req, resp, admin);
                break;
            case "/candidates/update":
                updateCandidate(req, resp, admin);
                break;
            case "/candidates/delete":
                deleteCandidate(req, resp, admin);
                break;
            case "/users/toggle-active":
                toggleUserActive(req, resp, admin);
                break;
            case "/categories/add":
                addCategory(req, resp, admin);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        electionDAO.syncElectionStatuses();
        req.setAttribute("totalElections", electionDAO.countAll());
        req.setAttribute("activeElections", electionDAO.countByStatus("ACTIVE"));
        req.setAttribute("closedElections", electionDAO.countByStatus("CLOSED"));
        req.setAttribute("totalVotes", voteDAO.totalVotesAllTime());
        req.setAttribute("recentElections", electionDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }

    private void showElections(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int page = parseId(req.getParameter("page"));
        if (page < 1) page = 1;
        PageResult<Election> pr = electionDAO.findAllPaged(page, 8);
        req.setAttribute("elections", pr.getItems());
        req.setAttribute("electionPage", pr);
        req.setAttribute("categories", categoryDAO.findAll());
        req.setAttribute("voters", userDAO.findAllVoters());
        req.setAttribute("selectedCategoryIds", List.of());
        req.setAttribute("eligibleUserIds", List.of());
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
    }

    private void showEditElection(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = parseId(req.getParameter("id"));
        Election election = electionDAO.findById(id);
        if (election == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/elections");
            return;
        }
        req.setAttribute("election", election);
        req.setAttribute("categories", categoryDAO.findAll());
        req.setAttribute("selectedCategoryIds", categoryDAO.findCategoryIdsForElection(id));
        req.setAttribute("voters", userDAO.findAllVoters());
        req.setAttribute("eligibleUserIds", eligibilityDAO.findEligibleUserIds(id));
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
    }

    private void showCandidates(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int electionId = parseId(req.getParameter("electionId"));
        Election election = electionDAO.findById(electionId);
        if (election == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/elections");
            return;
        }
        List<Candidate> candidates = candidateDAO.findByElectionWithVotes(electionId);
        req.setAttribute("election", election);
        req.setAttribute("candidates", candidates);
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-candidates.jsp").forward(req, resp);
    }

    private void showUsers(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int page = parseId(req.getParameter("page"));
        if (page < 1) page = 1;
        req.setAttribute("voterPage", userDAO.findVotersPaged(page, 12));
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-users.jsp").forward(req, resp);
    }

    private void createElection(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException, ServletException {
        try {
            Election e = buildElectionFromRequest(req);
            e.setCreatedBy(admin.getId());
            e.setStatus(computeStatus(e.getStartDate(), e.getEndDate()));

            if (electionDAO.createElection(e)) {
                categoryDAO.setElectionCategories(e.getId(), parseIntArray(req.getParameterValues("categoryIds")));
                eligibilityDAO.setEligibleVoters(e.getId(), parseIntArray(req.getParameterValues("eligibleUserIds")));
                AuditService.log("ADMIN_ELECTION_CREATE", admin.getId(), "id=" + e.getId(), req);
                resp.sendRedirect(req.getContextPath() + "/admin/elections?created=true");
            } else {
                req.setAttribute("error", "Failed to create election.");
                req.setAttribute("categories", categoryDAO.findAll());
                req.setAttribute("voters", userDAO.findAllVoters());
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.setAttribute("categories", categoryDAO.findAll());
            req.setAttribute("voters", userDAO.findAllVoters());
            req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
        }
    }

    private void updateElection(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException, ServletException {
        try {
            Election e = buildElectionFromRequest(req);
            e.setId(parseId(req.getParameter("id")));
            e.setStatus(computeStatus(e.getStartDate(), e.getEndDate()));

            if (electionDAO.updateElection(e)) {
                categoryDAO.setElectionCategories(e.getId(), parseIntArray(req.getParameterValues("categoryIds")));
                eligibilityDAO.setEligibleVoters(e.getId(), parseIntArray(req.getParameterValues("eligibleUserIds")));
                AuditService.log("ADMIN_ELECTION_UPDATE", admin.getId(), "id=" + e.getId(), req);
                resp.sendRedirect(req.getContextPath() + "/admin/elections?updated=true");
            } else {
                req.setAttribute("error", "Failed to update election.");
                req.setAttribute("election", e);
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
            }
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp").forward(req, resp);
        }
    }

    private void deleteElection(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException {
        int id = parseId(req.getParameter("id"));
        electionDAO.deleteElection(id);
        AuditService.log("ADMIN_ELECTION_DELETE", admin.getId(), "id=" + id, req);
        resp.sendRedirect(req.getContextPath() + "/admin/elections?deleted=true");
    }

    private void updateStatus(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException {
        int id = parseId(req.getParameter("id"));
        String status = req.getParameter("status");
        if (List.of("UPCOMING", "ACTIVE", "CLOSED").contains(status)) {
            electionDAO.updateStatus(id, status);
            AuditService.log("ADMIN_ELECTION_STATUS", admin.getId(), "id=" + id + " " + status, req);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/elections");
    }

    private void promoteRound(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException {
        int parentId = parseId(req.getParameter("parentElectionId"));
        String title = trim(req.getParameter("newTitle"));
        String startStr = trim(req.getParameter("startDate"));
        String endStr = trim(req.getParameter("endDate"));
        if (parentId <= 0 || title.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/elections");
            return;
        }
        Election parent = electionDAO.findById(parentId);
        if (parent == null) {
            resp.sendRedirect(req.getContextPath() + "/admin/elections");
            return;
        }
        List<Candidate> top = candidateDAO.findTopByVotes(parentId, 2);
        if (top.size() < 2) {
            resp.sendRedirect(req.getContextPath() + "/admin/candidates?electionId=" + parentId);
            return;
        }

        try {
            Timestamp start = Timestamp.valueOf(LocalDateTime.parse(startStr, DT_FMT));
            Timestamp end = Timestamp.valueOf(LocalDateTime.parse(endStr, DT_FMT));
            if (!end.after(start)) {
                resp.sendRedirect(req.getContextPath() + "/admin/elections");
                return;
            }
            Election neo = new Election();
            neo.setTitle(title);
            neo.setDescription("Runoff (round " + (parent.getRoundNum() + 1) + ") from: " + parent.getTitle());
            neo.setStartDate(start);
            neo.setEndDate(end);
            neo.setCreatedBy(admin.getId());
            neo.setStatus(computeStatus(start, end));
            neo.setRoundNum(parent.getRoundNum() + 1);
            neo.setParentElectionId(parentId);

            if (electionDAO.createElection(neo)) {
                for (Candidate c : top) {
                    Candidate nc = new Candidate();
                    nc.setName(c.getName());
                    nc.setParty(c.getParty());
                    nc.setBio(c.getBio());
                    nc.setElectionId(neo.getId());
                    nc.setPhotoPath(c.getPhotoPath());
                    candidateDAO.addCandidate(nc);
                }
                eligibilityDAO.setEligibleVoters(neo.getId(),
                    eligibilityDAO.findEligibleUserIds(parentId).stream().mapToInt(i -> i).toArray());
                AuditService.log("ADMIN_PROMOTE_ROUND", admin.getId(),
                    "parent=" + parentId + " new=" + neo.getId(), req);
            }
        } catch (Exception e) {
            // redirect
        }
        resp.sendRedirect(req.getContextPath() + "/admin/elections");
    }

    private void addCandidate(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException, ServletException {
        int electionId = parseId(req.getParameter("electionId"));
        String name = trim(req.getParameter("name"));
        String party = trim(req.getParameter("party"));
        String bio = trim(req.getParameter("bio"));

        Candidate c = new Candidate();
        c.setName(name);
        c.setParty(party.isEmpty() ? "Independent" : party);
        c.setBio(bio);
        c.setElectionId(electionId);

        try {
            Part photo = req.getPart("photo");
            if (photo != null && photo.getSize() > 0) {
                String fn = photo.getSubmittedFileName();
                if (fn != null && (fn.toLowerCase().endsWith(".jpg")
                    || fn.toLowerCase().endsWith(".jpeg")
                    || fn.toLowerCase().endsWith(".png")
                    || fn.toLowerCase().endsWith(".gif"))) {
                    Path root = FileUploadUtil.uploadRoot(getServletContext());
                    try (InputStream in = photo.getInputStream()) {
                        c.setPhotoPath(FileUploadUtil.savePart(in, root, "candidates", fn));
                    }
                }
            }
        } catch (Exception ignored) {
        }

        candidateDAO.addCandidate(c);
        AuditService.log("ADMIN_CANDIDATE_ADD", admin.getId(), "election=" + electionId, req);
        resp.sendRedirect(req.getContextPath() + "/admin/candidates?electionId=" + electionId);
    }

    private void updateCandidate(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException, ServletException {
        int id = parseId(req.getParameter("id"));
        int electionId = parseId(req.getParameter("electionId"));
        String name = trim(req.getParameter("name"));
        String party = trim(req.getParameter("party"));
        String bio = trim(req.getParameter("bio"));

        Candidate existing = candidateDAO.findById(id);
        Candidate c = new Candidate();
        c.setId(id);
        c.setName(name);
        c.setParty(party.isEmpty() ? "Independent" : party);
        c.setBio(bio);
        c.setPhotoPath(existing != null ? existing.getPhotoPath() : null);

        try {
            Part photo = req.getPart("photo");
            if (photo != null && photo.getSize() > 0) {
                String fn = photo.getSubmittedFileName();
                if (fn != null && (fn.toLowerCase().endsWith(".jpg")
                    || fn.toLowerCase().endsWith(".jpeg")
                    || fn.toLowerCase().endsWith(".png")
                    || fn.toLowerCase().endsWith(".gif"))) {
                    Path root = FileUploadUtil.uploadRoot(getServletContext());
                    try (InputStream in = photo.getInputStream()) {
                        c.setPhotoPath(FileUploadUtil.savePart(in, root, "candidates", fn));
                    }
                }
            }
        } catch (Exception ignored) {
        }

        candidateDAO.updateCandidate(c);
        AuditService.log("ADMIN_CANDIDATE_UPDATE", admin.getId(), "id=" + id, req);
        resp.sendRedirect(req.getContextPath() + "/admin/candidates?electionId=" + electionId);
    }

    private void deleteCandidate(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException {
        int id = parseId(req.getParameter("id"));
        int electionId = parseId(req.getParameter("electionId"));
        candidateDAO.deleteCandidate(id);
        AuditService.log("ADMIN_CANDIDATE_DELETE", admin.getId(), "id=" + id, req);
        resp.sendRedirect(req.getContextPath() + "/admin/candidates?electionId=" + electionId);
    }

    private void toggleUserActive(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException {
        int userId = parseId(req.getParameter("userId"));
        boolean active = "true".equalsIgnoreCase(req.getParameter("active"));
        userDAO.setActive(userId, active);
        AuditService.log("ADMIN_USER_ACTIVE", admin.getId(), "user=" + userId + " active=" + active, req);
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }

    private void addCategory(HttpServletRequest req, HttpServletResponse resp, User admin)
            throws IOException {
        String name = trim(req.getParameter("name"));
        if (!name.isEmpty()) {
            categoryDAO.insert(name);
            AuditService.log("ADMIN_CATEGORY_ADD", admin.getId(), name, req);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/categories");
    }

    private String getPath(HttpServletRequest req) {
        String info = req.getPathInfo();
        return (info == null) ? "" : info;
    }

    private int parseId(String s) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private String trim(String s) {
        return (s == null) ? "" : s.trim();
    }

    private int[] parseIntArray(String[] raw) {
        if (raw == null) return new int[0];
        List<Integer> list = new ArrayList<>();
        for (String s : raw) {
            try {
                list.add(Integer.parseInt(s.trim()));
            } catch (NumberFormatException ignored) {
            }
        }
        return list.stream().mapToInt(i -> i).toArray();
    }

    private Election buildElectionFromRequest(HttpServletRequest req)
            throws IllegalArgumentException {

        String title = trim(req.getParameter("title"));
        String desc = trim(req.getParameter("description"));
        String startStr = trim(req.getParameter("startDate"));
        String endStr = trim(req.getParameter("endDate"));

        if (title.isEmpty()) throw new IllegalArgumentException("Title is required.");
        if (startStr.isEmpty() || endStr.isEmpty())
            throw new IllegalArgumentException("Start and end dates are required.");

        Timestamp start;
        Timestamp end;
        try {
            start = Timestamp.valueOf(LocalDateTime.parse(startStr, DT_FMT));
            end = Timestamp.valueOf(LocalDateTime.parse(endStr, DT_FMT));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format.");
        }

        if (!end.after(start))
            throw new IllegalArgumentException("End date must be after start date.");

        Election e = new Election();
        e.setTitle(title);
        e.setDescription(desc);
        e.setStartDate(start);
        e.setEndDate(end);
        e.setRoundNum(parseId(req.getParameter("roundNum")) > 0
            ? parseId(req.getParameter("roundNum")) : 1);
        String pid = trim(req.getParameter("parentElectionId"));
        if (!pid.isEmpty()) {
            int p = parseId(pid);
            e.setParentElectionId(p > 0 ? p : null);
        } else {
            e.setParentElectionId(null);
        }
        return e;
    }

    private String computeStatus(Timestamp start, Timestamp end) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.before(start)) return "UPCOMING";
        if (now.after(end)) return "CLOSED";
        return "ACTIVE";
    }
}
