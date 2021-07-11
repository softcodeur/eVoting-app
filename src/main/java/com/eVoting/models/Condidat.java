package com.eVoting.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
/**
 * @author Soumana Abdou
 *
 */
@Entity
public class Condidat implements Serializable {
	
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nom;
	@ColumnDefault("0")
	private int nbrVotes;

	public Condidat(Long id, String nom, int nbrVotes) {
		this.id = id;
		this.nom = nom;
		this.nbrVotes = nbrVotes;
	}

	public Condidat(String nom, int nbrVotes) {
		this.nom = nom;
		this.nbrVotes = nbrVotes;
	}

	public Condidat() {
		super();
	}

	public Condidat(String nom) {
		this.nom = nom;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public int getNbrVotes() {
		return nbrVotes;
	}

	public void setNbrVotes(int nbrVotes) {
		this.nbrVotes = nbrVotes;
	}

}
