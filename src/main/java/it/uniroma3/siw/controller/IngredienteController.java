package it.uniroma3.siw.controller;

import static it.uniroma3.siw.model.Credentials.CUOCO_ROLE;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ingrediente;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.IngredienteRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class IngredienteController {
	
	@Autowired IngredienteRepository ingredienteRepository;
	@Autowired RicettaRepository ricettaRepository;
	@Autowired CredentialsService credentialsService;
	
	/*GET  E POST PER LA FORM NEW INGREDIENTE*/
	@GetMapping(value="/formNewIngrediente/{idRicetta}")
	public String formNewIngrediente(@PathVariable Long idRicetta,Model model) {
		model.addAttribute("ingrediente", new Ingrediente());
		model.addAttribute("ricetta", this.ricettaRepository.findById(idRicetta).orElse(null));
		return "formNewIngrediente.html";
	}
	
	@PostMapping("/newIngrediente/{idRicetta}")
	public String newIngrediente(@PathVariable Long idRicetta,@Valid @ModelAttribute Ingrediente ingrediente, BindingResult bindingResult, Model model) {
		Ricetta r=ricettaRepository.findById(idRicetta).orElse(null);
		if(!this.ingredienteRepository.existsByNome(ingrediente.getNome())) {
			r.getIngredienti().add(ingrediente);
			this.ingredienteRepository.save(ingrediente);
			this.ricettaRepository.save(r);
			model.addAttribute("ricetta", r);
			return "ricetta.html";			
		}else {
			model.addAttribute("ricetta", r);
			return "ricetta.html";
		}
	}

}
