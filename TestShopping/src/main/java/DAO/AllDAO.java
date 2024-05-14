package DAO;

import java.sql.SQLException;
import java.util.List;

import model.Product;
import model.ProductInfo;

public interface AllDAO {
	List<Product> getAllProducts() throws ClassNotFoundException;

	String[] getAllProductCategories();

	List<Product> getProductsByCategory(String categoryId);

	int checkDeliverablePincode(String pincode);

	int checkavailability(int c, int p);

	String generateOrderID() throws SQLException;

	int getcid(String u);

	void insertOrders(int cid, String oid, double totalPrice, int cpid);

	void insertOrderedProducts(List<ProductInfo> products, String oid);

	double getTotalPrice(String oid);

	List<ProductInfo> getOrderedProducts(String oid);

	double getShipping(double t);

	public int getStock(int pid);

	public void decreasestock(int[] psid, int[] sqty);

	public void updatestock(int psid, int sqty);

	public int getdiscount(int pid);

	public boolean CheckValidCoupon(String dcode, double total);

	public void UpdateCoupon(String dcode);

	public int getCouponID(String dcode);

	public double getCouponAmount(int cpid);

	public double getCashBack(double total);

	public boolean checkPinCode(String pincode, int productId);

}
