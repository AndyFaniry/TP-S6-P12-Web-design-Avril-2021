package com.infocovid.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.infocovid.bdd.ConnectionPstg;

public class Categorie {
	int idCategorie; 
    String categorie;
    public Categorie() {
    }
	public int getIdCategorie() {
		return idCategorie;
	}
	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}
	public String getCategorie() {
		return categorie;
	}
	public void setCategorie(String categorie) {
		this.categorie = categorie;
	}
	public Categorie(int idCategorie, String categorie) {
		super();
		this.idCategorie = idCategorie;
		this.categorie = categorie;
	}
	public static ArrayList<Categorie> findAllCategorie() throws Exception{
		Connection co=new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Categorie> array = new ArrayList<Categorie>();
		try {
			st = co.prepareStatement("select * from categorie");
			result = st.executeQuery(); 
			while(result.next()) {
				int id=result.getInt("idcategorie");
				String nom=result.getString("categorie");
				Categorie c=new Categorie(id,nom);
				array.add(c);
			}
		}catch(Exception e) {
			e.getMessage();
		}finally {
			if(st!=null) st.close();
		}
		return array;
    }
}
