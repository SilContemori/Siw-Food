package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.RicettaRepository;

@Controller
public class RicettaController {
	@Autowired RicettaRepository ricettaRepository;
	
	@GetMapping("/ricetta")
	public String getRicette(Model model){
		model.addAttribute("ricette",this.ricettaRepository.findAll());
		return "ricette.html";
	}
	
	@GetMapping("/ricetta/{id}")
	public String getRicette(@PathVariable Long id,Model model){
		model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
		return "ricetta.html";
	}
	
}
