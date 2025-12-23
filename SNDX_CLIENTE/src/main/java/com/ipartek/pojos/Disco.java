package com.ipartek.pojos;

public class Disco {
////ATRIBUTOS
	private int id;
	private int puntuacion;	
	private String fecha;
	private String nombre;
	private String foto;
	private String enlaceWikipedia;
	private String enlaceSpotify;
	private String enlaceTidal;
    private Productor productor;
    private Discografica discografica;
    private Genero genero;
    private Artista artista;

    
////CONSTRUCTORES
    public Disco(int id, int puntuacion, String fecha, String nombre, String foto, String enlaceWikipedia,
    		String enlaceSpotify, String enlaceTidal, Productor productor, Discografica discografica, Genero genero,
    		Artista artista) {
    	super();
    	this.id = id;
    	this.puntuacion = puntuacion;
    	this.fecha = fecha;
    	this.nombre = nombre;
    	this.foto = foto;
    	this.enlaceWikipedia = enlaceWikipedia;
    	this.enlaceSpotify = enlaceSpotify;
    	this.enlaceTidal = enlaceTidal;
    	this.productor = productor;
    	this.discografica = discografica;
    	this.genero = genero;
    	this.artista = artista;  	
    }
    
	public Disco() {
    	super();
    	this.id = 0;
    	this.puntuacion = 0;
    	this.fecha = "";
    	this.nombre = "";
    	this.foto = "";
    	this.enlaceWikipedia = "";
    	this.enlaceSpotify = "";
    	this.enlaceTidal = "";
    	this.productor = new Productor();
    	this.discografica = new Discografica();
    	this.genero = new Genero();
    	this.artista = new Artista();	
    }


////GETTERS & SETTERS
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPuntuacion() {
		return puntuacion;
	}

	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
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

	public Productor getProductor() {
		return productor;
	}

	public void setProductor(Productor productor) {
		this.productor = productor;
	}

	public Discografica getDiscografica() {
		return discografica;
	}

	public void setDiscografica(Discografica discografica) {
		this.discografica = discografica;
	}

	public Artista getArtista() {
		return artista;
	}

	public void setArtista(Artista artista) {
		this.artista = artista;
	}
	
	public Genero getGenero() {
		return genero;
	}

	public void setGenero(Genero genero) {
		this.genero = genero;
	}

	public String getEnlaceSpotify() {
		return enlaceSpotify;
	}

	public void setEnlaceSpotify(String enlaceSpotify) {
		this.enlaceSpotify = enlaceSpotify;
	}

	public String getEnlaceTidal() {
		return enlaceTidal;
	}

	public void setEnlaceTidal(String enlaceTidal) {
		this.enlaceTidal = enlaceTidal;
	}
	
	
////TO STRING
	@Override
	public String toString() {
		return "Disco [id=" + id + ", puntuacion=" + puntuacion + ", fecha=" + fecha + ", nombre=" + nombre + ", foto="
				+ foto + ", enlaceWikipedia=" + enlaceWikipedia + ", enlaceSpotify=" + enlaceSpotify + ", enlaceTidal="
				+ enlaceTidal + ", productor=" + productor + ", discografica=" + discografica + ", genero=" + genero
				+ ", artista=" + artista + "]";
	}

}