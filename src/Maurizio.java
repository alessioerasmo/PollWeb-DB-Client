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
			 * CICLO WHILE MENU'
			 */

			int exit = 0;
			int risposta = 0;
			String messaggio;
			while (exit != 1) {
				System.out.println();
				System.out.println("Cosa vuoi fare? (Seleziona il numero corrispondente all'azione desiderata)");
				System.out.println("1) Visualizza Info su di me");
				System.out.println("2) Visualizza Sondaggi da compilare");
				System.out.println("3) Compila un sondaggio");
				System.out.println("4) Crea un sondaggio");
				System.out.println("4) Modifica un sondaggio");
				System.out.println("5) Esci");

				risposta = Integer.parseInt(reader.readLine());

				messaggio = "tornare al menù";
				switch (risposta) {
				case 2:
					System.out.println();
					ClientFunction.MostraSondaggiByUser(utente, con);
					break;
				case 3:
					Sondaggio sondaggio = ClientFunction.CompilaSondaggio(utente, con);
					if (sondaggio != null) {
						System.out.println();
						System.out.println("Compilazione sondaggio: " + sondaggio.getTitolo());
						System.out.println();
						System.out.println(sondaggio.getApertura());
					}
					break;
				case 5:
					exit = 1;
					messaggio = "uscire";
					break;
				case 1:
					ClientFunction.MostraInfoUtente(utente, con);
					break;
				}
				System.out.println();
				System.out.println("premi Enter per " + messaggio);
				reader.readLine();
			}

			con.close();

		} catch (SQLException e) {

			e.printStackTrace();
			System.out.println("Connessione Fallita! :(");

		}

	}

}
