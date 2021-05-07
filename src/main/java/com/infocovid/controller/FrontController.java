package com.infocovid.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infocovid.model.Categorie;
import com.infocovid.model.Compte;
import com.infocovid.model.Information;
import com.infocovid.model.Statistique;

@Controller
@CrossOrigin(origins="*",allowedHeaders="*")
public class FrontController {
	@GetMapping(value="/")
	public String index() {
		return "redirect:les-derenieres-informations";
	}
	@GetMapping(value="les-derenieres-informations")
	public String acceuil(Model model) throws Exception {
		String  sql="select * from information order by dates Desc";
		ArrayList<Information> info=Information.findAll(sql);
		model.addAttribute("titre","Les dernières informations:");
		model.addAttribute("descri", "Les dernières informations sur le coronavirus à Madagascar et dans le monde");
		model.addAttribute("motsC", "dernières,informations,coronavirus,Madagascar,Monde");
		model.addAttribute("info",info);
		model.addAttribute("categorie", Categorie.findAllCategorie());
		return "front/acceuil";
	}
	@GetMapping(value="les-derenieres-informations/{idCategorie}")
	public String parCategorie(Model model,@PathVariable String idCategorie) throws Exception {
		Categorie sel=Categorie.findCategorie(idCategorie).get(0);
		String  sql="select * from information where idCategorie="+sel.getIdCategorie()+"order by dates Desc";
		ArrayList<Information> info=Information.findAll(sql);
		model.addAttribute("titre","Les dernières informations: "+sel.getCategorie());
		model.addAttribute("descri", "Les dernières informations sur le coronavirus :"+sel.getCategorie());
		model.addAttribute("motsC", "dernières,informations,coronavirus,"+sel.getCategorie());
		model.addAttribute("info",info);
		model.addAttribute("categorie", Categorie.findAllCategorie());
		return "front/acceuil";
	}
	@GetMapping(value="article/{url}")
	public String article(Model model,@PathVariable String url) throws Exception {
		Information sel=Information.findInformationByUrl(url);
		model.addAttribute("titre",sel.getTitre());
		model.addAttribute("info",sel);
		model.addAttribute("descri", sel.getTitre());
		model.addAttribute("motsC", sel.getMotsCle());
		model.addAttribute("assoc", Information.getInformationCategorie(sel.getIdCategorie()));
		model.addAttribute("categorie", Categorie.findAllCategorie());
		return "front/article";
	}
	@GetMapping(value="statistique")
	public String stat(Model model) throws Exception {
		model.addAttribute("titre","Les statistiques de coronavirus");
		model.addAttribute("descri", "Les statistiques du coronavirus dans le monde et à Madagascar");
		model.addAttribute("motsC", "dernières,statistiques,coronavirus,madagascar,monde,covid19");
		model.addAttribute("state",Statistique.getLastStat());
		model.addAttribute("categ", Categorie.findAllCategorie());
		model.addAttribute("data", Statistique.getDataView());
		model.addAttribute("total", Statistique.getTotal());
		model.addAttribute("stat", new Statistique());
		return "front/stat";
	}
	@RequestMapping(value = "sitemap.xml", method = RequestMethod.GET, produces = { MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public String sitemap() throws IOException {
		Resource resource = new ClassPathResource("sitemap.xml");
		Reader reader = new InputStreamReader(resource.getInputStream());
		return FileCopyUtils.copyToString(reader);
	}
}
