package models;

public class Seller {

	private final int id_vanzator;
	private final String nume;

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

	public String toString() {
		return "\nID_VANZATOR: " + this.getId_vanzator() + " NUME: " + this.getNume();
	}
}
