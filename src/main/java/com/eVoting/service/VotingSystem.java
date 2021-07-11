package com.eVoting.service;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.util.HashMap;
import java.util.List;
import com.eVoting.models.Condidat;
import com.eVoting.models.Electeur;
/**
 * @author Soumana Abdou
 *
 */

public interface VotingSystem extends Remote {

	public Electeur register(String nom, String prenom, int age, String email, byte[] publicKey) throws RemoteException;

	public List<Condidat> resultatDesVotes() throws RemoteException;

	public HashMap<Long, String> candidateList() throws RemoteException;

	public int castVote(byte[] messageSigne, PublicKey publicKey, String idCondidat,Long idVotant) throws RemoteException, NoSuchAlgorithmException, SignatureException, InvalidKeyException;
	
	public String findCondidateById(Long id) throws RemoteException;

}
