package com.voting.controller;

import com.voting.dao.CandidateDAO;
import com.voting.dao.ElectionDAO;
import com.voting.dao.VoteDAO;
import com.voting.model.Candidate;
import com.voting.model.Election;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * AdminServlet — handles all admin actions via the "action" request parameter.
 *
 * GET  /admin/dashboard               → show admin dashboard
 * GET  /admin/elections               → list all elections
 * GET  /admin/elections/new           → show create election form
 * POST /admin/elections/create        → create election
 * GET  /admin/elections/edit?id=X     → show edit election form
 * POST /admin/elections/update        → update election
 * POST /admin/elections/delete        → delete election
 * POST /admin/elections/status        → update election status
 * GET  /admin/candidates?electionId=X → list candidates for election
 * POST /admin/candidates/add          → add candidate
 * POST /admin/candidates/update       → update candidate
 * POST /admin/candidates/delete       → delete candidate
 */
@WebServlet("/admin/*")
public class AdminServlet extends HttpServlet {

    private final ElectionDAO  electionDAO  = new ElectionDAO();
    private final CandidateDAO candidateDAO = new CandidateDAO();
    private final VoteDAO      voteDAO      = new VoteDAO();

    private static final DateTimeFormatter DT_FMT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");

    // ── Routing ───────────────────────────────────────────

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
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
                   .forward(req, resp);
                break;
            case "/elections/edit":
                showEditElection(req, resp);
                break;
            case "/candidates":
                showCandidates(req, resp);
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

        switch (path) {
            case "/elections/create":
                createElection(req, resp);
                break;
            case "/elections/update":
                updateElection(req, resp);
                break;
            case "/elections/delete":
                deleteElection(req, resp);
                break;
            case "/elections/status":
                updateStatus(req, resp);
                break;
            case "/candidates/add":
                addCandidate(req, resp);
                break;
            case "/candidates/update":
                updateCandidate(req, resp);
                break;
            case "/candidates/delete":
                deleteCandidate(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        }
    }

    // ── GET handlers ──────────────────────────────────────

    private void showDashboard(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Sync statuses from actual dates first
        electionDAO.syncElectionStatuses();

        req.setAttribute("totalElections", electionDAO.countAll());
        req.setAttribute("activeElections", electionDAO.countByStatus("ACTIVE"));
        req.setAttribute("closedElections", electionDAO.countByStatus("CLOSED"));
        req.setAttribute("totalVotes",      voteDAO.totalVotesAllTime());
        req.setAttribute("recentElections", electionDAO.findAll());

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp")
           .forward(req, resp);
    }

    private void showElections(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("elections", electionDAO.findAll());
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
           .forward(req, resp);
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
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
           .forward(req, resp);
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
        req.setAttribute("election",   election);
        req.setAttribute("candidates", candidates);
        req.getRequestDispatcher("/WEB-INF/views/admin/manage-candidates.jsp")
           .forward(req, resp);
    }

    // ── POST handlers — Elections ─────────────────────────

    private void createElection(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            Election e = buildElectionFromRequest(req);

            com.voting.model.User admin =
                (com.voting.model.User) req.getSession().getAttribute("user");
            e.setCreatedBy(admin.getId());
            e.setStatus(computeStatus(e.getStartDate(), e.getEndDate()));

            if (electionDAO.createElection(e)) {
                resp.sendRedirect(req.getContextPath()
                                  + "/admin/elections?created=true");
            } else {
                req.setAttribute("error", "Failed to create election.");
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
                   .forward(req, resp);
            }
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
               .forward(req, resp);
        }
    }

    private void updateElection(HttpServletRequest req, HttpServletResponse resp)
            throws IOException, ServletException {
        try {
            Election e = buildElectionFromRequest(req);
            e.setId(parseId(req.getParameter("id")));
            e.setStatus(computeStatus(e.getStartDate(), e.getEndDate()));

            if (electionDAO.updateElection(e)) {
                resp.sendRedirect(req.getContextPath()
                                  + "/admin/elections?updated=true");
            } else {
                req.setAttribute("error", "Failed to update election.");
                req.setAttribute("election", e);
                req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
                   .forward(req, resp);
            }
        } catch (IllegalArgumentException ex) {
            req.setAttribute("error", ex.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/manage-elections.jsp")
               .forward(req, resp);
        }
    }

    private void deleteElection(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id = parseId(req.getParameter("id"));
        electionDAO.deleteElection(id);
        resp.sendRedirect(req.getContextPath() + "/admin/elections?deleted=true");
    }

    private void updateStatus(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int    id     = parseId(req.getParameter("id"));
        String status = req.getParameter("status");
        if (List.of("UPCOMING", "ACTIVE", "CLOSED").contains(status)) {
            electionDAO.updateStatus(id, status);
        }
        resp.sendRedirect(req.getContextPath() + "/admin/elections");
    }

    // ── POST handlers — Candidates ────────────────────────

    private void addCandidate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int    electionId = parseId(req.getParameter("electionId"));
        String name       = trim(req.getParameter("name"));
        String party      = trim(req.getParameter("party"));
        String bio        = trim(req.getParameter("bio"));

        Candidate c = new Candidate();
        c.setName(name);
        c.setParty(party.isEmpty() ? "Independent" : party);
        c.setBio(bio);
        c.setElectionId(electionId);

        candidateDAO.addCandidate(c);
        resp.sendRedirect(req.getContextPath()
                          + "/admin/candidates?electionId=" + electionId);
    }

    private void updateCandidate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int    id         = parseId(req.getParameter("id"));
        int    electionId = parseId(req.getParameter("electionId"));
        String name       = trim(req.getParameter("name"));
        String party      = trim(req.getParameter("party"));
        String bio        = trim(req.getParameter("bio"));

        Candidate c = new Candidate();
        c.setId(id);
        c.setName(name);
        c.setParty(party.isEmpty() ? "Independent" : party);
        c.setBio(bio);

        candidateDAO.updateCandidate(c);
        resp.sendRedirect(req.getContextPath()
                          + "/admin/candidates?electionId=" + electionId);
    }

    private void deleteCandidate(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        int id         = parseId(req.getParameter("id"));
        int electionId = parseId(req.getParameter("electionId"));
        candidateDAO.deleteCandidate(id);
        resp.sendRedirect(req.getContextPath()
                          + "/admin/candidates?electionId=" + electionId);
    }

    // ── Helpers ───────────────────────────────────────────

    private String getPath(HttpServletRequest req) {
        String info = req.getPathInfo();
        return (info == null) ? "" : info;
    }

    private int parseId(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }

    private String trim(String s) { return (s == null) ? "" : s.trim(); }

    private Election buildElectionFromRequest(HttpServletRequest req)
            throws IllegalArgumentException {

        String title    = trim(req.getParameter("title"));
        String desc     = trim(req.getParameter("description"));
        String startStr = trim(req.getParameter("startDate"));
        String endStr   = trim(req.getParameter("endDate"));

        if (title.isEmpty()) throw new IllegalArgumentException("Title is required.");
        if (startStr.isEmpty() || endStr.isEmpty())
            throw new IllegalArgumentException("Start and end dates are required.");

        Timestamp start, end;
        try {
            start = Timestamp.valueOf(LocalDateTime.parse(startStr, DT_FMT));
            end   = Timestamp.valueOf(LocalDateTime.parse(endStr,   DT_FMT));
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
        return e;
    }

    private String computeStatus(Timestamp start, Timestamp end) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (now.before(start)) return "UPCOMING";
        if (now.after(end))    return "CLOSED";
        return "ACTIVE";
    }
}
