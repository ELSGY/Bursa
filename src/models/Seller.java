package models;

import java.util.ArrayList;
import java.util.List;

public class Seller {

	private int id_vanzator;
	private String nume;
	private final List<Offers> sellerOffers = new ArrayList<>();
	private final List<Offers> originalSellerOffers = new ArrayList<>();

	public Seller() {
	}

	public int getId_vanzator() {
		return this.id_vanzator;
	}

	public String getNume() {
		return this.nume;
	}

	public void setId_vanzator(int id) {
		this.id_vanzator = id;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public List<Offers> getOriginalSellerOffers() {
		return originalSellerOffers;
	}

	public void addOffer(Offers off) {
		sellerOffers.add(off);
	}

	public void addOriginalOffer(Offers off) {
		originalSellerOffers.add(off);
	}

	public void showOffers() {
		System.out.println("\nSELLER: " + this.getId_vanzator() + " LIST OFFERS: \n" + this.getOriginalSellerOffers());
	}

	public String toString() {
		return "\nID_VANZATOR: " + this.getId_vanzator() + " NUME: " + this.getNume();
	}

	public void sellerReady(String nume) {
		System.out.println(nume + " has free places for every offer!");
	}

	public void isTrading(String seller, int client) {
		System.out.println(seller + " is trading with client " + client);
	}
}
