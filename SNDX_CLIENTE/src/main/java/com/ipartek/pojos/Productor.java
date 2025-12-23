package com.ipartek.pojos;

import java.util.ArrayList;
import java.util.List;

public class Productor {
////ATRIBUTOS
    private int id;
    private int notaMedia;
    private String nombre;
    private String foto;
    private String enlaceWikipedia;
    private List<Disco> discografia;


////CONSTRUCTORES
	public Productor(int id, int notaMedia, String nombre, String foto, String enlaceWikipedia,
			List<Disco> discografia) {
		super();
		this.id = id;
		this.notaMedia = notaMedia;
		this.nombre = nombre;
		this.foto = foto;
		this.enlaceWikipedia = enlaceWikipedia;
		this.discografia = discografia;
	}
	
	public Productor() {
		super();
		this.id = 0;
		this.notaMedia = 0;
		this.nombre = "";
		this.foto = "";
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

	public int getNotaMedia() {
		return notaMedia;
	}

	public void setNotaMedia(int notaMedia) {
		this.notaMedia = notaMedia;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
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

	
////TO STRING
	@Override
	public String toString() {
		return "Productor [id=" + id + ", notaMedia=" + notaMedia + ", nombre=" + nombre + ", foto=" + foto
				+ ", enlaceWikipedia=" + enlaceWikipedia + "]";
	}

}