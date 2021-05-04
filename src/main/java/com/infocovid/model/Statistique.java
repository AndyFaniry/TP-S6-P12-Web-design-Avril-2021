package com.infocovid.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.format.annotation.DateTimeFormat;

import com.infocovid.bdd.ConnectionPstg;

public class Statistique {
	int idStatistique;
    int idCategorie;
    int positif; 
    int guerie;
    int deces;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDate dates;
    
    public Statistique(){
    }

	public int getIdStatistique() {
		return idStatistique;
	}

	public void setIdStatistique(int idStatistique) {
		this.idStatistique = idStatistique;
	}

	public int getIdCategorie() {
		return idCategorie;
	}

	public void setIdCategorie(int idCategorie) {
		this.idCategorie = idCategorie;
	}

	public int getPositif() {
		return positif;
	}

	public void setPositif(int positif) throws Exception {
		if(positif<0)throw new Exception("nombre cas négatif");
		else this.positif = positif;
	}

	public int getGuerie() {
		return guerie;
	}

	public void setGuerie(int guerie) throws Exception {
		if(guerie<0)throw new Exception("nombre de guérie négatif");
		else this.guerie = guerie;
	}

	public int getDeces() {
		return deces;
	}

	public void setDeces(int deces) throws Exception {
		if(deces<0)throw new Exception("nombre de décés négatif");
		else this.deces = deces;
	}

	public LocalDate getDates() {
		return dates;
	}

	public void setDates(LocalDate dates) throws Exception {
		if(dates.isBefore(LocalDate.now()))throw new Exception("Date de publication invalide");
		else this.dates = dates;
	}

	public Statistique(int idStatistique, int idCategorie, int positif, int guerie, int deces, LocalDate dates) {
		super();
		this.idStatistique = idStatistique;
		this.idCategorie = idCategorie;
		this.positif = positif;
		this.guerie = guerie;
		this.deces = deces;
		this.dates = dates;
	}
	public Statistique(int idCategorie, int positif, int guerie, int deces, LocalDate dates) {
		super();
		this.idCategorie = idCategorie;
		this.positif = positif;
		this.guerie = guerie;
		this.deces = deces;
		this.dates = dates;
	}
	public Statistique(int idCategorie, int positif, int guerie, int deces) {
		super();
		this.idCategorie = idCategorie;
		this.positif = positif;
		this.guerie = guerie;
		this.deces = deces;
	}
	public static ArrayList<Statistique> findAll(String sql) throws Exception{
		Connection co=new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Statistique> array = new ArrayList<Statistique>();
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery(); 
			while(result.next()) {
				int idStat=result.getInt("idStatistique");
				int idCa=result.getInt("idCategorie");
				int pos=result.getInt("positif");
				int gue=result.getInt("guerie");
				int dec=result.getInt("deces");
				LocalDate date=result.getTimestamp("dates").toLocalDateTime().toLocalDate();
				Statistique stat=new Statistique(idStat,idCa,pos,gue,dec,date);
				array.add(stat);
			}
		}catch(Exception e) {
			e.getMessage();
			throw e;
		}finally {
			if(st!=null) st.close();
		}
		return array;
    }
	public static ArrayList<Statistique> findView(String sql) throws Exception{
		Connection co=new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Statistique> array = new ArrayList<Statistique>();
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery(); 
			while(result.next()) {
				int idCa=result.getInt("idCategorie");
				int pos=result.getInt("positif");
				int gue=result.getInt("guerie");
				int dec=result.getInt("deces");
				LocalDate date=result.getTimestamp("dates").toLocalDateTime().toLocalDate();
				Statistique stat=new Statistique(idCa,pos,gue,dec,date);
				array.add(stat);
			}
		}catch(Exception e) {
			e.getMessage();
			throw e;
		}finally {
			if(st!=null) st.close();
		}
		return array;
    }
	public static Statistique getStatistiqueById(String id) throws Exception {
		String sql="select * from statistique where idStatistique="+id;
		ArrayList<Statistique> info=Statistique.findAll(sql);
		return info.get(0);
	}
	public void insertStatistique() throws Exception {
		Connection co= new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		try {
			String sql= "insert into statistique(idCategorie,positif,guerie,deces,dates) VALUES (?,?,?,?,?)";
			st = co.prepareStatement(sql);
			st.setInt(1, idCategorie);
			st.setInt(2,positif);
			st.setInt(3, guerie);
			st.setInt(4, deces);
			st.setTimestamp(5, Timestamp.valueOf(dates.atStartOfDay()));			
			st.execute();
			co.commit();		
			
		} catch (Exception e) {
			throw e;
		} finally {
			if(st != null) st.close();
			if(co!=null) co.close();
		}
	}
	public static ArrayList<Statistique> getLastStat() throws Exception{
		String sql="select * from lastStat";
		return Statistique.findView(sql);
	}
	public static ArrayList<Statistique> getDataView() throws Exception{
		String sql="select * from dataChart";
		return Statistique.findView(sql);
	}
	public static ArrayList<Statistique> getTotal() throws Exception{
		String sql="select * from totalNb";
		Connection co=new ConnectionPstg().getConnection();
		PreparedStatement st = null;
		ResultSet result = null;
		ArrayList<Statistique> array = new ArrayList<Statistique>();
		try {
			st = co.prepareStatement(sql);
			result = st.executeQuery(); 
			while(result.next()) {
				int idCa=result.getInt("idCategorie");
				int pos=result.getInt("positif");
				int gue=result.getInt("guerie");
				int dec=result.getInt("deces");
				Statistique stat=new Statistique(idCa,pos,gue,dec);
				array.add(stat);
			}
		}catch(Exception e) {
			e.getMessage();
			throw e;
		}finally {
			if(st!=null) st.close();
		}
		return array;
	}
}
