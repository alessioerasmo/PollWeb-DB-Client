package domain;

import java.time.LocalDate;

public class Domanda {

	private String Tipo;
	private int ID;
	private int ID_Sondaggio;
	private String Testo;
	private String Note;
	private int Minimo;
	private int Massimo;
	private LocalDate DataMinima;
	private LocalDate DataMassima;
	private String pattern;
	private int posizione;

	public String getTipo() {
		return Tipo;
	}

	public void setTipo(String tipo) {
		Tipo = tipo;
	}

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

	public int getMinimo() {
		return Minimo;
	}

	public void setMinimo(int minimo) {
		Minimo = minimo;
	}

	public int getMassimo() {
		return Massimo;
	}

	public void setMassimo(int massimo) {
		Massimo = massimo;
	}

	public LocalDate getDataMinima() {
		return DataMinima;
	}

	public void setDataMinima(LocalDate dataMinima) {
		DataMinima = dataMinima;
	}

	public LocalDate getDataMassima() {
		return DataMassima;
	}

	public void setDataMassima(LocalDate dataMassima) {
		DataMassima = dataMassima;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public int getPosizione() {
		return posizione;
	}

	public void setPosizione(int posizione) {
		this.posizione = posizione;
	}

}
