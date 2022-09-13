package domain;

public class DomandaNumerica implements Domanda {

	private int ID;
	private int ID_Sondaggio;
	private String Testo;
	private String Note;
	private int Minimo;
	private int Massimo;

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

}
