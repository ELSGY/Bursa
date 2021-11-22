package models;

import java.util.Objects;

public class Requests {

	private final int id_client;
	private final int id_actiune;
	private int nr_actiuni;

	public Requests(int id_client, int id_actiune, int nr_actiuni) {
		this.id_client = id_client;
		this.id_actiune = id_actiune;
		this.nr_actiuni = nr_actiuni;
	}

	public int getNr_actiuni() {
		return this.nr_actiuni;
	}

	public int getId_actiune() {
		return this.id_actiune;
	}

	public int getId_client() {
		return this.id_client;
	}

	public void setNr_actiuni(int nr) {
		this.nr_actiuni = nr;
	}

	public String toString() {
		return "\nID_CLIENT: " + this.getId_client() + " ID_ACTIUNE: " + this.getId_actiune() + " NR_ACTIUNI: " + this.getNr_actiuni();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Requests requests = (Requests) o;
		return id_client == requests.id_client && id_actiune == requests.id_actiune;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id_client, id_actiune);
	}
}
