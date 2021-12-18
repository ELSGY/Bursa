package utils;

import models.Client;
import models.Offers;
import models.Requests;
import models.Seller;
import models.SingletonMarket;
import models.Stocks;
import models.Transactions;
import rabbitmq.Consumer;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;
import java.util.logging.Logger;

public class Application {

	// Logger
	private final static Logger LOG = Logger.getLogger(Application.class.getName());
	// Lists
	private List<Client> listClients = new ArrayList<>();
	private List<Seller> listSellers = new ArrayList<>();
	private List<Stocks> listStocks = new ArrayList<>();
	private List<Offers> originalOffers = new ArrayList<>();
	private List<Requests> originalRequests = new ArrayList<>();
	private List<Requests> listRequests = new ArrayList<>();
	private List<Thread> thd = new ArrayList<>();
	// DB
	static Connection con = null;

	private int getRandomInteger(int minimum, int maximum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

	private void initTransactions() throws SQLException {

		Statement stmt = null;
		stmt = con.createStatement();
		Statement finalStmt = stmt;
		SingletonMarket.getInstance().getListTransactions().forEach(t -> {
			String insertOffers = "INSERT INTO tranzactii (id_actiune, id_client, id_vanzator, nr_tranzactii) " +
								  "VALUES (" + t.getId_actiune() + ", " + t.getId_client() + ", " + t.getId_vanzator() + ", " + t.getNr_tranzactii() + ")";
			try {
				finalStmt.executeUpdate(insertOffers);
			} catch (SQLException throwables) {
				throwables.printStackTrace();
			}
		});
	}

	private void initClients() throws SQLException, IOException, TimeoutException {

		Client client = null;

		// standard personal: intre 5 - 10 clienti
		int nrClient = getRandomInteger(1, 5);

		for (int c = 1; c <= nrClient; c++) {
			client = new Client();
			client.setId_client(c);
			listClients.add(client);
		}

		for (Client c : listClients) {
			String insertClient = "INSERT INTO client (id_client) VALUES (" + c.getId_client() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertClient);
		}
	}

	private void initSellers() throws SQLException {

		final String getAllSellers = "select * from vanzator";
		Seller seller = null;

		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getAllSellers);

