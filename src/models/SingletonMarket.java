package models;

import java.util.ArrayList;
import java.util.List;

public class SingletonMarket {

	// singleton instance
	private static SingletonMarket instance;
	// market lists
	private final List<Offers> listOffers = new ArrayList<>();;
	private final List<Offers> originalOffers = new ArrayList<>();
	private final List<Requests> listRequests = new ArrayList<>();
	private final List<Transactions> listTransactions = new ArrayList<>();

	private SingletonMarket() {
	}

	public static SingletonMarket getInstance() {
		if (instance == null) {
			synchronized (SingletonMarket.class) {
				if (instance == null) {
					instance = new SingletonMarket();
				}
			}
		}
		return instance;
	}

	public void setListOffers(List<Offers> listOffers) throws CloneNotSupportedException {
		for (Offers p : listOffers) {
			this.listOffers.add(p.clone());
		}
	}

	public List<Offers> getListOffers() {
		return this.listOffers;
	}

	public List<Offers> getOriginalOffers() {
		return this.originalOffers;
	}

	public List<Requests> getListRequests() {
		return this.listRequests;
	}

	public List<Transactions> getListTransactions() {
		return this.listTransactions;
	}

	public synchronized void updateOffer(Offers next, int i) {
		getListOffers().get(getListOffers().indexOf(next)).setNr_actiuni(i);
	}

	public synchronized void addTransactions(Transactions transaction) {
		this.listTransactions.add(transaction);
	}

	public synchronized void addRequests(Requests request) {
		this.listRequests.add(request);
	}

	public synchronized void addOffer(Offers offer) {
		this.originalOffers.add(offer);
	}
}
