package models;

public class Stocks {

	private final int id_actiune;
	private final String denumire;
	private final double pret;

	public Stocks(int id, String den, double pret) {
		this.id_actiune = id;
		this.denumire = den;
		this.pret = pret;
	}

	public int getId() {
		return this.id_actiune;
	}

	public String getDenumire() {
		return this.denumire;
	}

	public double getPret() {
		return this.pret;
	}

	public String toString() {
		return "\nID: " + this.getId() + " DENUMIRE: " + this.getDenumire() + " PRET: " + this.getPret();
	}
}
