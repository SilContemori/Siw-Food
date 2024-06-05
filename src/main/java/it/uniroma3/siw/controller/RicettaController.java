package it.uniroma3.siw.controller;



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
import static it.uniroma3.siw.model.Credentials.CUOCO_ROLE;
import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.CuocoRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;

@Controller
public class RicettaController {
	@Autowired private RicettaRepository ricettaRepository;
	@Autowired private CuocoRepository cuocoRepository;
	@Autowired private CredentialsService credentialsService;

	/*GET PAGINA CON LISTA RICETTE*/
	@GetMapping("/ricetta")
	public String getRicette(Model model){
		model.addAttribute("ricette",this.ricettaRepository.findAll());
		return "ricette.html";
	}

	/*GET DEI DETTAGLI DELLA RICETTA*/
	@GetMapping("/ricetta/{id}")
	public String getRicetta(@PathVariable Long id,Model model){
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Ricetta r=this.ricettaRepository.findById(id).get();
		if(r.getCuoco()!=null) {
			if(credentials.getCuoco()!=null) {
				Long idCuoco=credentials.getCuoco().getId();
				if(r.getCuoco().getId()==idCuoco) {
					model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
					return "ricettaCuoco.html";
				}
			}
		}
		if(credentials.getRole().equals(ADMIN_ROLE)) {
			model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
			return "ricettaCuoco.html";
		}
		model.addAttribute("ricetta", this.ricettaRepository.findById(id).get());
		return "ricetta.html";

	}

	/*GET DELLE RICETTE DIVISE PER TIPOLOGIA*/
	@GetMapping("/ricettePerTipo")
	public String getRicettePerTipo(Model model) {
		model.addAttribute("ricette",this.ricettaRepository.findAll());
		return "ricettePerTipo.html";
	}

	/*GET  E POST PER LA FORM NEW RICETTE*/
	@GetMapping(value="/formNewRicetta")
	public String formNewRicetta(Model model) {
		model.addAttribute("ricetta", new Ricetta());
		return "formNewRicetta.html";
	}

	@PostMapping("/ricetta")
	public String newRicetta(@Valid @ModelAttribute Ricetta ricetta, BindingResult bindingResult, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		if(credentials.getRole().equals(CUOCO_ROLE)) {
			Cuoco c=credentials.getCuoco();
			//		this.movieValidator.validate(movie, bindingResult);
			if (!bindingResult.hasErrors()) {
				ricetta.setCuoco(c);
				this.ricettaRepository.save(ricetta);
				c.getRicette().add(ricetta);
				this.cuocoRepository.save(c); 
				model.addAttribute("ricetta", ricetta);
				return "ricetta.html";
			} else {
				return "formNewRicetta.html"; 
			}
		}else {
			if (!bindingResult.hasErrors()) {
				this.ricettaRepository.save(ricetta); 
				model.addAttribute("ricetta", ricetta);
				return "ricetta.html";
			}else {
				return "formNewRicetta.html"; 
			}
		}
	}

	/*GET PAGINA CON LISTA RICETTE MODIFICABILI*/
	@GetMapping("/admin/ricette")
	public String getRicetteModificabili(Model model){
		model.addAttribute("ricette",this.ricettaRepository.findAll());
		return "admin/ricetteModificabili.html";
	}

}
