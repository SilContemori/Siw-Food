package it.uniroma3.siw.model;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Entity
public class Cuoco {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String nome;
	@NotBlank
	private String cognome;
	@NotBlank
	private String email;
	
	private LocalDate  dataDiNascita;
	
//	private String luogoDiNascita;
//	private String descrizione;
	
	@OneToMany(mappedBy="cuoco")
	private List<Ricetta> ricette;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
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
	public LocalDate getDataDiNascita() {
		return dataDiNascita;
	}
	public void setDataDiNascita(LocalDate dataDiNascita) {
		this.dataDiNascita = dataDiNascita;
	}
//	public String getLuogoDiNascita() {
//		return luogoDiNascita;
//	}
//	public void setLuogoDiNascita(String luogoDiNascita) {
//		this.luogoDiNascita = luogoDiNascita;
//	}
//	public String getDescrizione() {
//		return descrizione;
//	}
//	public void setDescrizione(String descrizione) {
//		this.descrizione = descrizione;
//	}
	@Override
	public int hashCode() {
		return Objects.hash(cognome, dataDiNascita, nome);
	}
	
	public List<Ricetta> getRicette() {
		return ricette;
	}
	public void setRicette(List<Ricetta> ricette) {
		this.ricette = ricette;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuoco other = (Cuoco) obj;
		return Objects.equals(cognome, other.cognome) && Objects.equals(dataDiNascita, other.dataDiNascita)
				&& Objects.equals(nome, other.nome);
	}
	
}
