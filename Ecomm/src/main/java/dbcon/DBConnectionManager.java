package dbcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class DBConnectionManager {
	private static final String PROPERTIES_FILE = "db";

	public static Connection getConnection() throws SQLException {
		ResourceBundle bundle = ResourceBundle.getBundle(PROPERTIES_FILE);

		String url = bundle.getString("url");
		String user = bundle.getString("username");
		String password = bundle.getString("password");
		String driver = bundle.getString("driver");

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new SQLException("Unable to load database driver: " + driver);
		}

		return DriverManager.getConnection(url, user, password);
	}
}
