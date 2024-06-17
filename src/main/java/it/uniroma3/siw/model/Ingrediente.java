package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
@Entity
public class Ingrediente {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotNull
	private String nome;
	@NotNull
	private int quantita;
	
	private String misura;
	@ManyToMany(mappedBy="ingredienti")
	private List<Ricetta> ricette;
	
	public String getMisura() {
		return misura;
	}

	public void setMisura(String misura) {
		this.misura = misura;
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

	public int getQuantita() {
		return quantita;
	}

	public void setQuantita(int quantita) {
		this.quantita = quantita;
	}

	public List<Ricetta> getRicette() {
		return ricette;
	}

	public void setRicette(List<Ricetta> ricette) {
		this.ricette = ricette;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ingrediente other = (Ingrediente) obj;
		return Objects.equals(nome, other.nome);
	}

	
}