		while (result.next()) {
			int id_vanzator = result.getInt("id_vanzator");
			String nume = result.getString("nume");

			seller = new Seller();
			seller.setId_vanzator(id_vanzator);
			seller.setNume(nume);

			listSellers.add(seller);
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
		Offers o = null;
		// nr vanzatori in bd
		int nrSellers = listSellers.size();
		// nr actiuni in bd
		int nrStocks = listStocks.size();
		int offerStock, noOffers, actions;
		for (int seller = 1; seller <= nrSellers; seller++) { // pentru fiecare vanzator

			noOffers = getRandomInteger(1, nrStocks); // numarul de oferte ale lui seller

			for (int offer = 1; offer <= noOffers; offer++) {// pentru fiecare oferta
				actions = getRandomInteger(1, 10);
				do {
					offerStock = getRandomInteger(1, nrStocks); // id ul oferteil
					o = new Offers(offerStock, seller, actions);
				} while (originalOffers.contains(o)); // 0 pt ca numarul de actiuni
				// nu conteaza momentan
				int nrStocksPerOffer = getRandomInteger(1, 10);// numarul de actiuni

				// eroare de referinta
				originalOffers.add(o);

				// add offer to it's seller
				listSellers.get(seller - 1).addOffer(o);
			}
		}

		for (Offers offer : originalOffers) {
			String insertOffers = "INSERT INTO oferte (id_vanzator, id_actiune, nr_actiuni) VALUES (" + offer.getId_vanzator() + ", " + offer.getId_actiune() + ", " + offer.getNr_actiuni() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertOffers);
		}
	}

	private void initRequests() throws SQLException {

		Requests r = null;
		Requests r1 = null;

		// nr clienti in bd
		int nrClients = listClients.size();
		// nr actiuni in bd
		int nrStocks = listStocks.size();
		int offerStock, actions, noRequests;

		for (int client = 1; client <= nrClients; client++) {// pentru fiecare client

			noRequests = getRandomInteger(1, nrStocks);// un numar random de cereri

			for (int request = 1; request <= noRequests; request++) {// pentru fiecare cerere
				actions = getRandomInteger(1, 10);
				do {
					offerStock = getRandomInteger(1, nrStocks);
					r = new Requests(client, offerStock, actions);
				} while (listRequests.contains(r));

				// eroare de referinta
				listRequests.add(r);
				r1 = new Requests(client, offerStock, actions);
				originalRequests.add(r1);

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

	public void showOffersDifferences() {

		if (SingletonMarket.getInstance().getListTransactions().isEmpty()) {
			System.out.println("No transaction took place...");
		} else {
			for (int i = 0; i < originalOffers.size(); i++) {
				System.out.println("Seller: "
								   + originalOffers.get(i).getId_vanzator()
								   + ", stock: " + originalOffers.get(i).getId_actiune()
								   + "| Original: " + originalOffers.get(i).getNr_actiuni()
								   + " -> Now: " + SingletonMarket.getInstance().getListOffers().get(i).getNr_actiuni());
			}
		}
	}

	public void showRequestsDifferences() {
		if (SingletonMarket.getInstance().getListTransactions().isEmpty()) {
			System.out.println("No transaction took place...");
		} else {
			for (int i = 0; i < originalRequests.size(); i++) {
				System.out.println("Client: "
								   + originalRequests.get(i).getId_client() + ", stock: "
								   + originalRequests.get(i).getId_actiune() + "| Original: "
								   + originalRequests.get(i).getNr_actiuni() + " -> Now: "
								   + SingletonMarket.getInstance().getListRequests().get(i).getNr_actiuni());
			}
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
		System.out.println(originalOffers.toString());

		System.out.println("CERERI:");
		System.out.println(listRequests.toString());

		_showMenu();
	}

	public void showTranzactions() throws SQLException {

		if (SingletonMarket.getInstance().getListTransactions().isEmpty()) {
			System.out.println("No transactions took place...");
		} else {
			SingletonMarket.getInstance().getListTransactions().forEach(t -> {
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
		System.out.println("\n" +
						   "\n" +
						   "\n?: Show Menu" +
						   "\nq: Quit" +
						   "\n");
	}

	public void showMenu() {
		System.out.println("a: Start simulation" +
						   "\nb: Show database" +
						   "\nc: Show clients' requests" +
						   "\nd: Show sellers' offers" +
						   "\ne: Show requests differences" +
						   "\nf: Show offers differences" +
						   "\ng: Show tranzactions" +
						   "\nq: Quit");
	}

	public void clearLists() {
		listClients.clear();
		listSellers.clear();
		listStocks.clear();
		listRequests.clear();
		originalOffers.clear();
		originalRequests.clear();
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

	public void initDBTables() throws SQLException, IOException, TimeoutException {
		initClients();
		initSellers();
		initStocks();
		initOffers();
		initRequests();
	}

	public void initDatabase() throws SQLException, IOException, TimeoutException {

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

	private void consumeMessagesFromQueue() throws IOException, TimeoutException, InterruptedException {
		Thread.sleep(3000);
		LOG.info("Getting messages from queue...");
		Consumer.getInstance().consumeMessage(SingletonMarket.getInstance().getListTransactions().size());
		Thread.sleep(SingletonMarket.getInstance().getListTransactions().size() * 1000);
	}

	public void startSimulation() throws InterruptedException, SQLException, IOException, TimeoutException, CloneNotSupportedException {

		LOG.info("Simulation started...");
		// set singleton's lists
		SingletonMarket.getInstance().setListOffers(originalOffers);

		LOG.info("Transactions in process...");
		LOG.info("Sending messages to queue...");
		ExecutorService executor = Executors.newFixedThreadPool(listClients.size());
		// make a thread for every client
		listClients.forEach(client -> {
			executor.submit(client);
		});

		// consume queue
		consumeMessagesFromQueue();

		initTransactions();

		LOG.info("Simulation ended...");
		_showMenu();
	}
}
