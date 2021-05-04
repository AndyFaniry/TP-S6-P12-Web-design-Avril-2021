package com.infocovid.model;

import java.text.Normalizer;
import java.time.LocalDateTime;

public class Main {

	public static void main(String[] args) {
		Information info=new Information();
		info.setIdCategorie(1);
		info.setImage("andrana.jpg");
		info.setTitre("LES CHIFFRES DE L'ÉPIDÉMIE");
		info.setMotsCle("chiffres,épidémie,cas,taux");
		info.setInformation("Les indicateurs repartent légèrement à la hausse ce dimanche avec 28.818 personnes hospitalisées (+215) pour une infection au Covid-19, dont 5585 (+4) en soins critiques. 113 décès supplémentaires sont à déplorer.Ces dernières 24 heures, 9888 cas ont en outre été enregistrés (contre 24.465 dimanche dernier), un chiffre bas qui s'explique notamment par le 1er mai férié.À noter que seuls trois départements restent au-dessus du taux d'incidence de 400 cas pour 100.000 habitants : la Seine-Saint-Denis (440), le Val-de-Marne (431) et le Val-d'Oise (418).");
		try {
			//info.insertInformation();
			String url=Information.toUrl("LES CHIFFRES DE L'ÉPIDÉMIE");
			System.out.println(Information.findInformationByUrl("andrana-mandeha").getTitre());
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
