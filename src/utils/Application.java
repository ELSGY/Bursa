package utils;

import models.Client;
import models.Offers;
import models.Requests;
import models.Seller;
import models.Stocks;
import models.Transactions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Application {

	// Logger
	private final static Logger LOG = Logger.getLogger(Application.class.getName());
	// Lists
	private List<Client> listClients = new ArrayList<>();
	private List<Seller> listSellers = new ArrayList<>();
	private List<Stocks> listStocks = new ArrayList<>();
	private List<Requests> listRequests = new ArrayList<>();
	private List<Thread> thd = new ArrayList<>();
	// Static Lists
	private static List<Offers> listOffers = new ArrayList<>();
	private static List<Transactions> listTransactions = new ArrayList<>();
	// DB
	static Connection con = null;

	public static List<Offers> getListOffers() {
		return listOffers;
	}

	public static void setListOffers(List<Offers> list) {
		listOffers = list;
	}

	public static List<Transactions> getListTransactions() {
		return listTransactions;
	}

	public static void setListTransactions(List<Transactions> list) {
		listTransactions = list;
	}

	private void initTransactions(List<Transactions> list) throws SQLException {
		Statement stmt = null;
		stmt = con.createStatement();
		Statement finalStmt = stmt;
		list.forEach(t -> {
			String insertOffers = "INSERT INTO tranzactii (id_actiune, id_client, id_vanzator, nr_tranzactii) " +
								  "VALUES (" + t.getId_actiune() + ", " + t.getId_client() + ", " + t.getId_vanzator() + ", " + t.getNr_tranzactii() + ")";
			try {
				finalStmt.executeUpdate(insertOffers);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		});
	}

	private int getRandomInteger(int minimum, int maximum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

	private void initClients() throws SQLException {

		// standard personal: intre 5 - 10 clienti
		int nrClient = getRandomInteger(5, 10);

		for (int c = 1; c <= nrClient; c++) {
			listClients.add(new Client(c));
		}

		for (Client client : listClients) {
			String insertClient = "INSERT INTO client (id_client) VALUES (" + client.getId_client() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertClient);
		}
	}

	private void initSellers() throws SQLException {

		final String getAllSellers = "select * from vanzator";

		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getAllSellers);

		while (result.next()) {
			int id_vanzator = result.getInt("id_vanzator");
			String nume = result.getString("nume");

			listSellers.add(new Seller(id_vanzator, nume));
		}
	}

	private void initStocks() throws SQLException {

		final String getAllStocks = "select * from actiune";

		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getAllStocks);

		while (result.next()) {
			int id_actiune = result.getInt("id_actiune");
			String denumire = result.getString("denumire");
			int pret = result.getInt("pret");

			listStocks.add(new Stocks(id_actiune, denumire, pret));
		}
	}

	private void initOffers() throws SQLException {
		Offers o;
		// nr vanzatori in bd
		int nrSellers = listSellers.size();
		// nr actiuni in bd
		int nrStocks = listStocks.size();
		int offerStock, noOffers, actions;
		for (int seller = 1; seller <= nrSellers; seller++) { //pentru fiecare vanzator

			noOffers = getRandomInteger(1, nrStocks); // numarul de oferte ale lui seller

			for (int offer = 1; offer <= noOffers; offer++) {// pentru fiecare oferta
				actions = getRandomInteger(1, 10);
				do {
					offerStock = getRandomInteger(1, nrStocks); // id ul oferei
					o = new Offers(offerStock, seller, actions);
				} while (listOffers.contains(o)); // 0 pt ca numarul de actiuni
				// nu conteaza momentan
				int nrStocksPerOffer = getRandomInteger(1, 10);// numarul de actiuni

				listOffers.add(o);

				// add offer to it's seller
				listSellers.get(seller - 1).addOffer(o);
			}
		}

		for (Offers offer : listOffers) {
			String insertOffers = "INSERT INTO oferte (id_vanzator, id_actiune, nr_actiuni) VALUES (" + offer.getId_vanzator() + ", " + offer.getId_actiune() + ", " + offer.getNr_actiuni() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertOffers);
		}
	}

	private void initRequests() throws SQLException {

		// nr clienti in bd
		int nrClients = listClients.size();
		// nr actiuni in bd
		int nrStocks = listStocks.size();
		int offerStock, actions, noRequests;
		Requests r;
		for (int client = 1; client <= nrClients; client++) {// pentru fiecare client

			noRequests = getRandomInteger(1, nrStocks);// un numar random de cereri

			for (int request = 1; request <= noRequests; request++) {// pentru fiecare cerere
				actions = getRandomInteger(1, 10);
				do {
					offerStock = getRandomInteger(1, nrStocks);
					r = new Requests(client, offerStock, actions);
				} while (listRequests.contains(r)); // 0 pentru ca numarul de
				// cereri nu conteaz momentan

				listRequests.add(r);

				// add request to it's client
				listClients.get(client - 1).addRequest(r);
			}
		}

		for (Requests requests : listRequests) {
			String insertRequests = "INSERT INTO cerere (id_client, id_actiune, nr_actiuni) VALUES (" + requests.getId_client() + ", " + requests.getId_actiune() + ", " + requests.getNr_actiuni() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertRequests);
		}
	}

	public void showDB() {

		System.out.println("CLIENTI:");
		System.out.println(listClients.toString());

		System.out.println("VANZATORI:");
		System.out.println(listSellers.toString());

		System.out.println("ACTIUNI:");
		System.out.println(listStocks.toString());

		System.out.println("OFERTE:");
		System.out.println(listOffers.toString());

		System.out.println("CERERI:");
		System.out.println(listRequests.toString());

		_showMenu();
	}

	public void showTranzactions() {

		if (listTransactions.isEmpty()) {
			System.out.println("No tranzactions took place...");
		} else {
			listTransactions.forEach(t -> {
				try {
					System.out.println("Client: " + t.getId_client() +
									   " purchased: " + t.getNr_tranzactii() +
									   " stocks, stock: " + t.getId_actiune() +
									   " from: " + getNumeVanzator(t.getId_vanzator()));
				} catch (SQLException throwables) {
					throwables.printStackTrace();
				}
			});
		}
		_showMenu();
	}

	public String getNumeVanzator(int id) throws SQLException {
		String getClientName = "SELECT nume\n" +
							   "  FROM vanzator\n" +
							   " WHERE id_vanzator = " + id + ";\n";
		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getClientName);
		return result.getString("nume");
	}

	public void showClientsRequests() {
		listClients.forEach(client -> {
			client.showRequests();
		});
		_showMenu();
	}

	public void showSellersOffers() {
		listSellers.forEach(seller -> {
			seller.showOffers();
		});
		_showMenu();
	}

	private void _showMenu() {
		System.out.println("");
		System.out.println("");
		System.out.println("?: Show Menu");
		System.out.println("q: Quit");
		System.out.println("");
	}

	public void showMenu() {
		System.out.println("a: Start simulation");
		System.out.println("b: Show database");
		System.out.println("c: Show clients' requests");
		System.out.println("d: Show sellers' offers");
		System.out.println("e: Show tranzactions");
		System.out.println("q: Quit");
	}

	public void clearLists() {
		listClients.clear();
		listSellers.clear();
		listStocks.clear();
		listOffers.clear();
		listRequests.clear();
		listTransactions.clear();
	}

	public void clearDBTables() throws SQLException {

		final String deleteClientData = "DELETE FROM client";
		final String deleteCerereData = "DELETE FROM cerere";
		final String deleteOferteData = "DELETE FROM oferte";
		final String deleteTranzactiiData = "DELETE FROM tranzactii";

		PreparedStatement deleteClients = con.prepareStatement(deleteClientData);
		PreparedStatement deleteRequests = con.prepareStatement(deleteCerereData);
		PreparedStatement deleteOffers = con.prepareStatement(deleteOferteData);
		PreparedStatement deleteTranz = con.prepareStatement(deleteTranzactiiData);
		deleteClients.execute();
		deleteRequests.execute();
		deleteOffers.execute();
		deleteTranz.execute();
	}

	public void initDBTables() throws SQLException {
		initClients();
		initSellers();
		initStocks();
		initOffers();
		initRequests();
	}

	public void initDatabase() throws SQLException {

		// setup connection
		con = new DatabaseConnection().connect();

		// clear all
		clearLists();

		// clear DB
		clearDBTables();

		// init lists
		initDBTables();

		LOG.info("Simulation is ready!");
	}

	public void startSimulation() throws InterruptedException, SQLException {

		LOG.info("Simulation started...");

		// make a thread for every client
		listClients.forEach(client -> {
			thd.add(new Thread(client));
		});

		LOG.info("Transactions in process...");

		thd.forEach(thread -> {
			thread.start();
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		// insert transactions into DB
		initTransactions(listTransactions);

		LOG.info("Simulation ended...");
		_showMenu();
	}

}
