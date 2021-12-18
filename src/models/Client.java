package models;

import rabbitmq.Sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReentrantLock;

public class Client implements Runnable {

	private int id_client;
	private final List<Requests> clientRequests = new ArrayList<>();
	private final Sender sender = new Sender();

	public Client() {
	}

	public void setId_client(int id) {
		this.id_client = id;
	}

	public int getId_client() {
		return this.id_client;
	}

	public List<Requests> getOriginalClientRequests() {
		return clientRequests;
	}

	public void addRequest(Requests req) {
		clientRequests.add(req);
	}

	public void showRequests() {
		System.out.println("\nCLIENT: " + this.getId_client() + " LIST REQUESTS: \n" + this.getOriginalClientRequests());
	}

	public String toString() {
		return "\nID_CLIENT: " + this.getId_client();
	}

	private void sendMessageToQueue(int id_client, int min, int id_actiune, int id_vanzator) {
		try {
			sender.sendMessage(id_client, min, id_actiune, id_vanzator);
		} catch (IOException | TimeoutException e) {
			e.printStackTrace();
		}
	}

	private void addTransactions(int id_actiune, int id_client, int id_vanzator, int min) {
		SingletonMarket.getInstance().addTransactions(new Transactions(id_actiune, id_client, id_vanzator, min));
	}

	public void tradeStocks() {

		ReentrantLock mutex = new ReentrantLock();

		this.clientRequests.forEach(req -> Collections.synchronizedList(SingletonMarket.getInstance().getListOffers()).forEach(off -> {
			if (req.getId_actiune() == off.getId_actiune()) {
				if (req.getNr_actiuni() != 0 && off.getNr_actiuni() != 0) {

					int min = Math.min(req.getNr_actiuni(), off.getNr_actiuni());

					req.setNr_actiuni(Math.abs(req.getNr_actiuni() - min));
					SingletonMarket.getInstance().addRequests(req);
					SingletonMarket.getInstance().updateOffer(off, off.getNr_actiuni() - min);

					addTransactions(req.getId_actiune(),
									req.getId_client(),
									off.getId_vanzator(), min);
					sendMessageToQueue(req.getId_client(),
									   min,
									   req.getId_actiune(),
									   off.getId_vanzator());
				}
			} else {
				SingletonMarket.getInstance().addRequests(req);
			}

		}));
	}

	@Override
	public void run() {
		tradeStocks();
	}
}
