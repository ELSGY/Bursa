import models.Client;
import models.Offers;
import models.Requests;
import models.Seller;
import models.Stocks;
import models.Tranzactions;
import utils.DatabaseConnection;

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
	private List<Offers> listOffers = new ArrayList<>();
	private List<Requests> listRequests = new ArrayList<>();
	private List<Tranzactions> listTranzactions = new ArrayList<>();
	// DB
	Connection con = null;

	public int getRandomInteger(int minimum, int maximum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

	public void initClients() throws SQLException {

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

	public void initSellers() throws SQLException {

		final String getAllSellers = "select * from vanzator";

		Statement stmt = con.createStatement();
		ResultSet result = stmt.executeQuery(getAllSellers);

		while (result.next()) {
			int id_vanzator = result.getInt("id_vanzator");
			String nume = result.getString("nume");

			listSellers.add(new Seller(id_vanzator, nume));
		}
	}

	public void initStocks() throws SQLException {

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

	public void initOffers() throws SQLException {

		// nr clienti in bd
		int nrClients = listClients.size();
		// nr actiuni in bd
		int nrStocks = listStocks.size();

		for (int client = 1; client <= nrClients; client++) { //pentru fiecare client

			int nrOffers = getRandomInteger(1, nrStocks); //un numar random de oferte, dar nu mai multe decat numarul total de actiuni diferite

			for (int offer = 1; offer <= nrOffers; offer++) {// pentru fiecare oferta
				int offerStock = getRandomInteger(1, nrStocks);// actiunea
				int nrStocksPerOffer = getRandomInteger(1, 5);// numarul de actiuni

				listOffers.add(new Offers(client, offerStock, nrStocksPerOffer));
			}
		}

		for (Offers offer : listOffers) {
			String insertOffers = "INSERT INTO oferte (id_vanzator, id_actiune, nr_actiuni) VALUES (" + offer.getId_vanzator() + ", " + offer.getId_actiune() + ", " + offer.getNr_actiuni() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertOffers);
		}
	}

	public void initRequests() throws SQLException {

		int nrClients = listClients.size();
		int nrStocks = listStocks.size();

		for (int client = 1; client <= nrClients; client++) {
			int nrRequests = getRandomInteger(1, nrStocks);
			for (int request = 1; request <= nrRequests; request++) {
				int offerStock = getRandomInteger(1, nrStocks);
				int nrRequest = getRandomInteger(1, 5);

				listRequests.add(new Requests(client, offerStock, nrRequest));
			}
		}

		for (Requests requests : listRequests) {
			String insertRequests = "INSERT INTO cerere (id_client, id_actiune, nr_actiuni) VALUES (" + requests.getId_client() + ", " + requests.getId_actiune() + ", " + requests.getNr_actiuni() + ")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertRequests);
		}
	}

	public void showDB(){

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

	}

	public void showTranzactions() {

		if (listTranzactions.isEmpty()) {
			System.out.println("No tranzactions took place...");
		} else {
			listTranzactions.toString();
		}
	}

	public void showMenu() {

		System.out.println("Command Options: ");
		System.out.println("a: Start simulation");
		System.out.println("b: Show database");
		System.out.println("c: Show tranzactions");
		System.out.println("q: Quit");
	}

	public void clearLists() {
		listClients.clear();
		listSellers.clear();
		listStocks.clear();
		listOffers.clear();
		listRequests.clear();
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

	public void initDatabase() throws SQLException {

		// setup connection
		con = new DatabaseConnection().connect();

		// clear all
		clearLists();
		// clear DB
		clearDBTables();

		// init lists
		initClients();
		initSellers();
		initStocks();
		initOffers();
		initRequests();

		LOG.info("Simulation is ready!");
	}

	public void startSimulation() {
		// TODO functie startSimulation() - aici e concurenta
		// TODO tranzactions - asta nu se initializeaza, se pun date in ea in startSimulation(), cand se fac calculele

		LOG.info("Simulation started...");
	}

}
