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

import domain.Utente;

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
				System.out.println("__________________________________________________________________________________");
				System.out.println();
				count ++;
			}
			
			if (count == 0) {
				System.out.println("non sei abilitato a compilare nessun sondaggio!");
				System.out.println();
				System.out.println("__________________________________________________________________________________");
				System.out.println();
			}

		} catch (SQLException e) {
			System.out.println("errore nella visualizzazione dei messaggi");
		}

	}

}
