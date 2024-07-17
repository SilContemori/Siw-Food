package it.uniroma3.siw.controller;



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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.Cuoco;
import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.CuocoService;
import it.uniroma3.siw.service.UserService;
import jakarta.validation.Valid;


@Controller
public class AuthenticationController {
	@Autowired
	private CredentialsService credentialsService;
	@Autowired private UserService userService;
	@Autowired private CuocoService cuocoService;
	@Autowired private ImageRepository imageRepository;
	
	
	/*GET DELLA HOME PAGE*/
	@GetMapping(value = "/") 
	public String index(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
	        return "index.html";
		}
		else {		
			UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
			if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
				return "index.html";
			}
		}
        return "index.html";
	}
	
	/*GET DELLA PAGINA DI LOG-IN CON LA FORM PER INSERIRE I DATI*/
	@GetMapping(value = "/login") 
	public String showLoginForm (Model model) {
		return "logIn.html";
	}
	
	/*GET DELLA PAGINA PER REGISTRARE I DATI E POST PER INSERIRE I DATI NEL DB*/
	@GetMapping(value = "/register") 
	public String showRegisterForm (Model model) {
		model.addAttribute("user", new User());
		model.addAttribute("credentials", new Credentials());
		return "signUp.html";
	}
	
	@PostMapping(value = { "/register" })
    public String registerUser(@Valid @ModelAttribute("user") User user,
                 BindingResult userBindingResult, @Valid@ModelAttribute("credentials")Credentials credentials,BindingResult credentialsBindingResult,
                 Model model) {
        if(!userBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
            userService.saveUser(user);
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("user", user);
            return "logIn.html";
        }
        return "signUp.html";
    }
	
	/*GET DELLA PAGINA PER REGISTRARE I DATI E POST PER INSERIRE I DATI NEL DB DEI CUOCHI*/
	@GetMapping(value = "/registerCuoco") 
	public String showRegisterCuocoForm (Model model) {
		model.addAttribute("cuoco", new Cuoco());
		model.addAttribute("credentials", new Credentials());
		return "signUpCuoco.html";
	}
	
	@PostMapping(value = { "/registerCuoco" },consumes = "multipart/form-data")
    public String registerCuoco(@Valid @ModelAttribute("cuoco") Cuoco cuoco,BindingResult cuocoBindingResult,
                  @Valid@ModelAttribute("credentials") Credentials credentials,BindingResult credentialsBindingResult,
                  @RequestPart("file") MultipartFile file,
                 Model model) {
        if(!cuocoBindingResult.hasErrors() && !credentialsBindingResult.hasErrors()) {
        	try {
				Image i=new Image();
				i.setImageData(file.getBytes());
				cuoco.setCopertina(i);
				this.imageRepository.save(i);
			} catch (Exception e) {
				System.out.println("erroreeee");
			}
        	cuocoService.saveCuoco(cuoco);
            credentials.setCuoco(cuoco);
            credentialsService.saveCredentials(credentials);
            model.addAttribute("cuoco", cuoco);
            return "logIn.html";
        }
        return "signUpCuoco.html";
    }
	
	/*GET CHE MOSTRA LA HOME PAGE (O PAGINA DEDICATA A SECONDA DEL RUOLO) DOPO LOG IN*/
	@GetMapping(value = "/success")
    public String defaultAfterLogin(Model model) {
    	UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	Credentials credentials = credentialsService.getCredentials(userDetails.getUsername());
    	if (credentials.getRole().equals(Credentials.ADMIN_ROLE)) {
    		return "index.html";
        }
        return "index.html";
    }
	
	/*GET DELLA PAGINA DELLA SCELTA DEL RUOLO*/
	@GetMapping(value = "/sceltaRuolo") 
	public String showSceltaRuolo (Model model) {
		return "sceltaRuolo.html";
	}
	
//	/*GET DELLA PAGINA DI ERRORE*/
//	@GetMapping("/errore")
//	public String getErrore() {
//		return "errore.html";
//	}
	
	
	
}