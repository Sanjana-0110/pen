package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import DAL.AllProductsDAL;
import DAO.AllDAO;

/**
 * Servlet implementation class CouponServlet
 */
@WebServlet("/CouponServlet")
public class CouponServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		int cpid = 0;
		double price = 0.0;
		double total = 0.0;
		total = (Double) session.getAttribute("totalcp");
		System.out.println("total:" + total);
		String dcode = request.getParameter("coupon");
		System.out.println("dcode:" + dcode);
		boolean couponApplied = false;
		couponApplied = a.CheckValidCoupon(dcode, total);
		if (couponApplied) {
			// Coupon applied successfully
			cpid = a.getCouponID(dcode);
			price = a.getCouponAmount(cpid);
			session.setAttribute("cpid", cpid);
			session.setAttribute("cprice", price);
			PrintWriter out = response.getWriter();
			out.println("Coupon applied successfully!");
		} else {
			// Coupon invalid
			PrintWriter out = response.getWriter();
			out.println("Invalid coupon");
		}

	}

}
