import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

public class Main {

	private final static Logger LOG = Logger.getLogger(Main.class.getName());

	public static void main(String[] args) throws SQLException {

		new Application().initDatabase();

	}
}
