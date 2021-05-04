package com.infocovid.model;

import java.util.ArrayList;

public class Data {
	int[] Y;
	int[] X;
	int[] id;
	public int[] getY() {
		return Y;
	}
	public void setY(int[] y) {
		Y = y;
	}
	public int[] getX() {
		return X;
	}
	public void setX(int[] x) {
		X = x;
	}
	
	public int[] getId() {
		return id;
	}
	public void setId(int[] id) {
		this.id = id;
	}
	public Data(int[] i,int[] y, int[] x) {
		super();
		id = i;
		Y = y;
		X = x;
	}
	public ArrayList<Data> toData(ArrayList<Statistique> stat){
		ArrayList<Data> data=new ArrayList<Data>();
		return data;
	}
}
