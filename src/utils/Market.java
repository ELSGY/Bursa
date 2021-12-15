package utils;

import models.Client;
import models.Seller;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class Market {

	// actors
	private Seller seller;
	private Client client;

	// variables
	private final ReentrantLock mutex;

	// semaphores
	private final Semaphore sellerHasFreePlaces;
	private final Semaphore sellerIsTrading;
	private final Semaphore clientFoundWantedOffer;
	private final Semaphore clientDoneTrading;

	public Market() {
		seller = new Seller();
		client = new Client();

		sellerHasFreePlaces = new Semaphore(0);
		sellerIsTrading = new Semaphore(0);
		clientFoundWantedOffer = new Semaphore(0);
		clientDoneTrading = new Semaphore(0);

		mutex = new ReentrantLock();
	}

	public Void startService() {

		// every seller is available for every offer
		seller.sellerReady(seller.getNume());
		// signal that seller is ready to trade an offer
		sellerHasFreePlaces.release();

		// wait for a client to trade
		try {
			clientFoundWantedOffer.acquire();
		} catch (InterruptedException e) {
			System.out.println("Seller got a client: " + e.getMessage());
		}

		// seller is trading
		seller.isTrading(seller.getNume(), client.getId_client());
		sellerIsTrading.release();

		// wait for a client to approve done trading
		try {
			clientDoneTrading.acquire();
		} catch (InterruptedException e) {
			System.out.println("Seller traded with a client: " + e.getMessage());
		}

		// signal the next client that that seller is done trading with current client
		clientDoneTrading.release();
		sellerHasFreePlaces.release();
		System.out.println("Client" + client.getId_client() + " bought from stocks from" + seller.getNume());

		return null;
	}

	public Void receiveNewClient() {

		Client client2 = new Client();

		// wait for seller to have a free place for current offer
		try {
			sellerHasFreePlaces.acquire();
			clientDoneTrading.acquire();
		} catch (InterruptedException e) {
			System.out.println("Seller got the next client: " + e.getMessage());
		}

		// signal that client found a place
		client.tookAPlaceInLine(client.getId_client());
		clientFoundWantedOffer.release();

		// seller is trading
		seller.isTrading(seller.getNume(), client.getId_client());
		sellerIsTrading.release();

		// wait for a client to approve done trading
		try {
			clientDoneTrading.acquire();
		} catch (InterruptedException e) {
			System.out.println("Seller traded with a client: " + e.getMessage());
		}

		// signal the next client that that seller is done trading with current client
		clientDoneTrading.release();
		sellerHasFreePlaces.release();
		System.out.println("Client" + client.getId_client() + " bought from stocks from" + seller.getNume());

		return null;
	}

}
