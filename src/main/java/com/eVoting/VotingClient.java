package com.eVoting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.Signature;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.eVoting.models.Condidat;
import com.eVoting.models.Electeur;
import com.eVoting.service.VotingSystem;
/**
 * @author Soumana Abdou
 *
 */
public class VotingClient {

	public static void main(String[] args)
			throws RemoteException, MalformedURLException, NotBoundException, NoSuchAlgorithmException {
		VotingSystem stub = (VotingSystem) Naming.lookup("eVote.com");
		//Generation de la clé public
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DSA");
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		byte[] pubKey = keyPair.getPublic().getEncoded();
		Electeur votant = null;
		while (true) {
			System.out.println("*******E-Voting*********");
			System.out.println("************************");
			System.out.println("**********Menu**********");
			System.out.println(" 1.Register");
			System.out.println(" 2.condidate List");
			System.out.println(" 3.Voter");
			System.out.println(" 4.Résultat");
			System.out.println(" 5.Quitter");
			System.out.println("************************");
			System.out.println("******Votre choix********");
			try {
				InputStreamReader isr = new InputStreamReader(System.in);
				BufferedReader br = new BufferedReader(isr);
				String r = br.readLine();
				int input = Integer.parseInt(r);
				switch (input) {
				case 1:
					System.out.println(" Votre Nom:");
					String nom = br.readLine();
					System.out.println(" Votre Prenom:");
					String prenom = br.readLine();
					System.out.println(" Votre Age:");
					String age = br.readLine();
					System.out.println(" Votre Email:");
					String email = br.readLine();


					votant = stub.register(nom, prenom, Integer.valueOf(age), email, pubKey);
					if (votant != null) {
						System.out.println(" ");
						System.out.println("Vous êtes inscrit !");
						System.out.println("**********************");
					} else {
						System.out.println("User déjà existant !");
					}
					System.out.println(" Entrer pour continuer");
					try {
						System.in.read();
					} catch (Exception e) {
					}
					break;
				case 2:
					System.out.println("***********************************");
					System.out.println("***********Liste des condidats********");
					System.out.println("***********************************");
					System.out.println("| ID | NOM");
					HashMap<Long, String> listDesCondidats = stub.candidateList();
					for (Map.Entry<Long, String> entry : listDesCondidats.entrySet()) {
						System.out.println("| " + entry.getKey() + " | " + entry.getValue());
					}
					System.out.println(" Entrer pour continuer");
					try {
						System.in.read();
					} catch (Exception e) {
					}
					break;
				case 3:
					if (votant != null) {

						System.out
								.println("*************************************");
						System.out.println("Saisissez ID du condidat");
						System.out
								.println("**************************************");
						String identifiant = br.readLine();
						String nomCondidat = stub.findCondidateById(Long.parseLong(identifiant));
						if (nomCondidat == null) {
							System.out.println("Pas de candidat");
						} else {
							//Generation de la clé privé et signature du message
							Signature signature = Signature.getInstance("SHA256WithDSA");
							signature.initSign(keyPair.getPrivate(), secureRandom);
							signature.update(identifiant.getBytes());
							byte[] digitalSignature = signature.sign();
							int result = stub.castVote(digitalSignature, keyPair.getPublic(), identifiant, votant.getId());
							if (result == -1) {
								System.out.println("Signature non vérifié");

							} else if (result == -2) {
								System.out.println("Vous avez déjà voté");
							} else if (result == 0) {
								System.out.println("Vote enregistré");
							} else {
								System.out.println("Erreur Serveur");
							}
							System.out.println(" Entrer pour continuer");
							try {
								System.in.read();
							} catch (Exception e) {
							}
						}
					} else {
						System.out.println("Vous n'êtes pas inscrit ! ");
					}
					System.out.println(" Entrer pour continuer");
					try {
						System.in.read();
					} catch (Exception e) {
					}
					break;
				case 4:
					System.out.println("*****************************");
					System.out.println("************Résultat*************");
					System.out.println("************************");
					List<Condidat> condidats = stub.resultatDesVotes();
					System.out.println("*************************");
					System.out.println("NOM  | NBVOTES");
					for (Condidat condidate : condidats) {
						System.out.println(condidate.getNom() + "   |  " + condidate.getNbrVotes());
					}
					break;
				case 5:
					System.out.println("**********************");
					System.out.println("Déconnecter !...");
					System.out.println("**********************");
					System.exit(0);
					break;
				default:
					break;
				}
				System.out.println(input);
			} catch (Exception e) {
			}
		}
	}

}
