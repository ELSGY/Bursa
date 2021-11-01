package models;

public class Offers {

	private final int id_vanzator;
	private final int id_actiune;
	private int nr_actiuni;

	public Offers(int id_vanzator, int id_actiune, int nr_actiuni) {
		this.id_vanzator = id_vanzator;
		this.id_actiune = id_actiune;
		this.nr_actiuni = nr_actiuni;
	}

	public int getNr_actiuni() {
		return this.nr_actiuni;
	}

	public int getId_actiune() {
		return this.id_actiune;
	}

	public int getId_vanzator() {
		return this.id_vanzator;
	}

	public void setNr_actiuni(int nr) {
		this.nr_actiuni = nr;
	}

	public String toString() {
		return "\nID_VANZATOR: " + this.getId_vanzator() + " ID_ACTIUNE: " + this.getId_actiune() + " NR_ACTIUNI: " + this.getNr_actiuni();
	}
}
