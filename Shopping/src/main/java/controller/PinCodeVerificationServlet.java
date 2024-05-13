package controller;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import DAL.AllProductsDAL;
import DAO.AllDAO;

@WebServlet("/PinCodeVerificationServlet")
public class PinCodeVerificationServlet extends HttpServlet {
	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int dp = 0, avl = 0;
		boolean productAvailable = false;
		int pid = 0;
		// Read the JSON data sent from the client
		BufferedReader reader = request.getReader();
		StringBuilder jsonStringBuilder = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			jsonStringBuilder.append(line);
		}

		// Convert the JSON data to a JSON object
		JsonObject jsonObject = new JsonParser().parse(jsonStringBuilder.toString()).getAsJsonObject();

		// Get the pincode and cart data from the JSON object
		String pincode = jsonObject.get("pincode").getAsString();
		JsonArray cartArray = jsonObject.getAsJsonArray("cart");

		// Now you can iterate through the cart data to access each product's pid
		for (JsonElement element : cartArray) {
			JsonObject cartItem = element.getAsJsonObject();
			pid = cartItem.get("pid").getAsInt(); // Accessing the pid
			// Do whatever you need with the pid
		}
		System.out.println("pid:" + pid);

		// Example: Send a JSON response back to the client
		JsonObject jsonResponse = new JsonObject();
		jsonResponse.addProperty("message", "Pincode received successfully");
		// Add more properties as needed
		System.out.println(pincode);
		dp = a.checkDeliverablePincode(pincode);
		avl = a.checkavailability(dp, pid);

		if (avl == pid) {
			productAvailable = true;
		}

		// Example: Send a JSON response back to the client
		JsonObject jsonResponse1 = new JsonObject();
		jsonResponse1.addProperty("message", "Pincode received successfully");
		jsonResponse1.addProperty("productAvailable", productAvailable); // Add product availability flag
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonResponse1.toString());

	}

}
