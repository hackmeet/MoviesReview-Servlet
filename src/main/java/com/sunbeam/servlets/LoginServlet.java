package com.sunbeam.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sunbeam.daos.UserDao;
import com.sunbeam.daos.UserDaoImple;
import com.sunbeam.pojos.User;


@WebServlet("/login")
public class LoginServlet extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String email = req.getParameter("email");
		String password = req.getParameter("passwd");
		// business logic
		User user = null;
		boolean success = false;
		try(UserDao dao = new UserDaoImple()) {
			Optional<User> userOpt = dao.findByEmail(email);
			if(userOpt.isPresent()) {
				user = userOpt.get();
				if(password.equals(user.getPassword()))
					success = true;
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
        out.println("<title>Login</title>");
        out.println("<style>");
        out.println("body {");
        out.println("    font-family: Arial, sans-serif;");
        out.println("    background-color: #f4f4f4;");
        out.println("    text-align: center;");
        out.println("}");

        out.println("h1 {");
        out.println("    color: #333333;");
        out.println("}");

        out.println("a {");
        out.println("    text-decoration: none;");
        out.println("    color: #007BFF;");
        out.println("}");

        out.println("</style>");
        out.println("</head>");
        out.println("<body>");
        if (success)
            resp.sendRedirect("reviews"); // go to next servlet
        else {
            out.println("<h1>Invalid email or password.</h1>");
            out.println("<br/><br/>");
            out.println("<a href='index.html'>Login Again</a>");
        }
        out.println("</body>");
        out.println("</html>");
    }
}
