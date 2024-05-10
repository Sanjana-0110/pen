package DAO;

import java.sql.SQLException;
import java.util.List;

import model.CartList;
import model.Product;

public interface AllDAO {
	List<Product> getAllProducts() throws ClassNotFoundException;

	String[] getAllProductCategories();

	List<Product> getProductsByCategory(String categoryId);

	int checkDeliverablePincode(String pincode);

	int checkavailability(int c, int p);

	String generateOrderID() throws SQLException;

	int getcid(String u);

	void insertOrders(int cid, String oid, double totalPrice);

	void insertOrderedProducts(CartList cartList, String oid);
}
