package DAL;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import DAO.AllDAO;
import dbcon.DBConnectionManager;
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

	public double getTotalPrice(String oid) {
		double t = 0.0;
		ResultSet rs = null;

		// Get customer ID based on username
		try {
			Connection conn = DBConnectionManager.getConnection();

			String sql1 = "SELECT oname, totalprice FROM orders2003 WHERE oid = ?";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, oid);
			rs = st.executeQuery();
			if (rs.next()) {
				t = rs.getDouble("totalprice");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return t;
	}

	public double getShipping(double t) {
		ResultSet rs = null;
		double s = 0.0;
		// Get customer ID based on username
		try {
			Connection conn = DBConnectionManager.getConnection();

			String sql1 = "SELECT orvl_shippingamount FROM OrderValueWiseShippingCharges2003 WHERE ? BETWEEN orvl_from AND orvl_to";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setDouble(1, t);
			rs = st.executeQuery();
			if (rs.next()) {
				s = rs.getDouble("orvl_shippingamount");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return s;
	}

	public void insertOrders(int cid, String oid, double totalPrice, int cpid) {

		// Get customer ID based on username
		try {
			Connection conn = DBConnectionManager.getConnection();

			String sql1 = "INSERT INTO orders2003 VALUES (?, ?, ?, ?,?)";
			PreparedStatement st = conn.prepareStatement(sql1);
			st.setString(1, oid);
			st.setString(2, "order " + oid);
			st.setDouble(3, totalPrice);
			st.setInt(4, cid);
			st.setInt(5, cpid);
			st.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void insertOrderedProducts(List<ProductInfo> products, String oid) {
		ResultSet rs = null;

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

	public List<ProductInfo> getOrderedProducts(String oid) {
		List<ProductInfo> products = new ArrayList<>();

		try {
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT * FROM ordered_products2003 WHERE oid = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, oid);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				ProductInfo product = new ProductInfo();
				product.setPname(rs.getString("pname"));
				product.setQty(rs.getInt("qty"));
				product.setPrice(rs.getDouble("price"));
				product.setGst(rs.getDouble("gst"));
				products.add(product);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return products;
	}

	public int getStock(int pid) {
		int stock = 0;
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT prod_stock FROM ProductStock2003 WHERE prod_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, pid);
			ResultSet rs = stmt.executeQuery();

			// Check if the product exists and get the stock

			if (rs.next()) {
				stock = rs.getInt("prod_stock");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return stock;

	}

	public void updatestock(int psid, int sqty) {
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "UPDATE ProductStock2003 SET prod_stock = prod_stock - ? WHERE prod_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, sqty);
			stmt.setInt(2, psid);
			int rows = stmt.executeUpdate();
			System.out.println(rows + " updated");
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public void decreasestock(int[] psid, int[] sqty) {

		int stock = 0;
		for (int i = 0; i < psid.length; i++) {
			stock = getStock(psid[i]);
			if (stock > 0 && stock >= sqty[i]) {
				updatestock(psid[i], sqty[i]);
			}
		}
	}

	public int getdiscount(int pid) {
		int discount = 0;
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT discount FROM products2003 WHERE pid = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, pid);
			ResultSet rs = stmt.executeQuery();

			// Check if the product exists and get the stock

			if (rs.next()) {
				discount = rs.getInt("discount");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("discount method:" + discount);
		return discount;
	}

	public boolean CheckValidCoupon(String dcode, double total) {
		boolean couponApplied = false;

		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT dcp_noc FROM coupon2003 WHERE dcp_code = ? and ? between dcp_minval and dcp_maxval";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, dcode);
			stmt.setDouble(2, total);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				int remainingCoupons = rs.getInt("dcp_noc");
				if (remainingCoupons > 0) {
					// Apply coupon logic here, e.g., update order total with discount
					couponApplied = true;
					UpdateCoupon(dcode);

				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return couponApplied;

	}

	public void UpdateCoupon(String dcode) {
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "UPDATE coupon2003 SET dcp_noc = dcp_noc - 1 WHERE dcp_code = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, dcode);
			stmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int getCouponID(String dcode) {
		int cpid = 0;
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT dcp_id FROM coupon2003 WHERE dcp_code = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, dcode);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				cpid = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cpid;
	}

	public double getCouponAmount(int cpid) {
		double p = 0;
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT dcp_val FROM coupon2003 WHERE dcp_id= ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setInt(1, cpid);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				p = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return p;
	}

	public double getCashBack(double total) {
		double cashback = 0.0;
		try {
			// Connect to the database
			Connection conn = DBConnectionManager.getConnection();
			String sql = "SELECT cashback FROM cashback2003 WHERE ? between minv and maxv";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setDouble(1, total);
			ResultSet rs = stmt.executeQuery();

			if (rs.next()) {
				cashback = rs.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cashback;
	}

	public boolean checkPinCode(String pincode, int productId) {
		boolean pinCodeExists = false;

		try {
			Connection conn = DBConnectionManager.getConnection();
			CallableStatement statement = conn.prepareCall("{? = CALL CheckPinCodeExists2003(?, ?)}");
			statement.registerOutParameter(1, Types.BOOLEAN);
			statement.setInt(2, productId);
			statement.setString(3, pincode);
			statement.execute();
			pinCodeExists = statement.getBoolean(1);

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return pinCodeExists;
	}
}
