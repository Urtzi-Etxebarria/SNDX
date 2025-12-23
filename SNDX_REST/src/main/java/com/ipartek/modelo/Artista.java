package com.ipartek.modelo;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "artistas")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_artista")
    private int id;
    private int notaMedia;

    private String nombre;
    private String foto;
    private String enlaceWikipedia;
    private String enlaceSpotify;
    private String enlaceTidal;
    
    @OneToMany(mappedBy = "artista")
    @JsonIgnore // ✅ No serializamos los discos del artista para evitar el bucle
    private List<Disco> discografia;

    
////CONSTRUCTORES
	public Artista(int id, int notaMedia, String nombre, String foto, String enlaceWikipedia, String enlaceSpotify,
			String enlaceTidal, List<Disco> discografia) {
		super();
		this.id = id;
		this.notaMedia = notaMedia;
		this.nombre = nombre;
		this.foto = foto;
		this.enlaceWikipedia = enlaceWikipedia;
		this.enlaceSpotify = enlaceSpotify;
		this.enlaceTidal = enlaceTidal;
		this.discografia = discografia;	
	}
	
	public Artista() {
		super();
		this.id = 0;
		this.notaMedia = 0;
		this.nombre = "";
		this.foto = "";
		this.enlaceWikipedia = "";
		this.enlaceSpotify = "";
		this.enlaceTidal = "";
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

	public List<Disco> getDiscografia() {
		return discografia;
	}

	public void setDiscografia(List<Disco> discografia) {
		this.discografia = discografia;
	}

	
////TO STRING
	@Override
	public String toString() {
		return "Artista [id=" + id + ", notaMedia=" + notaMedia + ", nombre=" + nombre + ", foto=" + foto
				+ ", enlaceWikipedia=" + enlaceWikipedia + ", enlaceSpotify=" + enlaceSpotify + ", enlaceTidal="
				+ enlaceTidal + "]";
	}

}
