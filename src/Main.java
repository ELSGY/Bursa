import utils.Application;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

public class Main {

	public static void main(String[] args) throws SQLException, InterruptedException, IOException, TimeoutException {

		Application app = new Application();
		app.initDatabase();

		app.showMenu();

		String choice;
		do {
			Scanner scan = new Scanner(System.in);
			choice = scan.nextLine();
			switch (choice) {
				case "a" -> app.startSimulation();
				case "b" -> app.showDB();
				case "c" -> app.showClientsRequests();
				case "d" -> app.showSellersOffers();
				case "e" -> app.showRequestsDifferences();
				case "f" -> app.showOffersDifferences();
				case "g" -> app.showTranzactions();
				case "?" -> app.showMenu();
				default -> {
					System.out.println("\"" + choice + "\"" + " unknown option, please select another option.");
					app.showMenu();
				}
			}
		} while (!choice.equals("q"));
	}
}
