package it.uniroma3.siw.controller;
import static it.uniroma3.siw.model.Credentials.CUOCO_ROLE;

import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.CredentialsRepository;
import it.uniroma3.siw.repository.CuocoRepository;
import it.uniroma3.siw.repository.ImageRepository;
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
	@Autowired private CredentialsRepository credentialsRepository;
	@Autowired private RicettaRepository ricettaRepository;
	@Autowired private ImageRepository imageRepository;


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

	@PostMapping(value={"/admin/cuoco"},consumes = "multipart/form-data")
	public String newCuoco(@Valid @ModelAttribute Cuoco cuoco,@RequestPart("file") MultipartFile file, BindingResult bindingResult, Model model) {
		//		this.movieValidator.validate(movie, bindingResult);
		if (!bindingResult.hasErrors()) {
			try {
				Image i=new Image();
				i.setImageData(file.getBytes());
				cuoco.setCopertina(i);
				this.imageRepository.save(i);
			} catch (Exception e) {
				System.out.println("erroreeee");
			}
			this.cuocoRepository.save(cuoco);
			return "redirect:/cuoco/"+cuoco.getId();
		} else {
			return "admin/formNewCuoco.html"; 
		}
	}

	/*GET RIMOZIONE DEL CUOCO*/
	@GetMapping("/admin/rimuoviCuoco/{idCuoco}")
	public String cancellaCuoco(@PathVariable("idCuoco") Long idCuoco, Model model) {
		UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
		Cuoco cuoco = this.cuocoRepository.findById(idCuoco).get();
		List<Ricetta> ricette = cuoco.getRicette();
		for(Ricetta r: ricette) {
			r.setCuoco(null);
		}
	    this.cuocoRepository.save(cuoco);
		Iterable<Credentials> allCredentials = this.credentialsRepository.findAll();
		for(Credentials i: allCredentials) {
			if(i.getCuoco() != null) {
				if(i.getCuoco().getId() == idCuoco) {
					if(!i.getRole().equals(Credentials.ADMIN_ROLE)) {
		                i.setCuoco(null);
		                this.credentialsRepository.delete(i);
		            }
				}
			}
		}
		this.cuocoRepository.delete(cuoco);
		model.addAttribute("user", this.userRepository.findById(credentials.getUser().getId()).get());
		return "dettagliAdmin.html";
	}
	
	/*GET PER SETTARE UN CUOCO A UNA RICETTA*/
	@GetMapping(value="/admin/{idRicetta}/setCuoco")
	public String setCuoco(@PathVariable Long idRicetta,Model model) {
		Ricetta r=ricettaRepository.findById(idRicetta).orElse(null);
		model.addAttribute("cuochi",this.cuocoRepository.findAll());
		model.addAttribute("ricetta",r);
		return "admin/setCuoco.html";
	}
	@GetMapping(value="/admin/{idRicetta}/{idCuoco}")
	public String setCuocoRicetta(@PathVariable Long idRicetta,@PathVariable Long idCuoco,Model model) {
		Ricetta r=ricettaRepository.findById(idRicetta).orElse(null);
		Cuoco c=cuocoRepository.findById(idCuoco).orElse(null);
		r.setCuoco(c);
		c.getRicette().add(r);
		ricettaRepository.save(r);
		cuocoRepository.save(c);
		return "redirect:/ricetta/"+r.getId();
		
	}
	
}
