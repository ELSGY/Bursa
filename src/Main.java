import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws SQLException {

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
				case "c" -> app.showTranzactions();
				case "?" -> app.showMenu();
			}
		} while (!choice.equals("q"));
	}
}
