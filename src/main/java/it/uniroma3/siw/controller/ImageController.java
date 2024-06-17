package it.uniroma3.siw.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.model.Image;
import it.uniroma3.siw.model.Ricetta;
import it.uniroma3.siw.repository.ImageRepository;
import it.uniroma3.siw.repository.RicettaRepository;

@Controller
public class ImageController {
	@Autowired private ImageRepository imageRepository;
	@Autowired private RicettaRepository ricettaRepository;
	
	
	/*GET E POST PER LA FORM AGGIUNIGI NUOVA IMMAGINE*/
	@GetMapping(value="/formNewImage/{idRicetta}")
	public String formNewImage(@PathVariable Long idRicetta,Model model) {
		model.addAttribute("ricetta", this.ricettaRepository.findById(idRicetta).orElse(null));
		return "formNewImage.html";
	}

	@PostMapping(value="/newImage/{idRicetta}",consumes = "multipart/form-data")
	public String newImage(@PathVariable Long idRicetta, @RequestPart("file") MultipartFile file,Model model) {
		Ricetta r=ricettaRepository.findById(idRicetta).orElse(null);
		try {
			Image i=new Image();
			i.setImageData(file.getBytes());
			i.setRicetta(r);
			r.getImages().add(i);
			this.imageRepository.save(i);
		} catch (Exception e) {
			System.out.println("erroreeee");
		}
		this.ricettaRepository.save(r);
		model.addAttribute("ricetta", r);
		return "redirect:/ricetta/"+r.getId();
	}
	
	/*GET PER RIMUOVI IMMAGINE*/
	@GetMapping("/rimuoviImmagine/{idRicetta}/{idImmagine}")
	public String rimuoviImmagine(@PathVariable Long idRicetta,@PathVariable Long idImmagine,Model model) {
		Ricetta r=ricettaRepository.findById(idRicetta).orElse(null);
		Image i=imageRepository.findById(idImmagine).orElse(null);
		r.getImages().remove(i);
		ricettaRepository.save(r);
		imageRepository.delete(i);
		return "redirect:/ricetta/"+r.getId();
	}
}
