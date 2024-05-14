package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAL.AllProductsDAL;
import DAO.AllDAO;
import model.CartList;
import model.ProductInfo;
import model.ProductPriceDetails;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();

		String oid = (String) session.getAttribute("oid");
		System.out.println("oid:"+oid);
		CartList cartList = new CartList();
		List<ProductInfo> pi=(List<ProductInfo>) session.getAttribute("productinfo");
		int[] psid = (int[]) session.getAttribute("psid");
		int[] sqty = (int[]) session.getAttribute("sqty");
		int cpid = (int) session.getAttribute("cpid"); 
		double totalPrice=0.0;
		
		int cid=(int) session.getAttribute("cid");
	    List<ProductPriceDetails> lpd =(List<ProductPriceDetails>) session.getAttribute("productlistdetails");
	    for(ProductPriceDetails ppd:lpd) {
			totalPrice=ppd.getTotalprice();
		}


		a.insertOrders(cid, oid, totalPrice,cpid);
		a.insertOrderedProducts(pi, oid);
		a.decreasestock(psid, sqty);

        request.getRequestDispatcher("final.jsp").forward(request, response);
	}


}
