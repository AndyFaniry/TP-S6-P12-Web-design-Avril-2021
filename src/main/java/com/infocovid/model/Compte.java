package com.infocovid.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.infocovid.bdd.ConnectionPstg;


public class Compte {
	int idCompte;
	String nom;
	String mdp;
	
	public int getIdCompte() {
		return idCompte;
	}
	public void setIdCompte(int idCompte) {
		this.idCompte = idCompte;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getMdp() {
		return mdp;
	}
	public void setMdp(String mdp) {
		this.mdp = mdp;
	}
	public Compte() {}
	public Compte(int idCompte, String nom, String mdp) {
		setIdCompte(idCompte);
		setNom(nom);
		setMdp(mdp);
	}
	public Compte(String nom, String mdp) {
		setNom(nom);
		setMdp(mdp);
	}
	public static ArrayList<Compte> findAllCompte(String sql,Connection co) throws Exception{
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Compte> array = new ArrayList<Compte>();
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery(); 
			while(result.next()) {
				int id=result.getInt("idcompte");
				String nom=result.getString("nom");
				String mdp=result.getString("mdp");
				Compte c=new Compte(id,nom,mdp);
				array.add(c);
			}
		}catch(Exception e) {
			e.getMessage();
		}finally {
			if(st!=null) st.close();
		}
		return array;
    }
	public static Compte valideLogin(String nom, String mdp, Connection co) throws Exception {
		String sql= "select * from Compte where nom='"+nom+"' and mdp=md5('123@"+mdp+"')";
		ArrayList<Compte> comptes= Compte.findAllCompte(sql, co);
		if(comptes.size()!=1) throw new Exception("mot de passe ou nom non valide");
		return comptes.get(0);
	}
	public static Compte findCompteById(int id,Connection co) throws Exception {
		String sql= "select * from Compte where idcompte="+id;
		ArrayList<Compte> comptes= Compte.findAllCompte(sql, co);
		if(comptes.size()!=1) throw new Exception("compte invalide");
		return comptes.get(0);
	}
	public void insert(Connection co)throws Exception{
		PreparedStatement st = null;
		try {
				String sql= "insert into compte(nom,mdp) VALUES (?,md5(?))";
				st = co.prepareStatement(sql);
				st.setString(1,this.getNom());
				st.setString(2,"@123"+this.getMdp());
				st.execute();
		} catch (Exception e) {
			throw e;
		} finally {
			if(st != null) st.close();
		}
	}
	public String login() throws Exception {
		Connection co= new ConnectionPstg().getConnection();
		String message=null;
		try {
			Compte compteValide= Compte.valideLogin(this.getNom(),this.getMdp(), co);
			message= Token.insertToken(compteValide,co);
		}
		catch(Exception ex) {
			throw ex;
		}
		finally {
			if(co != null) co.close();
		}
		return message;
	}
	public static void deconnect(String token) throws Exception {
		Connection co= new ConnectionPstg().getConnection();
		try {
			Token.updateToken(token, co);	
		}
		catch(Exception ex) {
			throw ex;
		}
		finally {
			if(co != null) co.close();
		}
	}
}
