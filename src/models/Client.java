package models;

import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {

	private final int id_client;
	private List<Requests> clientRequests = new ArrayList<>();

	public Client(int id_client) {
		this.id_client = id_client;
	}

	public int getId_client() {
		return this.id_client;
	}

	public List<Requests> getClientRequests(){
		return clientRequests;
	}

	public void addRequest(Requests req){
		clientRequests.add(req);
	}

	public void showRequests(){
		System.out.println("\nCLIENT: " + this.getId_client() + " LIST REQUESTS: \n" + this.getClientRequests());
	}

	public String toString() {
		return "\nID_CLIENT: " + this.getId_client();
	}

	@Override
	public void run() {

	}
}
