package com.bolao.entity.user;

public enum PerfilTipo {

	ADMIN(1, "ADMIN"), GESTOR(2, "GESTOR"), CONVIDADO(3, "CONVIDADO"), USUARIO(4, "USUARIO");
	
	private long cod;
	private String desc;

	private PerfilTipo(long cod, String desc) {
		this.cod = cod;
		this.desc = desc;
	}

	public long getCod() {
		return cod;
	}

	public String getDesc() {
		return desc;
	}
}
