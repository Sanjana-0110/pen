package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import DAL.AllProductsDAL;
import DAO.AllDAO;

@WebServlet("/StockCheckServlet")
public class StockCheckServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private AllDAO a;

    public void init() {
        a = new AllProductsDAL();
    }

 // Inside StockCheckServlet

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get product ID from request parameter
        String productId = request.getParameter("pid");
        int pid = Integer.parseInt(productId);
        System.out.println("pid:" + pid);

        int stock = 0;
        stock = a.getStock(pid);

        // Set response content type
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        // Create a JSON object to hold the response data
        JSONObject jsonResponse = new JSONObject();
        if (stock > 0) {
            // If stock is available
            jsonResponse.put("stock", stock);
        } else {
            // If stock is not available, set an error message
            String message = "The following item is not available: " + pid + ". We will let you know once it is available.";
            jsonResponse.put("error", message);
        }

        // Write the JSON response to the PrintWriter
        out.println(jsonResponse.toString());
    }

}
