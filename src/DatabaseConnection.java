import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

public class DatabaseConnection {

	private final static Logger LOG = Logger.getLogger(DatabaseConnection.class.getName());

	public Connection connect() {
		Connection con = null;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:pcbeDB.db");
			LOG.info("Connection established...");
		} catch (ClassNotFoundException | SQLException e) {
			LOG.info(e + "");
		}
		return con;
	}

}
