import models.Stocks;
import utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Application {

	private final static Logger LOG = Logger.getLogger(Application.class.getName());

	public int getRandomInteger(int maximum, int minimum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

	public void initDatabase() throws SQLException {

		// setup connection
		Connection con = new DatabaseConnection().connect();

		// models
		List<Stocks> stockList = new ArrayList<>();


		// inserare bd



		// preluare date
		final String getAllSellers = "select * from actiune";

		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getAllSellers);

		while (result.next()) {
			int id = result.getInt("id_actiune");
			String name = result.getString("denumire");
			double pret = result.getDouble("pret");

			stockList.add(new Stocks(id, name, pret));
		}

		System.out.println(stockList.get(0));
		stockList.get(0).setPret(300);

		String update = "update actiune\n" +
						"set pret =" + 600 + "\n" +
						"where id_actiune =" + stockList.get(0).getId() + ";";

		stmt.executeUpdate(update);

		System.out.println(stockList.toString());

//		int nr = getRandomInteger(1, 10);
//		System.out.println(nr);

	}
}
