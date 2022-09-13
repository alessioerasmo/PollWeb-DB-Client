import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import SQLinterface.*;
import domain.*;

public class Maurizio {

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		System.out.println("Premi il tasto enter per avviare il programma");

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		reader.readLine();

		try {
			/*
			 * CONNESSIONE AL DB
			 */
			System.out.println("Connessione alla base di dati ... ");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/maurizio", "root", "0938");
			System.out.println("Connessione riuscita! ");
			System.out.println();

			/*
			 * LOGIN o REGISTRAZIONE
			 */
			Utente utente = null;
			while (utente == null) {

				System.out.println("scrivi 'a' per effettuare l'accesso, 'r' per registrarti al sistema");
				String risposta = reader.readLine();

				if (risposta.equals("a"))
					utente = ClientFunction.loginUtente(con);
				else if (risposta.equals("r"))
					utente = ClientFunction.creaUtente(con);

				if (utente != null)
					System.out.println("Benvenuto " + utente.getUsername() + "!");
			}
			/*
			 * Mostra i sodaggi che può compilare
			 */
			System.out.println();
			ClientFunction.MostraSondaggiByUser(utente, con);

			
		con.close();

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("Connessione Fallita! :(");

		}

	}

}
