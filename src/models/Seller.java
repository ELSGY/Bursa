package models;

import java.util.ArrayList;
import java.util.List;

public class Seller {

	private final int id_vanzator;
	private final String nume;
	private List<Offers> sellerOffers = new ArrayList<>();

	public Seller(int id_vanzator, String nume) {
		this.id_vanzator = id_vanzator;
		this.nume = nume;
	}

	public int getId_vanzator() {
		return this.id_vanzator;
	}

	public String getNume() {
		return this.nume;
	}

	public List<Offers> getSellerOffers(){
		return sellerOffers;
	}

	public void addOffer(Offers off){
		sellerOffers.add(off);
	}

	public void showOffers(){
		System.out.println("\nSELLER: " + this.getId_vanzator() + " LIST OFFERS: \n" + this.getSellerOffers());
	}

	public String toString() {
		return "\nID_VANZATOR: " + this.getId_vanzator() + " NUME: " + this.getNume();
	}
}
