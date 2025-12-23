package com.ipartek.pojos;

import java.util.ArrayList;
import java.util.List;


public class Discografica {
////ATRIBUTOS
    private int id;
    private String nombre;   
    private String enlaceWikipedia;
    private String logo;
    private List<Disco> discografia;

    
////CONSTRUCTORES
	public Discografica(int id, String nombre, String logo, String enlaceWikipedia, List<Disco> discografia, List<String> enlaces) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.logo = logo;
		this.discografia = discografia;
	}
	
	public Discografica() {
		super();
		this.id = 0;
		this.nombre = "";
		this.enlaceWikipedia = "";
		this.discografia = new ArrayList<Disco>();
	}

	
////GETTERS & SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	
	public String getEnlaceWikipedia() {
		return enlaceWikipedia;
	}

	public void setEnlaceWikipedia(String enlaceWikipedia) {
		this.enlaceWikipedia = enlaceWikipedia;
	}

	public List<Disco> getDiscografia() {
		return discografia;
	}

	public void setDiscografia(List<Disco> discografia) {
		this.discografia = discografia;
	}
	
	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	

////TO STRING
	@Override
	public String toString() {
		return "Discografica [id=" + id + ", nombre=" + nombre + ", enlaceWikipedia=" + enlaceWikipedia + ", logo="
				+ logo + "]";
	}
	
}