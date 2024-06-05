package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Ingrediente;

//public interface IngredienteRepository extends CrudRepository<Ingrediente,Long>{
//	public boolean existsByName(String nome);
//}
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {
    boolean existsByNome(String nome);
}

