import models.Client;
import models.Offers;
import models.Requests;
import models.Seller;
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

	// Logger
	private final static Logger LOG = Logger.getLogger(Application.class.getName());
	// Lists
	private List<Client> listClients = new ArrayList<>();
	private List<Seller> listSellers = new ArrayList<>();
	private List<Stocks> listStocks = new ArrayList<>();
	private List<Offers> listOffers = new ArrayList<>();
	private List<Requests> listRequests = new ArrayList<>();
	// DB
	Connection con = null;

	public int getRandomInteger(int minimum, int maximum) {
		return ((int) (Math.random() * (maximum - minimum))) + minimum;
	}

	public void initClients() {

		// standard personal: intre 5 - 10 clienti
		int nrClient = getRandomInteger(5, 10);

		for (int c = 1; c <= nrClient; c++) {
			listClients.add(new Client(c));
		}
		System.out.println(listClients.toString());

		//TODO introducere in baza de date
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

		System.out.println(listSellers.toString());
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

		System.out.println(listStocks.toString());

	}

	public void initOffers() {

		// nr clienti in bd
		int nrClients = listClients.size();
		// nr actiuni in bd
		int nrStocks = listStocks.size();

		for (int client = 1; client <= nrClients; client++) { // pentru fiecare client

			int nrOffers = getRandomInteger(1, nrStocks); // un numar random de oferte, dar nu mai multe decat numarul total de actiuni diferite

			for (int offer = 1; offer <= nrOffers; offer++) {// pentru fiecare oferta
				int offerStock = getRandomInteger(1, nrStocks);// actiunea
				int nrStocksPerOffer = getRandomInteger(1, 5);// numarul de actiuni

				listOffers.add(new Offers(client, offerStock, nrStocksPerOffer));
			}
		}
		System.out.println(listOffers.toString());

		//TODO introducere in baza de date
	}

	public void initRequests() {
		//TODO implementare plus introducere in baza de date
	}

	public void clearLists() {
		listClients.clear();
		listSellers.clear();
		listStocks.clear();
	}

	public void clearDBTables() {
		//TODO clear db tables
	}

	public void initDatabase() throws SQLException {

		// setup connection
		con = new DatabaseConnection().connect();

		// init lists
		initClients();
		initSellers();
		initStocks();
		initOffers();
		// TODO requests - trebuie sa va legati de lista de vanzatori, lista actiuni
		// TODO tranzactions - asta nu se initializeaza, se pun date in ea in startSimulation(), cand se fac calculele
		// TODO functie startSimulation() - aici e concurenta
		// TODO System.out.println() la toate listele
		// TODO facut meniu cu switch() in terminal

		// clear all
		clearLists();
		clearDBTables();
	}

}
