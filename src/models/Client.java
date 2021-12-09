package models;

import utils.Application;

import java.util.ArrayList;
import java.util.List;

public class Client implements Runnable {

	private final int id_client;
	private List<Requests> clientRequests = new ArrayList<>();
	private List<Offers> sGLB = new ArrayList<>();

	public Client(int id_client) {
		this.id_client = id_client;
	}

	public int getId_client() {
		return this.id_client;
	}

	public List<Requests> getClientRequests() {
		return clientRequests;
	}

	public void addRequest(Requests req) {
		clientRequests.add(req);
	}

	public void showRequests() {
		System.out.println("\nCLIENT: " + this.getId_client() + " LIST REQUESTS: \n" + this.getClientRequests());
	}

	public String toString() {
		return "\nID_CLIENT: " + this.getId_client();
	}

	@Override
	public void run() {

		List<Offers> listOffers = new ArrayList<>(Application.getListOffers());
		List<Transactions> listTransactions = new ArrayList<>(Application.getListTransactions());

		clientRequests.forEach(req -> {

			listOffers.forEach(off -> {

				if (req.getId_actiune() == off.getId_actiune()) {
					if (req.getNr_actiuni() != 0 && off.getNr_actiuni() != 0) {
						int min = Math.min(req.getNr_actiuni(), off.getNr_actiuni());

						off.setNr_actiuni(off.getNr_actiuni() - min);
						req.setNr_actiuni(req.getNr_actiuni() - min);

						listTransactions.add(new Transactions(req.getId_actiune(), req.getId_client(), off.getId_vanzator(), min));

						System.out.println("Client: " + this.getId_client() + " purchased: " + min + " stocks, stock: " + req.getId_actiune() + " from: " + off.getId_vanzator());
					}
				}

			});

		});
		Application.setListOffers(listOffers);
		Application.setListTransactions(listTransactions);
	}
}
