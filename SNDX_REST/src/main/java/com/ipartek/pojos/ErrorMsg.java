package com.ipartek.pojos;

import java.time.LocalDateTime;

public class ErrorMsg {
	private int codigo;
	private String mensaje;
	private LocalDateTime fecha;

	public ErrorMsg(int codigo, String mensaje) {
		super();
		this.codigo = codigo;
		this.mensaje = mensaje;
		this.fecha = LocalDateTime.now();
	}

	public ErrorMsg() {
		super();
		this.codigo = 0;
		this.mensaje = "";
		this.fecha = LocalDateTime.now();
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public LocalDateTime getFecha() {
		return fecha;
	}

	public void setFecha(LocalDateTime fecha) {
		this.fecha = fecha;
	}

	@Override
	public String toString() {
		return "ErrorMsg [codigo=" + codigo + ", mensaje=" + mensaje + ", fecha=" + fecha + "]";
	}

}
