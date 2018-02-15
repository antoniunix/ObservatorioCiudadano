package net.gshp.observatoriociudadano.dto;

public class DtoCheckIsPoll {
	
	private Integer id;
	private String nombre;
	private Integer estado;
	private Integer preguntas;
	private Integer respuestas;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getPreguntas() {
		return preguntas;
	}
	public void setPreguntas(Integer preguntas) {
		this.preguntas = preguntas;
	}
	public Integer getRespuestas() {
		return respuestas;
	}
	public void setRespuestas(Integer respuestas) {
		this.respuestas = respuestas;
	}
	@Override
	public String toString() {
		return "DtoCheckIsPoll [id=" + id + ", nombre=" + nombre + ", estado=" + estado + ", preguntas=" + preguntas
				+ ", respuestas=" + respuestas + "]";
	}
	
	
}
