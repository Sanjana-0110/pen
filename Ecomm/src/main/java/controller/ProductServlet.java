package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import DAL.AllProductsDAL;
import DAO.AllDAO;
import model.Product;

@WebServlet("/products")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get the selected category ID from the request parameter
		String categoryId = request.getParameter("categoryId");

		// Initialize StringBuilder for JSON output
		StringBuilder jsonBuilder = new StringBuilder();
		jsonBuilder.append("[");

		// Initialize another StringBuilder for logging or debugging
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append("[");

		// Call the DAO method to fetch products based on the category ID
		List<Product> products = a.getProductsByCategory(categoryId);

		for (int i = 0; i < products.size(); i++) {
			Product product = products.get(i);
			System.out.println(product.getPcid());
			jsonBuilder.append("{").append("\"pid\":").append(product.getPid()).append(",\"pcid\":")
					.append(product.getPcid()).append(",\"pname\":\"").append(product.getPname())
					.append("\",\"price\":").append(product.getPrice()).append(",\"hsncode\":\"")
					.append(product.getHsncode()).append("\",\"image\":\"").append(product.getImage()).append("\"}");
			if (i < products.size() - 1) {
				jsonBuilder.append(",");
				logBuilder.append(",");
			}

			// Adding product details to log builder
			logBuilder.append("Product ").append(i + 1).append(": ").append(product.getPname()).append(" ($")
					.append(product.getPrice()).append(")\n");
		}

		jsonBuilder.append("]");

		// Log the constructed log information (for example, to console or file)

		// Set content type and write the JSON response
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		out.println(jsonBuilder.toString());
	}

}
