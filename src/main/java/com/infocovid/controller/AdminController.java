package com.infocovid.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.infocovid.model.Categorie;
import com.infocovid.model.Compte;
import com.infocovid.model.Information;
import com.infocovid.model.Statistique;

@CrossOrigin(origins="*",allowedHeaders="*")
@Controller
public class AdminController {

	@GetMapping(value="/admin/login")
	public String index(Model model) {
		model.addAttribute("compte", new Compte());
		return "/admin/login";
	}
	@PostMapping(value="admin/login")
	public String login(@ModelAttribute Compte compte,Model model,HttpSession session) throws Exception{
		String view=null;
		try {
			String token=compte.login();
			session.setAttribute("token", token);
			view="redirect:listeInformation";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			view="/admin/login";
		}
		return view;
	}
	@GetMapping(value="admin/deconnect")
	public String lougout(Model model,HttpSession session) throws Exception {
		Compte.deconnect((String)session.getAttribute("token"));
		session.setAttribute("token", null);
		return "redirect:login";
	}
	@GetMapping(value="admin/listeInformation")
	public String listeInformation(Model model,HttpSession session) throws Exception {
		ArrayList<Information> info=Information.findAll("select * from information order by dates DESC");
		session.setAttribute("nbInfo", info.size());
		model.addAttribute("info",info);
		return "/admin/listeInformation";
	}
	@GetMapping("admin/deleteInfo/{id}")
    public String deleteBuyer(@PathVariable String id,Model model){
		try {
			Information.deleteInformation(id);
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
		}
        return "redirect:../listeInformation";
    }
	@GetMapping(value="admin/ajoutInfo")
	public String ajoutInfo(Model model) throws Exception {
		model.addAttribute("categ", Categorie.findAllCategorie());
		model.addAttribute("info", new Information());
		model.addAttribute("type", "ajout");
		return "/admin/formInfo";
	}
	@PostMapping(value="admin/ajoutInfo")
	public String ajoutInfo(@ModelAttribute Information info,Model model,@RequestParam("photo") MultipartFile multipartFile) throws Exception{
		String view=null;
		try {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			String path=Information.imagePath(fileName);
			info.setImage(path+"/"+fileName);
			info.insertInformation();
			String pathe="src/main/resources/static"+path;
			Information.saveFile(pathe, fileName, multipartFile);
			view="redirect:listeInformation";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("type", "ajout");
			model.addAttribute("categ", Categorie.findAllCategorie());
			model.addAttribute("info", new Information());
			view="/admin/formInfo";
		}
		return view;
	}
	@GetMapping(value="admin/updateInfo/{id}")
	public String updateInfo(@PathVariable String id,Model model) throws Exception {
		Information info=Information.getInformatiopnById(id);
		model.addAttribute("info",info);
		model.addAttribute("categ", Categorie.findAllCategorie());
		model.addAttribute("type", "update");
		return "/admin/formInfo";
	}
	@PostMapping(value="admin/updateInfo/{id}")
	public String updateInfo(@PathVariable String id,@ModelAttribute Information info,Model model) throws Exception{
		String view=null;
		try {
			info.setIdInformation(Integer.parseInt(id));
			info.setImage(Information.getInformatiopnById(id).getImage());
			info.upDateInformation();
			view="redirect:../listeInformation";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("categ", Categorie.findAllCategorie());
			model.addAttribute("type", "update");
			model.addAttribute("info", info);
			view="/admin/formInfo";
		}
		return view;
	}
	@GetMapping(value="admin/ajoutStat")
	public String ajoutStat(Model model) throws Exception {
		model.addAttribute("categ", Categorie.findAllCategorie());
		model.addAttribute("stat", new Statistique());
		return "/admin/formStat";
	}
	@PostMapping(value="admin/ajoutStat")
	public String ajoutStat(@ModelAttribute Statistique stat,Model model) throws Exception{
		String view=null;
		try {
			stat.insertStatistique();
			view="redirect:stat";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			model.addAttribute("categ", Categorie.findAllCategorie());
			model.addAttribute("stat", new Statistique());
			view="/admin/formStat";
		}
		return view;
	}
	@GetMapping(value="admin/stat")
	public String stat(Model model) throws Exception {
		model.addAttribute("state",Statistique.getLastStat());
		model.addAttribute("categ", Categorie.findAllCategorie());
		model.addAttribute("data", Statistique.getDataView());
		model.addAttribute("total", Statistique.getTotal());
		model.addAttribute("stat", new Statistique());
		return "/admin/stat";
	}
}
