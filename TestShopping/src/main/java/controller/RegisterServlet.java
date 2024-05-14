package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import dbcon.DBConnectionManager;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public RegisterServlet() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		javax.servlet.RequestDispatcher dispatcher = request.getRequestDispatcher("login.jsp");
		dispatcher.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html");
		String name = request.getParameter("name");
		String mobile = request.getParameter("mobile");
		String location = request.getParameter("location");
		String address = request.getParameter("address");
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Connection conn = null;
		PreparedStatement stmt = null;

		try {
			conn = DBConnectionManager.getConnection();
			String sql = "INSERT INTO customer2003 (name, mobile, location, address, username, password) VALUES (?, ?, ?, ?, ?, ?)";
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, name);
			stmt.setString(2, mobile);
			stmt.setString(3, location);
			stmt.setString(4, address);
			stmt.setString(5, username);
			stmt.setString(6, password);
			stmt.executeUpdate();
			response.sendRedirect("index.jsp");
		} catch (SQLException e) {
			e.printStackTrace();
			response.getWriter().write("Error processing registration");
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
