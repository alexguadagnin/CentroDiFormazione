package it.uniroma3.siw.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

@Entity
public class Esame {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotBlank
	private String data;

	private String ora;
	
	private String aula;

	@ManyToOne
	private Certificazione certificazione;

	@ManyToMany(mappedBy = "iscrizioni")
	private List<Utente> iscritti;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getOra() {
		return ora;
	}

	public void setOra(String ora) {
		this.ora = ora;
	}

	public String getAula() {
		return aula;
	}

	public void setAula(String aula) {
		this.aula = aula;
	}

	public Certificazione getCertificazione() {
		return certificazione;
	}

	public void setCertificazione(Certificazione certificazione) {
		this.certificazione = certificazione;
	}

	public List<Utente> getIscritti() {
		return iscritti;
	}

	public void setIscritti(List<Utente> iscritti) {
		this.iscritti = iscritti;
	}

}
