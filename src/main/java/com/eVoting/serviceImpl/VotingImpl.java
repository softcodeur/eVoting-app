package com.eVoting.serviceImpl;

import java.nio.ByteBuffer;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.eVoting.models.Condidat;
import com.eVoting.models.Electeur;
import com.eVoting.service.VotingSystem;
import com.eVoting.utils.Utils;
/**
 * @author Soumana Abdou
 *
 */
public class VotingImpl extends UnicastRemoteObject implements VotingSystem {

	
	private static final long serialVersionUID = 1L;
	public VotingImpl() throws RemoteException {
		super();

	}

	//Obtenir les resultats des votes à partir de la base de données.
	public List<Condidat> resultatDesVotes() {
		Session session = Utils.getSessionFactory().openSession();
		session.beginTransaction();
		Query q = session.createQuery("From Condidat");
		List<Condidat> resultList = q.list();
		session.close();
		return resultList;
	}
	
    //Obtenir la liste des candidats à partir de la base de données.
	public HashMap<Long, String> candidateList() {
		Session session = Utils.getSessionFactory().openSession();
		session.beginTransaction();
		Query q = session.createQuery("From Condidat");
		List<Condidat> resultList = q.list();
		session.close();
		if (resultList.size() == 0) {
			return null;
		}
		HashMap<Long, String> condidat = new HashMap<Long, String>();
		for (Condidat cdt : resultList) {

			condidat.put(cdt.getId(), cdt.getNom());
		}

		return condidat;
	}
   //Inscription d'un électeur 
	public Electeur register(String nom, String prenom, int age, String email, byte[] publicKey) throws RemoteException {
		
		Session session = Utils.getSessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("select electeur from Electeur as electeur where electeur.email = :email");
		query.setString("email", email);
		List result = query.list();
		if (result.size() == 0) {
			Electeur votant = new Electeur(nom, prenom, age, email, publicKey);
			votant = session.find(Electeur.class, session.save(votant));
			session.close();
			return votant;

		}
		session.close();
		return null;
	}
	
   //Cette méthode permet de voter 
	@SuppressWarnings("deprecation")
	public int castVote(byte[] messageSigne, PublicKey publicKey, String idCondidat, Long idVotant)
			throws NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		Signature signature = Signature.getInstance("SHA256WithDSA");
		signature.initVerify(publicKey);
		signature.update(idCondidat.getBytes());
		boolean verified = signature.verify(messageSigne);
		if (!verified) {
			return -1;
		} else {
			Session session = Utils.getSessionFactory().openSession();
			session.beginTransaction();
			String req = "FROM Electeur e WHERE e.id = :id";
			Query query = session.createQuery(req);
			query.setParameter("id", idVotant);
			List<Electeur> result = query.list();
			System.out.println(result.get(0));
			Electeur el = result.get(0);
			System.out.println(el.getNom());
			if (el.isVoting()) {
				session.close();
				return -2;
			} else {
				Condidat condidat = session.find(Condidat.class, Long.parseLong(idCondidat));
				condidat.setNbrVotes(condidat.getNbrVotes() + 1);
				session.saveOrUpdate(condidat);
				el.setVoting(true);
				session.saveOrUpdate(el);
				session.getTransaction().commit();
				session.close();
				return 0;
			}
		}
	}
	
	//Trouver un candidat connaissant son ID
	public String findCondidateById(Long id) throws RemoteException {
		Session session = Utils.getSessionFactory().openSession();
		session.beginTransaction();
		Condidat condidat = session.find(Condidat.class, id);
		if (condidat != null) {
			session.close();
			return condidat.getNom();

		}
		session.close();
		return null;
	}
	//Convert long to bytes
	public byte[] longToBytes(long x) {
		ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
		buffer.putLong(x);
		return buffer.array();
	}
	
	

}
