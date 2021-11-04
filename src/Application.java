import models.Client;
import models.Offers;
import models.Requests;
import models.Seller;
import models.Stocks;
import utils.DatabaseConnection;

import java.sql.*;
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

	public void initClients() throws SQLException {

		// standard personal: intre 5 - 10 clienti
		int nrClient = getRandomInteger(5, 10);

		for (int c = 1; c <= nrClient; c++) {
			listClients.add(new Client(c));
		}
		System.out.println(listClients.toString());


		//TODO introducere in baza de date
		for (Client client: listClients) {
			String insertClient = "INSERT INTO client (id_client) VALUES ("+ client.getId_client()+")";
			System.out.println(client.getId_client());
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
		System.out.println(listOffers.toString());
		for (Offers offer: listOffers) {
			String insertOffers = "INSERT INTO oferte (id_vanzator, id_actiune, nr_actiuni) VALUES ("+ offer.getId_vanzator()+", "+offer.getId_actiune() +", "+offer.getNr_actiuni()+")";
			Statement stmt = con.createStatement();
			stmt.executeUpdate(insertOffers);
		}



		//TODO introducere in baza de date => rezolvat



	}

	public void initRequests() throws SQLException {
		//TODO implementare plus introducere in baza de date => cred ca am rezolvat

		int nrClients = listClients.size();
		int nrStocks = listStocks.size();

		for (int client = 1; client <= nrClients; client++) {
			int nrRequests = getRandomInteger(1, nrStocks);
			for (int request = 1; request <= nrRequests; request++) {
				int offerStock = getRandomInteger(1, nrStocks);
				int nrRequest = getRandomInteger(1, 5);

				listRequests.add(new Requests(client, offerStock, nrRequest));
				for (Requests requests: listRequests) {
					//TODO DE VAZUT [SQLITE_CONSTRAINT_PRIMARYKEY]  A PRIMARY KEY constraint failed (UNIQUE constraint failed: client.id_client) (NU STIU DC ati pus unique)
					String insertRequests = "INSERT INTO oferte (id_vanzator, id_actiune, nr_actiuni) VALUES ("+ requests.getId_client()+", "+requests.getId_actiune() +", "+requests.getNr_actiuni()+")";
					Statement stmt = con.createStatement();
					stmt.executeUpdate(insertRequests);

			}
		}

	}}

	public void clearLists() {
		listClients.clear();
		listSellers.clear();
		listStocks.clear();
	}

	public void clearDBTables() throws SQLException {
		//TODO clear db tables => rezolvat
		final String deleteClientData = "delete from client";
		final String deleteActiuneData = "delete from actiune";
		final String deleteCerereData = "delete from cerere";
		final String deleteOferteData = "delete from oferte";
		final String deleteTranzactiiData = "delete from tranzactii";
		//final String deleteVanzatorData = "delete from vanzator";



		PreparedStatement deleteClient = con.prepareStatement(deleteClientData);
		PreparedStatement deleteActiune = con.prepareStatement(deleteActiuneData);
		PreparedStatement deleteCerere = con.prepareStatement(deleteCerereData);
		PreparedStatement deleteOferte = con.prepareStatement(deleteOferteData);
		PreparedStatement deleteTranzactii = con.prepareStatement(deleteTranzactiiData);
		deleteClient.execute();
		deleteActiune.execute();
		deleteCerere.execute();
		deleteOferte.execute();
		deleteTranzactii.execute();
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
		//comentat momentan
		//clearDBTables();
	}

}
