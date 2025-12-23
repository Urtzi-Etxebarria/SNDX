package com.ipartek.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="usuarios")
public class Usuario {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	 
	@Column(unique = true, nullable = false)
	private String name;
	
	@Column( nullable = false)
	private String pass;
	
	@Column( nullable = false)
	private String role;
	
	public Usuario(int id, String name, String pass, String role) {
		super();
		this.id = id;
		this.name = name;
		this.pass = pass;
		this.role = role;
	}
	
	public Usuario() {
		super();
		this.id = 0;
		this.name = "";
		this.pass = "";
		this.role = "";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}


	@Override
	public String toString() {
		return "Usuario [id=" + id + ", name=" + name + ", pass=" + pass + ", role=" + role + "]";
	}

}
