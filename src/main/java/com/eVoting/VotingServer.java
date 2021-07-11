package com.eVoting;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.hibernate.Session;

import com.eVoting.models.Condidat;
import com.eVoting.service.VotingSystem;
import com.eVoting.serviceImpl.VotingImpl;
import com.eVoting.utils.Utils;
/**
 * @author Soumana Abdou
 *
 */

public class VotingServer {
	public static void main(String[] args) throws RemoteException, MalformedURLException {
		VotingSystem skeleton = new VotingImpl();
		LocateRegistry.createRegistry(1099);
		Naming.rebind("eVote.com", skeleton);
		Session session = Utils.getSessionFactory().openSession();
		session.beginTransaction();
		Condidat c1 = new Condidat("Mohamed Ali");
		session.save(c1);
		Condidat c2 = new Condidat("Soumana Abdou");
		session.save(c2);
		Condidat c3 = new Condidat("Azara Hassan");
		session.save(c3);
		Condidat c4 = new Condidat("Mustapha Hamza");
		session.save(c4);
		Condidat c5 = new Condidat("Ousman Diallo");
		session.save(c5);
		session.getTransaction().commit();
		session.close();

	}
}
