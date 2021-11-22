import utils.Application;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws SQLException, InterruptedException {

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
				case "e" -> app.showTranzactions();
				case "?" -> app.showMenu();
				default -> {
					System.out.println(choice + " este o optiune necunoscuta, alege una din cele disponibile");
					app.showMenu();
				}
			}
		} while (!choice.equals("q"));
	}
}
