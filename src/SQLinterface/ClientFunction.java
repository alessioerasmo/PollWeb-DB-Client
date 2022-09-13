package SQLinterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import domain.*;

public class ClientFunction {

	public static Utente loginUtente(Connection con) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Username:");
		String username = reader.readLine();
		System.out.println();

		System.out.println("password:");
		String password = reader.readLine();
		System.out.println();

		try {

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM utente as u WHERE (u.Username='" + username
					+ "' AND u.Password='" + password + "')");

			Utente utente = null;
			while (rs.next()) {
				utente = new Utente(rs.getInt("ID"), rs.getString("Nome"), rs.getString("Cognome"),
						LocalDate.parse(rs.getString("DataNascita")), rs.getString("Sesso"), username, password);
				System.out.println("accesso competato!");
			}
			return utente;

		} catch (SQLException e) {

			System.out.println("Credenziali errate");
			return null;
		}

	}

	public static Utente creaUtente(Connection con) throws IOException {

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Nome:");
		String nome = reader.readLine();
		System.out.println();

		System.out.println("Cognome:");
		String cognome = reader.readLine();
		System.out.println();

		try {
			System.out.println("Data di nascita (formato: YYYY-MM-DD):");
			LocalDate data = LocalDate.parse(reader.readLine());
			System.out.println();

			System.out.println("Username:");
			String username = reader.readLine();
			System.out.println();

			System.out.println("Password:");
			String password = reader.readLine();
			System.out.println();

			System.out.println("Sesso (inserire M se maschio, F se femmina, X se altro):");
			String sesso = reader.readLine();
			System.out.println();

			try {

				Statement stmt = con.createStatement();
				int affected = stmt.executeUpdate(
						"INSERT INTO " + "utente (Username, Password, Nome, Cognome, DataNascita, Sesso) " + "VALUES "
								+ "('" + username + "', '" + password + "', '" + nome + "', '" + cognome
								+ "', str_to_date('" + data.toString() + "', \"%Y-%m-%d\"), '" + sesso + "');");

				if (affected == 1) {
					System.out.println("Utente Creato con successo!");
					ResultSet rs = stmt
							.executeQuery("SELECT ID FROM utente as u WHERE (u.Username = '" + username + "')");

					int ID = 0;
					while (rs.next())
						ID = rs.getInt("ID");

					stmt.close();

					Utente utente = new Utente(ID, nome, cognome, data, sesso, username, password);
					return utente;
				}

			} catch (SQLException e) {
				System.out.println("errore nella creazione dell'utente");
				return null;
			}
			return null;

		} catch (DateTimeParseException e) {
			System.out.println("inserire un valore valido nel campo data (YYYY-MM-DD), esempio 2001-06-25");
			System.out.println();
			return null;
		}

	}

	public static void MostraSondaggiByUser(Utente utente, Connection con) {

		Statement stmt;
		try {
			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT s.Titolo, s.Apertura, s.chiusura, c.Data, c.Password FROM compilazione as c JOIN sondaggio as s WHERE (c.ID_Sondaggio = s.ID AND c.ID_Utente ="
							+ utente.getID() + ");");

			System.out.println("Sondaggi disponibili:");
			System.out.println();
			System.out.println("__________________________________________________________________________________");
			System.out.println();
			int count = 0;
			while (rs.next()) {
				System.out.println("Titolo: " + rs.getString("titolo"));
				System.out.println("\"" + rs.getString("Apertura") + "\"");
				System.out.println();
				System.out.println(
						"Puoi compilare questo sondaggio utilizzando la password: " + rs.getString("Password"));
				System.out.println();
				System.out
						.println("__________________________________________________________________________________");
				System.out.println();
				count++;
			}

			if (count == 0) {
				System.out.println("non sei abilitato a compilare nessun sondaggio!");
				System.out.println();
				System.out
						.println("__________________________________________________________________________________");
				System.out.println();
			}

		} catch (SQLException e) {
			System.out.println("errore nella visualizzazione dei messaggi");
		}

	}

	public static void MostraInfoUtente(Utente utente, Connection con) {

		try {
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM utente as u WHERE u.ID =" + utente.getID());

			while (rs.next()) {
				System.out.print("Nome e Cognome: \t");
				System.out.println(rs.getString("Nome") + " " + rs.getString("Cognome"));
				System.out.println("Sesso: \t\t\t" + rs.getString("Sesso"));
				System.out.println("Data di nascita: \t" + rs.getString("DataNascita"));
				System.out.println("Username: \t\t" + rs.getString("Username"));
				System.out.println("Password: \t\t" + rs.getString("Password"));
				System.out.println();
			}

			/*
			 * non è un modo furbo per implementarlo, la mia classe utente ha tutte le info
			 * di cui ho bisogno, per cui non è necessario eseguire una query!
			 */
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static Sondaggio CompilaSondaggio(Utente utente, Connection con) throws IOException {

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.println();
			System.out.println(
					"Inserire Il codice del sondaggio che vuoi compilare (scrivi exit per tornare indietro): ");
			String codice = reader.readLine();
			if (codice.equals("exit"))
				return null;

			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(
					"SELECT s.ID, s.Titolo, s.Apertura, s.Chiusura FROM compilazione as c JOIN sondaggio as s WHERE (c.ID_Sondaggio= s.ID AND c.Password='"
							+ codice + "' AND c.ID_Utente=" + utente.getID() + " );");

			int count = 0;
			Sondaggio sondaggio = new Sondaggio();
			while (rs.next()) {
				sondaggio.setID(Integer.parseInt(rs.getString("ID")));
				sondaggio.setTitolo(rs.getString("Titolo"));
				sondaggio.setApertura(rs.getString("Apertura"));
				sondaggio.setChiusura(rs.getString("Chiusura"));
				count++;
			}

			if (count == 0) {
				System.out.println(
						"non è stato trovato nessun sondaggio, controlla nella sezione 'Visualizza sondaggi da compilare'");
				return null;
			}
			;

			if (sondaggio.getID() != 0) {
				rs = stmt.executeQuery("call maurizio.VIEWDomandePerSondaggio(" + sondaggio.getID() + ");");

				while (rs.next()) {
					String Tipo = rs.getString("Tipo");
					switch (Tipo) {
					case "Numerica":
						DomandaNumerica domandan = new DomandaNumerica();
						domandan.setID(Integer.parseInt(rs.getString("ID")));
						domandan.setID_Sondaggio(sondaggio.getID());
						domandan.setTesto(rs.getString("TestoDomanda"));
						domandan.setNote(rs.getString("Note"));
						domandan.setMinimo(Integer.parseInt(rs.getString("Minimo")));
						domandan.setMassimo(Integer.parseInt(rs.getString("Massimo")));
						break;
					case "Data":
						DomandaData domandad = new DomandaData();
						domandad.setID(Integer.parseInt(rs.getString("ID")));
						domandad.setID_Sondaggio(sondaggio.getID());
						domandad.setTesto(rs.getString("TestoDomanda"));
						domandad.setNote(rs.getString("Note"));
						domandad.setMinimo(LocalDate.parse(rs.getString("Minimo")));
						domandad.setMassimo(LocalDate.parse(rs.getString("Massimo")));
						break;
					case "Multipla":
						DomandaNumerica domandan = new DomandaNumerica();
						domandan.setID(Integer.parseInt(rs.getString("ID")));
						domandan.setID_Sondaggio(sondaggio.getID());
						domandan.setTesto(rs.getString("TestoDomanda"));
						domandan.setNote(rs.getString("Note"));
						domandan.setMinimo(Integer.parseInt(rs.getString("Minimo")));
						domandan.setMassimo(Integer.parseInt(rs.getString("Massimo")));
						break;
					case "Aperta Breve":
						break;
					case "Aperta Lunga":
						break;
					}

				}
			}

			return sondaggio;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}
}
