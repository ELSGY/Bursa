package models;

import java.util.ArrayList;
import java.util.List;

public class Seller {

	private int id_vanzator;
	private String nume;
	private final List<Offers> sellerOffers = new ArrayList<>();

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

	public void addOffer(Offers off) {
		sellerOffers.add(off);
	}

	public void showOffers() {
		System.out.println("\nSELLER: " + this.getId_vanzator() + " LIST OFFERS: \n" + this.sellerOffers);
	}

	public String toString() {
		return "\nID_VANZATOR: " + this.getId_vanzator() + " NUME: " + this.getNume();
	}

}
