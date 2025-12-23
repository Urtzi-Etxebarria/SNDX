package com.ipartek.modelo;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "generos")
public class Genero {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_genero")
	private int id;
	private String nombre;
	
	@OneToMany(mappedBy = "genero") 
    @JsonIgnoreProperties("genero") 
    private List<Disco> discos;
	
	
	public Genero(int id, String nombre, List<Disco> discos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.discos = discos;
	}
	
	public Genero() {
		super();
		this.id = 0;
		this.nombre = "";
		this.discos = new ArrayList<Disco>();
	}

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

	public List<Disco> getDiscos() {
		return discos;
	}

	public void setDiscos(List<Disco> discos) {
		this.discos = discos;
	}

	@Override
	public String toString() {
		return "Genero [id=" + id + ", nombre=" + nombre + "]";
	}

}
