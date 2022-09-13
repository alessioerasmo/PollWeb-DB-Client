package domain;

import java.util.ArrayList;
import java.util.List;

public class Sondaggio {

	private int ID;
	private String Titolo;
	private String Apertura;
	private String Chiusura;
	private Utente UtenteResponsabile;
	private ArrayList<Domanda> Domande = new ArrayList<>();

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTitolo() {
		return Titolo;
	}

	public void setTitolo(String titolo) {
		Titolo = titolo;
	}

	public String getApertura() {
		return Apertura;
	}

	public void setApertura(String apertura) {
		Apertura = apertura;
	}

	public String getChiusura() {
		return Chiusura;
	}

	public void setChiusura(String chiusura) {
		Chiusura = chiusura;
	}

	public Utente getUtenteResponsabile() {
		return UtenteResponsabile;
	}

	public void setUtenteResponsabile(Utente utenteResponsabile) {
		UtenteResponsabile = utenteResponsabile;
	}

	public ArrayList<Domanda> getDomande() {
		ArrayList<Domanda> lista = new ArrayList<>();
		lista.addAll(Domande);
		return lista;
	}

	public void addDomanda(Domanda domanda) {
		Domande.add(domanda);
	}

}
