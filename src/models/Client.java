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

		clientRequests.forEach(req -> {

			listOffers.forEach(off -> {

				if (req.getId_actiune() == off.getId_actiune()) {
					if (req.getNr_actiuni() != 0 && off.getNr_actiuni() != 0) {
						int min = Math.min(req.getNr_actiuni(), off.getNr_actiuni());

						off.setNr_actiuni(off.getNr_actiuni() - min);
						req.setNr_actiuni(req.getNr_actiuni() - min);

						System.out.println("Clientul: " + this.getId_client() + " a cumparat: " + min + " actiuni, actiunea: " + req.getId_actiune() + " de la vanzatorul: " + off.getId_vanzator());
					}
				}

			});

		});

		Application.setListOffers(listOffers);
	}
}
