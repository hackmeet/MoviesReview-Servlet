package com.sunbeam.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunbeam.daos.ReviewDao;
import com.sunbeam.daos.ReviewDaoImple;
import com.sunbeam.pojos.Review;
@WebServlet("/reviews")
public class ReviewServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        processRequest(req, resp);
    }

    protected void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String viewType = req.getParameter("viewType"); 

        // business logic
        List<Review> list = new ArrayList<>();
        try (ReviewDao dao = new ReviewDaoImple()) {
            if ("myReviews".equals(viewType)) {
                // Load user-specific reviews
                int userId = getUserIdFromSessionOrRequest(req);
                list = dao.findReviewsById(userId);
            } else if ("sharedReviews".equals(viewType)) {
                // Load shared reviews
                int userId = getUserIdFromSessionOrRequest(req);
                list = dao.findSharedReviews(userId);
            } else {
                // Load all reviews by default
                list = dao.findAll();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException(e);
        }

        // presentation logic
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>Reviews</title>");
        out.println("<style>");
        out.println("table {");
        out.println("    border-collapse: collapse;");
        out.println("    width: 100%;");
        out.println("}");

        out.println("th, td {");
        out.println("    border: 1px solid #dddddd;");
        out.println("    text-align: left;");
        out.println("    padding: 8px;");
        out.println("}");

        out.println("tr:nth-child(even) {");
        out.println("    background-color: #f2f2f2;");
        out.println("}");
        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        out.println("Hello, User! <hr/>");

        // Links to different views
        out.println("<a href='reviews'>All Reviews</a> | ");
        out.println("<a href='reviews?viewType=myReviews'>My Reviews</a> | ");
        out.println("<a href='reviews?viewType=sharedReviews'>Shared Reviews</a><br/>");

        // Table heading
        out.println("<h1>Reviews</h1>");
        out.println("<table>");
        out.println("<tr>");
        out.println("<th>id</th>");
        out.println("<th>movie</th>");
        out.println("<th>rating</th>");
        out.println("<th>review</th>");
        out.println("<th>action</th>");
        out.println("</tr>");

        // Display reviews in the table
        for (Review r : list) {
            out.println("<tr>");
            out.printf("<td>%s</td>", r.getId());
            out.printf("<td>%s</td>", r.getMovieName());
            out.printf("<td>%s</td>", r.getRating());
            out.printf("<td>%s</td>", r.getReview());
            out.printf("<td></td>");
            out.printf("</tr>");
        }

        out.println("</table>");
        out.println("<a href='reviews'>Add Review</a> <tr><tr> ");
        out.println("<div style='position: absolute; top: 10px; right: 10px;'>");
        out.println("<a href='logout'>Logout</a>");
        out.println("</div>");        
        out.println("</body>");
        out.println("</html>");
    }

    private int getUserIdFromSessionOrRequest(HttpServletRequest req) {
        // Implement your logic to get the user ID from the session or request
        return 1; 
    }
}