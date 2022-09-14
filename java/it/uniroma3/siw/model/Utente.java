package it.uniroma3.siw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotBlank;


@Entity
public class Utente {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@NotBlank
	private String nome;
	
	@NotBlank
	private String cognome;

	@ManyToMany()
	private List<Esame> iscrizioni;

	
	@OneToOne(mappedBy = "utente")
	private Credenziali credenziali;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public List<Esame> getIscrizioni() {
		return iscrizioni;
	}

	public void setIscrizioni(List<Esame> iscrizioni) {
		this.iscrizioni = iscrizioni;
	}

	public Credenziali getCredenziali() {
		return credenziali;
	}

	public void setCredenziali(Credenziali credenziali) {
		this.credenziali = credenziali;
	}

	public String getUsername() {
		return this.credenziali.getUsername();
	}
	
	public void setUsername(String username) {
		this.credenziali.setUsername(username);
	}
	
	public String getPassword() {
		return this.credenziali.getPassword();
	}
	
	public void setPassword(String password) {
		this.credenziali.setPassword(password);
	}
}
