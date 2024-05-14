package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import DAL.AllProductsDAL;
import DAO.AllDAO;

/**
 * Servlet implementation class SeriveceableRegionServlet
 */
@WebServlet("/UnSeriveceableRegionServlet")
public class UnSeriveceableRegionServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String pincode = request.getParameter("pincode");
		String[] productIdsAsString = request.getParameterValues("itemIds[]");

		if (productIdsAsString != null) {
			int[] productIds = new int[productIdsAsString.length];

			for (int i = 0; i < productIdsAsString.length; i++) {
				try {
					productIds[i] = Integer.parseInt(productIdsAsString[i]);
				} catch (NumberFormatException e) {
					// Handle parsing errors, if any
					e.printStackTrace(); // For example, print the error message
				}
			}
			Map<Integer, Boolean> UnServicable = new HashMap<>();

			System.out.println(pincode + " " + productIds);

			if (productIds != null) {
				for (int productId : productIds) {
					System.out.println("Product ID in cart: " + productId);
					boolean isUnServiceable = a.checkPinCode(pincode, productId);
					UnServicable.put(productId, isUnServiceable);
				}

			} else {
				System.out.println("No product IDs in cart");
			}

			Gson json = new Gson();
			String jsonData = json.toJson(UnServicable);
			response.setContentType("application/json");
			PrintWriter out = response.getWriter();
			out.print(jsonData);
			out.flush();
		}
	}
}