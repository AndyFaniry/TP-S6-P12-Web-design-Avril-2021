package com.infocovid.bdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionPstg {
	Connection co;
	Statement stmt;
	ResultSet rs;
	public Connection getConnection(){
		return this.co;
	}
	public void clear()throws Exception{
		co.close();
		stmt.close();
	}
	public Statement getStatement(){
		return this.stmt;
	}
	public ConnectionPstg(){
            
            try{
                Class.forName("org.postgresql.Driver");
                this.co = DriverManager.getConnection("jdbc:postgresql://ec2-54-228-9-90.eu-west-1.compute.amazonaws.com:5432/dfining5frkknt","dazzujlzifvrcs","39ef19c071af24047dfd5329ef13394f86bac6e9298142cc925eaea23040de7f");
                this.stmt = this.co.createStatement();
                this.co.setAutoCommit(false);
            }catch(Exception e){
                System.out.println(e.getMessage());
            }	
	}
	public void setAutoCommit(boolean value)throws Exception{
		this.co.setAutoCommit(value);
	}
	public void commit()throws Exception{
		this.co.commit();
	}
	public void rollback()throws Exception{
		this.co.rollback();
	}
}
