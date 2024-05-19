package it.uniroma3.siw.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalController {
	@GetMapping(value="/")
	public String getIndext() {
		return "index.html";
	}

}
