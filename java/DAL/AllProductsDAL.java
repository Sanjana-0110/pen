package DAL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import DAO.AllDAO;
import dbcon.DBConnectionManager;
import model.CartList;
import model.Product;
import model.ProductInfo;

public class AllProductsDAL implements AllDAO {

	@Override
	public List<Product> getAllProducts() {
		List<Product> products = new ArrayList<>();
		String s = "SELECT * FROM products2003";
		try {
			Connection connection = DBConnectionManager.getConnection();

			PreparedStatement preparedStatement = connection.prepareStatement(s);
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setPid(rs.getInt("pid"));
				product.setPcid(rs.getInt("pcid"));
				product.setPname(rs.getString("pname"));
				product.setPrice(rs.getDouble("price"));
				product.setHsncode(rs.getString("hsncode"));
				product.setImage(rs.getString("image"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	// Implement other methods of the AllDAO interface as needed
	public String[] getAllProductCategories() {
		String[] categories = null;
		try {
			Connection conn = DBConnectionManager.getConnection();

			PreparedStatement stmt = conn.prepareStatement("SELECT pcname FROM product_category2003");
			ResultSet rs = stmt.executeQuery();

			List<String> categoryList = new ArrayList<>();
			while (rs.next()) {
				String pcname = rs.getString("pcname");
				categoryList.add(pcname);
			}

			categories = categoryList.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return categories;
	}

	public List<Product> getProductsByCategory(String categoryId) {
		List<Product> products = new ArrayList<>();

		try {
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT p.* FROM products2003 p JOIN product_category2003 pc ON p.pcid = pc.pcid WHERE pc.pcname = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, categoryId);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Product product = new Product();
				product.setPid(rs.getInt("pid"));
				product.setPcid(rs.getInt("pcid"));
				product.setPname(rs.getString("pname"));
				product.setPrice(rs.getDouble("price"));
				product.setHsncode(rs.getString("hsncode"));
				product.setImage(rs.getString("image"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	public int checkDeliverablePincode(String pincode) {
		try {
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT srrg_id FROM ServiceableRegions2003 WHERE ? BETWEEN srrg_pinfrom AND srrg_pinto";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, pincode);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int count = rs.getInt(1);
				System.out.println(count);
				return count;
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int checkavailability(int c, int p) {
		try {
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT prct_id FROM ProductCategoryWiseServiceableRegions2003 WHERE srrg_id=? AND prct_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, c);
			stmt.setInt(2, p);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int pid = rs.getInt(1);
				System.out.println(pid);
				System.out.println("pid" + pid);
				return pid;
			}

			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public String generateOrderID() throws SQLException {
		String orderID = null;
		try {
			Connection conn = DBConnectionManager.getConnection();
			PreparedStatement stmt = conn.prepareStatement("select generate_orderid2003()");
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) { // Move the cursor to the first row
				orderID = rs.getString(1); // Retrieve the result from the first column
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return orderID;
	}

	public int getcid(String username) {
		int cid = 0;
		ResultSet rs = null;

		// Get customer ID based on username
		try {
			Connection conn = DBConnectionManager.getConnection();

			String sql1 = "SELECT cid FROM customer2003 WHERE username=?";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, username);
			rs = st.executeQuery();
			if (rs.next()) {
				cid = rs.getInt("cid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cid;
	}

	public void insertOrders(int cid, String oid, double totalPrice) {
		ResultSet rs = null;

		// Get customer ID based on username
		try {
			Connection conn = DBConnectionManager.getConnection();

			String sql1 = "INSERT INTO orders2003 VALUES (?, ?, ?, ?)";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, oid);
			st.setString(2, "order " + oid);
			st.setDouble(3, totalPrice);
			st.setInt(4, cid);
			st.executeUpdate();
			rs = st.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void insertOrderedProducts(CartList cartList, String oid) {
		ResultSet rs = null;
		List<ProductInfo> products = cartList.getAllProducts();

		// Get customer ID based on username
		try {
			Connection conn = DBConnectionManager.getConnection();

			String sql1 = "INSERT INTO ordered_products2003 VALUES (?, ?, ?, ?, ?, ?)";
			PreparedStatement stmt = conn.prepareStatement(sql1);
			for (ProductInfo product : products) {
				stmt.setString(1, oid);
				stmt.setInt(2, product.getPid());
				stmt.setInt(3, product.getQty());
				stmt.setDouble(4, product.getPrice());
				stmt.setString(5, product.getPname());
				stmt.setDouble(6, product.getGst());
				stmt.addBatch();
			}
			stmt.executeBatch();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
