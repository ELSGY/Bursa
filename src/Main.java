import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Main {

	private final static Logger LOG = Logger.getLogger(Main.class.getName());

	public static void startUp() throws SQLException {

		// setup connection
		Connection con = new DatabaseConnection().connect();

		final String getAllSellers = "select * from vanzator";

		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getAllSellers);

		while(result.next()){
			String name = result.getString("nume");
			LOG.info(name);
		}

	}

	public static void main(String []args) throws SQLException {

		startUp();

	}
}
