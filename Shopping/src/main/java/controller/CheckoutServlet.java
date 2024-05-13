package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import BLL.BussinessLogic;
import DAL.AllProductsDAL;
import DAO.AllDAO;
import model.CartList;
import model.ProductInfo;
import model.ProductPriceDetails;

@WebServlet("/CheckoutServlet")
public class CheckoutServlet extends HttpServlet {
	private AllDAO a;

	public void init() {
		a = new AllProductsDAL();
	}

	@Override

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<ProductPriceDetails> lpd=new ArrayList<>();
		
        int p=-1,q=-1,r=-1;
		BussinessLogic b=new BussinessLogic();
		HttpSession session = request.getSession();
		String cartJson = (String) session.getAttribute("cart");
		String oid = null;
		if (cartJson != null) {
			try {
				JSONObject cartData = new JSONObject(cartJson);
				JSONArray items = cartData.getJSONArray("items");
				HashMap<Integer, ProductInfo> productMap = new HashMap<>();
				CartList cartList = new CartList();

				for (int i = 0; i < items.length(); i++) {
					JSONObject item = items.getJSONObject(i);
					int pid = item.getInt("pid");
					String pname = item.getString("pname");
					double price = item.getDouble("price");
					String hsncode = item.getString("hsncode");

					int pcid = item.optInt("pcid", 1);

					ProductInfo info = productMap.getOrDefault(pid,
							new ProductInfo(pid, pname, price, hsncode, pcid, 0, 0.0));
					info.incrementQuantity();
					info.setPrice(info.getQty() * price);
					info.setGst(b.findgst(hsncode));
					productMap.put(pid, info);
				}

				productMap.values().forEach(cartList::addProducts);
				String u = (String) session.getAttribute("username");
				System.out.println(u);
				// Calculate total price for the cart
				double totalPrice = 0;
				int numberOfProducts = cartList.getAllProducts().size();
				session.setAttribute("size", numberOfProducts);
				double[] pricesWithGST = new double[numberOfProducts];
				double[] gstPercentages = new double[numberOfProducts];
				int[] quantities = new int[numberOfProducts];
				int []psid=new int[numberOfProducts];
				int []sqty=new int[numberOfProducts];
				int[] discount=new int[numberOfProducts];
				int si=-1,sq=-1,d=-1;
				List<ProductInfo> pi=cartList.getAllProducts();
				session.setAttribute("productinfo",pi);
				for (ProductInfo product : cartList.getAllProducts()) {
					System.out.println(product.getPrice()+" "+product.getGst()+" "+product.getQty());
					psid[++si]=product.getPid();
					sqty[++sq]=product.getQty();
					pricesWithGST[++p]=product.getPrice();
					gstPercentages[++q]=product.getGst();
					quantities[++r]=product.getQty();
					discount[++d]=a.getdiscount(product.getPid());
					
				}
				
				for(int i=0;i<pricesWithGST.length;i++) {
					System.out.println(pricesWithGST[i]+" "+gstPercentages[i]+" "+quantities[i]);
					System.out.println(psid[i]+" "+sqty[i]);
					System.out.println("discount "+i+":"+discount[i]);
				}
				session.setAttribute("psid", psid);
				session.setAttribute("sqty", sqty);
				lpd = b.calculateTotalPrice(pricesWithGST,gstPercentages,quantities,discount);
//				System.out.println("Pid: " + product.getPid() + ", Pcid: " + product.getPcid() + ", Product Name: "
//						+ product.getPname() + ", Price: $" + product.getPrice() + ", HSN Code: "
//						+ product.getHsncode() + ", Quantity: " + product.getQty() + ", GST: " + product.getGst()
//						+ "%");
				for(ProductPriceDetails ppd:lpd) {
					totalPrice=ppd.getTotalprice();
				}
				
				System.out.println("Total Cart Price: $" + totalPrice);
				int cid = a.getcid(u);
				session.setAttribute("cid", cid);
				
				oid = a.generateOrderID();
				System.out.println("oid:"+oid);
				session.setAttribute("oid", oid);
				session.setAttribute("CartList", cartList);
				List<ProductInfo> op=a.getOrderedProducts(oid);
				System.out.println("ProductInfo");

				
				session.setAttribute("productlistdetails", lpd);
				session.setAttribute("totalprice", totalPrice);
				
				
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
	            request.getRequestDispatcher("checkout.jsp").forward(request, response);

			} catch (Exception e) {
				e.printStackTrace();
				response.getWriter().write("Error processing cart data");
			}
		} else {
			response.getWriter().write("No cart data found in session");
		}
	}
}