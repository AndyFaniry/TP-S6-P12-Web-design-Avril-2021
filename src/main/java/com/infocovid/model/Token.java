package com.infocovid.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;

import com.infocovid.fonction.Fonction;

public class Token {
	int idToken;
    int idCompte;
    String valeur;
    LocalDateTime timestamp;
    int statu;
	public int getIdToken() {
		return idToken;
	}
	public void setIdToken(int idToken) {
		this.idToken = idToken;
	}
	public int getIdCompte() {
		return idCompte;
	}
	public void setIdCompte(int idCompte) {
		this.idCompte = idCompte;
	}
	public String getValeur() {
		return valeur;
	}
	public void setValeur(String valeur) {
		this.valeur = valeur;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	public int getStatu() {
		return statu;
	}
	public void setStatu(int statu) throws Exception {
		if(statu==1 || statu==0) this.statu = statu;
		else throw new Exception("status non valide");
	}
	public Token(int idToken, int idCompte, String valeur, LocalDateTime timestamp, int statu) {
		super();
		this.idToken = idToken;
		this.idCompte = idCompte;
		this.valeur = valeur;
		this.timestamp = timestamp;
		this.statu = statu;
	}
	public static String deleteBearerToToken(String bearerToken) {
		String[] token= bearerToken.split(" ");
		return token[1];
	}
	public static String getToken(Compte compte,Connection co) throws Exception {
		String daty= Fonction.getDateNow(co);
		String mdp= compte.getMdp();
		String val= daty+"@1308"+mdp;
		String token= Fonction.addSha1(val, co);
		return token;
	}
	public static String insertToken(Compte compte,Connection co) throws Exception {
		String token= getToken(compte,co);
		PreparedStatement st = null;
		try {
			String sql= "insert into token(idCompte,valeur,daty,statu) VALUES (?,?,CURRENT_TIMESTAMP,1)";
			st = co.prepareStatement(sql);
			st.setInt(1,compte.getIdCompte());
			st.setString(2,token);
			st.execute();
			co.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(st != null) st.close();
		}
		return token;
	}
	public static int verificationToken(String token,Connection co) throws Exception {
		String sql= "select * from token where valeur=? and statu=1";
		PreparedStatement st = null;
		ResultSet resultSet = null;
		int idCompte=0;
		try {
			st = co.prepareStatement(sql);
			st.setString(1,token);
			resultSet = st.executeQuery();
			while (resultSet.next()) {
				idCompte =resultSet.getInt("idcompte");	
			}
		}catch(Exception e) {
			e.getMessage();
		}finally {
			if(st!=null) st.close();
		}
		return idCompte;
	}
	public static void updateToken(String token,Connection co) throws Exception{
		PreparedStatement st = null;
		try {
			String sql= "update token set statu=0 where valeur= ?";
			st = co.prepareStatement(sql);
			st.setString(1, token);
			st.execute();
			co.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(st != null) st.close();
		}
	}
}
