package com.eVoting.utils;

import java.io.IOException;
import java.io.Serializable;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignedObject;
/**
 * @author Soumana Abdou
 *
 */
public abstract class Helper {

	private static final String SIGNING_ALGORITHM = "SHA256withRSA";
	private static final String RSA = "RSA";

	public static KeyPair genererKeys() throws Exception {
		SecureRandom secureRandom = new SecureRandom();
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA);
		keyPairGenerator.initialize(2048, secureRandom);
		return keyPairGenerator.generateKeyPair();
	}
	
	public static SignedObject signerObjet(Object input, PrivateKey key) throws Exception {
		Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
		SignedObject signedObject = new SignedObject((Serializable) input, key, signature);
		return signedObject;
	}

	public static boolean verifierObjet(SignedObject input, PublicKey key) throws Exception {
		Signature signature = Signature.getInstance(SIGNING_ALGORITHM);
	    boolean verified = input.verify(key, signature);
		return verified;
	}
	
	
	public static Object getObjet(SignedObject input) throws ClassNotFoundException, IOException {
		return input.getObject();
	}
	

}
