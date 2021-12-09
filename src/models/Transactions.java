package models;

public class Transactions {

	private final int id_actiune;
	private final int id_client;
	private final int id_vanzator;
	private final int nr_tranzactii;

	public Transactions(int id_actiune, int id_client, int id_vanzator, int nr_tranzactii) {
		this.id_actiune = id_actiune;
		this.id_client = id_client;
		this.id_vanzator = id_vanzator;
		this.nr_tranzactii = nr_tranzactii;
	}

	public int getNr_tranzactii() {
		return this.nr_tranzactii;
	}

	public int getId_vanzator() {
		return this.id_vanzator;
	}

	public int getId_client() {
		return this.id_client;
	}

	public int getId_actiune() {
		return this.id_actiune;
	}

	@Override
	public String toString() {
		return "\nClient: " + this.getId_client() + " purchased: " + this.getNr_tranzactii() + " stocks, stock: " + this.getId_actiune() + " from: " + this.getId_vanzator();
	}
}
