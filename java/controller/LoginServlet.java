package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dbcon.DBConnectionManager;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	String verify = "no";

	public LoginServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		javax.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		HttpSession session = request.getSession();
		session.setAttribute("username", username);

		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {
			conn = DBConnectionManager.getConnection();
			response.setContentType("text/plain");
			String sql = "SELECT username, password FROM customer2003 WHERE username = ? AND password = ?";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, username);
			stmt.setString(2, password);
			rs = stmt.executeQuery();

			if (rs.next()) {
				String storedPassword = rs.getString("password");
				if (password.equals(storedPassword)) {
					verify = "yes";
					session.setAttribute("loggedCustomer", "true");
				}
			} else {
				verify = "no";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		PrintWriter pw = response.getWriter();
		pw.print(verify);
	}
}
