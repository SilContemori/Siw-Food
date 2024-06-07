package it.uniroma3.siw.controller;
import static it.uniroma3.siw.model.Credentials.CUOCO_ROLE;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
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
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.CuocoRepository;
import it.uniroma3.siw.repository.RicettaRepository;
import it.uniroma3.siw.repository.UserRepository;
import it.uniroma3.siw.service.CredentialsService;
import jakarta.validation.Valid;
import static it.uniroma3.siw.model.Credentials.ADMIN_ROLE;


@Controller
public class CuocoController {
	@Autowired private CuocoRepository cuocoRepository;
	@Autowired private CredentialsService credentialsService;
	@Autowired private UserRepository userRepository;


	/*GET PAGINA CON LISTA DEI CUOCHI*/
	@GetMapping("/cuoco")
	public String getCuochi(Model model){
		model.addAttribute("cuochi",this.cuocoRepository.findAll());
		return "cuochi.html";
	}

	/*GET DEI DETTAGLI DEL CUOCO*/
	@GetMapping("/cuoco/{id}")
	public String getCuoco(@PathVariable Long id,Model model){
		model.addAttribute("cuoco", this.cuocoRepository.findById(id).get());
		return "cuoco.html";
	}
	
	/*GET DEI CUOCHI ELIMINABILI*/
	@GetMapping("/admin/cuochi")
	public String getCuochiEliminabili(Model model){
		model.addAttribute("cuochi",this.cuocoRepository.findAll());
		return "admin/cuochiModificabili.html";
	}

	/*GET DELLA PAGINA DESCRITTIVA DELL'UTENTE*/
	@GetMapping(value = "/dettagliUser") 
	public String getUserPage(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return "login.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.CUOCO_ROLE)) {
				//				return "admin/indexAdmin.html";
				model.addAttribute("cuoco", this.cuocoRepository.findById(credentials.getCuoco().getId()).get());
				return "dettagliCuoco.html";
			}else if(credentials.getRole().equals(Credentials.DEFAULT_ROLE)){
				model.addAttribute("user", this.userRepository.findById(credentials.getUser().getId()).get());
				return "dettagliUser.html";
			}else {
				model.addAttribute("user", this.userRepository.findById(credentials.getUser().getId()).get());
				return "dettagliAdmin.html";
			}
		}
	}

	/*GET  E POST PER LA FORM NEW CUOCO*/
	@GetMapping(value="/admin/formNewCuoco")
	public String formNewCuoco(Model model) {
		model.addAttribute("cuoco", new Cuoco());
		return "admin/formNewCuoco.html";
	}

	@PostMapping("/admin/cuoco")
	public String newCuoco(@Valid @ModelAttribute Cuoco cuoco, BindingResult bindingResult, Model model) {
		//		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			this.cuocoRepository.save(cuoco);
			model.addAttribute("cuoco", cuoco);
			return "cuoco.html";
		} else {
			return "admin/formNewCuoco.html"; 
		}

	}

	/*GET RIMOZIONE DEL CUOCO E TUTTE LE SUE RICETTE*/
	@GetMapping("/admin/rimuoviCuoco/{id}")
	public String removeRicetta(@PathVariable("id") Long id, Model model) {
		System.out.println("dopo la remove------------------------------------------------------");
		UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Optional<Cuoco> daEliminareOptional = cuocoRepository.findById(id);

		if (daEliminareOptional.isPresent()) {
			Cuoco daEliminare = daEliminareOptional.get();
//			if (credentials.getRole().equals(ADMIN_ROLE)) {
				//				Cuoco c = daEliminare.getCuoco();
				//				if (c!= null) {
				//					c.getRicette().remove(daEliminare);
				//					daEliminare.setCuoco(null);
				//				}

				daEliminare.setRicette(null);
				cuocoRepository.delete(daEliminare);
				//				ricettaRepository.save(ricettaRepository.findAll());
				//				cuocoRepository.save(c);
				//				model.addAttribute("cuoco", c);
				model.addAttribute("user", this.userRepository.findById(credentials.getUser().getId()).get());
				return "dettagliAdmin.html";
//			} 
		} else {
			return "errore.html";
		}
	}


}
