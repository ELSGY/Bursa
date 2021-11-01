package models;

public class Client {

	private final int id_client;

	public Client(int id_client) {
		this.id_client = id_client;
	}

	public int getId_client() {
		return this.id_client;
	}

	public String toString() {
		return "\nID_CLIENT: " + this.getId_client();
	}
}
