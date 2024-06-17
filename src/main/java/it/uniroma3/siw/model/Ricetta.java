package it.uniroma3.siw.model;

import java.util.List;
import java.util.Objects;

import org.apache.tomcat.util.codec.binary.Base64;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
@Entity
public class Ricetta {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	@NotNull
	private String nome;
	@NotNull
	private Float tempoPreparazioneTot;//min
	@Column(length=2000)
	private String descrizione;
	@NotNull
	private String tipologia; //se primo,secondo,contorno,dolce
	
	@OneToMany
	private List<Image> images;
	
	@OneToOne
	private Image copertina;
	
	@ManyToOne
	private Cuoco cuoco;
	
	@ManyToMany
	private List<Ingrediente> ingredienti;

	public List<Image> getImages() {
		return images;
	}
	public void setImages(List<Image> images) {
		this.images = images;
	}
	public Image getCopertina() {
		return copertina;
	}
	public void setCopertina(Image copertina) {
		this.copertina = copertina;
	}
	public String getTipologia() {
		return tipologia;
	}
	public void setTipologia(String tipologia) {
		this.tipologia = tipologia;
	}
	public Cuoco getCuoco() {
		return cuoco;
	}
	public void setCuoco(Cuoco cuoco) {
		this.cuoco = cuoco;
	}
	public List<Ingrediente> getIngredienti() {
		return ingredienti;
	}
	public void setIngrediente(List<Ingrediente> ingredienti) {
		this.ingredienti = ingredienti;
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
	public Float getTempoPreparazioneTot() {
		return tempoPreparazioneTot;
	}
	public void setTempoPreparazioneTot(Float tempoPreparazioneTot) {
		this.tempoPreparazioneTot = tempoPreparazioneTot;
	}
	public String getDescrizione() {
		return descrizione;
	}
	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}
	@Override
	public int hashCode() {
		return Objects.hash(descrizione, nome, tempoPreparazioneTot, tipologia);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Ricetta other = (Ricetta) obj;
		return Objects.equals(descrizione, other.descrizione) && Objects.equals(nome, other.nome)
				&& Objects.equals(tempoPreparazioneTot, other.tempoPreparazioneTot)
				&& Objects.equals(tipologia, other.tipologia);
	}
	
	
}
