package models;

import rabbitmq.Sender;
import utils.Application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {

	private int id_client;
	private final List<Requests> clientRequests = new ArrayList<>();
	private final List<Requests> originalClientRequests = new ArrayList<>();
	private final Sender sender = new Sender();
	Application app = new Application();

	public Client() {
	}

	public void setId_client(int id) {
		this.id_client = id;
	}

	public int getId_client() {
		return this.id_client;
	}

	public List<Requests> getOriginalClientRequests() {
		return originalClientRequests;
	}

	public void addRequest(Requests req) {
		clientRequests.add(req);
	}

	public void addOriginalRequest(Requests req) {
		originalClientRequests.add(req);
	}

	public void showRequests() {
		System.out.println("\nCLIENT: " + this.getId_client() + " LIST REQUESTS: \n" + this.getOriginalClientRequests());
	}

	public String toString() {
		return "\nID_CLIENT: " + this.getId_client();
	}

	public void tookAPlaceInLine(int id) {
		System.out.println("Client " + id + " took a place in line at seller x for offer y");
	}

	@Override
	public void run() {
		// Lambda Expression
		tradeStocks();
	}

	public void tradeStocks() {

		ReentrantLock mutex = new ReentrantLock();


		List<Offers> listOffers = Collections.synchronizedList(app.getOffers());
		List<Transactions> listTransactions = Collections.synchronizedList(app.getTransactions());

		synchronized (listOffers) {
			synchronized (listTransactions) {
				clientRequests.forEach(req -> listOffers.forEach(off -> {

					if (req.getId_actiune() == off.getId_actiune()) {
						if (req.getNr_actiuni() != 0 && off.getNr_actiuni() != 0) {
							int min = Math.min(req.getNr_actiuni(), off.getNr_actiuni());
							System.out.println(min);

							//off.setNr_actiuni(off.getNr_actiuni() - min); - >
							setNr_actiuni(off, off.getNr_actiuni() - min);
							req.setNr_actiuni(req.getNr_actiuni() - min);

							//Application.listTransactions.add(new Transactions(req.getId_actiune(), req.getId_client(), off.getId_vanzator(), min)); ->
							addTransactions(req.getId_actiune(), req.getId_client(), off.getId_vanzator(), min);
						}
					}
				}));
			}
		}
	}

	private void addTransactions(int id_actiune, int id_client, int id_vanzator, int min) {

		System.out.println("+++");
		app.updateTransactions(new Transactions(id_actiune, id_client, id_vanzator, min));

		try {
			sender.sendMessage(id_client, min, id_actiune, id_vanzator);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	private void setNr_actiuni(Offers obj, int nr_actiuni) {
		app.updateOffers(obj, nr_actiuni);
	}
}
