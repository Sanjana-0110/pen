package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAL.AllProductsDAL;
import DAO.AllDAO;

/**
 * Servlet implementation class ProductCategoryServlet
 */
@WebServlet("/cat")
public class ProductCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PrintWriter out = response.getWriter();

		try

		{
			String[] categories = a.getAllProductCategories();

			StringBuilder options = new StringBuilder();
			for (String category : categories) {
				options.append("<option value=\"").append(category).append("\">").append(category).append("</option>");
			}

			// Send the options as the response
			out.print(options.toString());
		} catch (Exception e) {
			e.printStackTrace();
			// Handle exceptions
		}
	}
}