import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;

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
						System.out.println();

						ArrayList<Domanda> domande = sondaggio.getDomande();

						System.out.println("premi Enter per visualizzare tutte le domande a cui dovrai rispondere");
						System.out.println();
						reader.readLine();
						Iterator<Domanda> itr = domande.iterator();
						while (itr.hasNext()) {
							Domanda dom = itr.next();
							System.out.println();
							System.out.println(dom.getPosizione() + ") " + dom.getTesto());
							System.out.println("Note: " + dom.getNote());
							System.out.println("Tipo Domanda: " + dom.getTipo());
						}

						System.out.println();
						System.out.println("premi Enter per iniziare a rispondere");
						reader.readLine();
						System.out.println();

						Statement stmt = con.createStatement();
						itr = domande.iterator();
						while (itr.hasNext()) {
							Domanda dom = itr.next();
							System.out.println();
							System.out.println(dom.getPosizione() + ") " + dom.getTesto());
							System.out.println("Note: " + dom.getNote());

							if (dom.getTipo().equals("Numerica")) {
								System.out.println("NB: inserire un valore intero compreso tra " + dom.getMinimo()
										+ " e " + dom.getMassimo());
								String risp = reader.readLine();
								if ((stmt.executeUpdate("CALL RispostaNumerica(" + utente.getID() + ", " + dom.getID()
										+ ", " + risp + ")")) != 1)
									break;

							}
							if (dom.getTipo().equals("Data")) {
								System.out.println("NB: inserire un valore compreso tra " + dom.getMinimo() + " e "
										+ dom.getMassimo() + " nel formato YYYY-MM-DD");
								String risp = reader.readLine();
								if ((stmt.executeUpdate("CALL RispostaData(" + utente.getID() + ", " + dom.getID()
										+ ", '" + risp + "')")) != 1)
									break;

							}
							if (dom.getTipo().equals("Multipla")) {
								System.out.println("NB: selezionare almeno " + dom.getMinimo() + " risposte ma massimo "
										+ dom.getMassimo() + "Tra le seguenti");

								ResultSet rs = stmt.executeQuery(
										"SELECT r.ID, r.Testo FROM maurizio.associazione_scelta_multipla as a JOIN rispostaselezionabile as r WHERE (a.ID_Risposta = r.ID AND ID_Domanda = "
												+ dom.getID() + ");");

								ArrayList<RispostaSelezionabile> risp = new ArrayList<RispostaSelezionabile>();
								int label = 0;
								while (rs.next()) {
									RispostaSelezionabile rispsel = new RispostaSelezionabile();
									rispsel.setID(rs.getInt("ID"));
									rispsel.setTesto(rs.getString("Testo"));
									rispsel.setLabel(++label);
									System.out.println(label + ") " + rispsel.getTesto());
									risp.add(rispsel);
								}

								int rispostedate = 0;
								String volont� = "si";
								stmt.executeUpdate("CALL DeselezionaRisposteMultiple(" + utente.getID() + ", "
										+ dom.getID() + ", 0 )");
								while ((rispostedate < dom.getMassimo()) && volont�.equals("si")) {

									String num = reader.readLine();
									int numero = Integer.parseInt(num);
									if ((numero < 1) || (numero > label)) {
										System.out.println("inserire un valore valido");
									} else {

										Iterator<RispostaSelezionabile> it = risp.iterator();

										while (it.hasNext()) {
											RispostaSelezionabile r = it.next();
											if (r.getLabel() == numero) {

												if ((stmt.executeUpdate(
														"CALL SelezionaRispostaMultipla(" + utente.getID() + ", "
																+ dom.getID() + ", " + r.getID() + ")")) == 0)
													break;

												rispostedate++;

											}
										}

									}

									if (rispostedate < dom.getMassimo() && rispostedate >= dom.getMinimo()) {
										System.out.println();
										System.out.println(
												"Hai selezionato un numero sufficiente di risposte, se vuoi selezionarne altre? (scrivi si o no)");

										if (reader.readLine().equalsIgnoreCase("no"))
											volont� = "no";

									}
								}

							}
							if (dom.getTipo().equals("Aperta Breve")) {

								System.out.println("NB: utilizzare poche parole");
								String risp = reader.readLine();
								if ((stmt.executeUpdate("CALL RispostaApertaBreve(" + utente.getID() + ", "
										+ dom.getID() + ", '" + risp + "')")) != 1)
									break;
							}
							if (dom.getTipo().equals("Aperta Lunga")) {

								String risp = reader.readLine();
								if ((stmt.executeUpdate("CALL RispostaApertaLunga(" + utente.getID() + ", "
										+ dom.getID() + ", '" + risp + "')")) != 1)
									break;
							}
						}

						System.out.println();
						System.out.println(sondaggio.getChiusura());
						System.out.println();
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
