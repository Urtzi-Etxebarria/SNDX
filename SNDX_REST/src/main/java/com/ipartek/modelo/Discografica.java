package com.ipartek.modelo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "discograficas")
public class Discografica {
////ATRIBUTOS
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_discografica")
    private int id;

    private String nombre;
    private String logo;
    private String enlaceWikipedia;

    @OneToMany(mappedBy = "discografica", cascade = CascadeType.ALL)
    @JsonBackReference("discografica-discos")
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
		return "Discografica [id=" + id + ", nombre=" + nombre + ", logo=" + logo + ", enlaceWikipedia="
				+ enlaceWikipedia + ", discografia=" + discografia + "]";
	}
	
}