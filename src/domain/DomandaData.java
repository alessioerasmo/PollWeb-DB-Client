package domain;

import java.time.LocalDate;

public class DomandaData implements Domanda {
	private int ID;
	private int ID_Sondaggio;
	private String Testo;
	private String Note;
	private LocalDate Minimo;
	private LocalDate Massimo;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public int getID_Sondaggio() {
		return ID_Sondaggio;
	}

	public void setID_Sondaggio(int iD_Sondaggio) {
		ID_Sondaggio = iD_Sondaggio;
	}

	public String getTesto() {
		return Testo;
	}

	public void setTesto(String testo) {
		Testo = testo;
	}

	public String getNote() {
		return Note;
	}

	public void setNote(String note) {
		Note = note;
	}

	public LocalDate getMinimo() {
		return Minimo;
	}

	public void setMinimo(LocalDate minimo) {
		Minimo = minimo;
	}

	public LocalDate getMassimo() {
		return Massimo;
	}

	public void setMassimo(LocalDate massimo) {
		Massimo = massimo;
	}

}
