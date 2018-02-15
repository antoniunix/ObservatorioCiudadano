package net.gshp.observatoriociudadano.dto;


public class DtoEARespuesta {

	private Integer id;
	private Integer idPregunta;
	private Integer idReport;
	private Integer idReporteLocal;
	private Integer idEncuesta;
	private String nombreEncuesta;
	private String respuesta;
	private String hash;
	private Integer enviado;
	private Integer numeroEncuesta;
	private String campoExtra1;
	private String campoExtra2;
	private Integer pdv;
	private String description;


	public Integer getPdv() {
		return pdv;
	}

	public void setPdv(Integer pdv) {
		this.pdv = pdv;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIdReport() {
		return idReport;
	}

	public void setIdReport(Integer idReport) {
		this.idReport = idReport;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdPregunta() {
		return idPregunta;
	}

	public void setIdPregunta(Integer idPregunta) {
		this.idPregunta = idPregunta;
	}

	public Integer getIdReporteLocal() {
		return idReporteLocal;
	}

	public void setIdReporteLocal(Integer idReporteLocal) {
		this.idReporteLocal = idReporteLocal;
	}

	public Integer getIdEncuesta() {
		return idEncuesta;
	}

	public void setIdEncuesta(Integer idEncuesta) {
		this.idEncuesta = idEncuesta;
	}

	public String getNombreEncuesta() {
		return nombreEncuesta;
	}

	public void setNombreEncuesta(String nombreEncuesta) {
		this.nombreEncuesta = nombreEncuesta;
	}

	public String getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(String respuesta) {
		this.respuesta = respuesta;
	}

	public String getHash() {
		return hash;
	}

	public void setHash(String hash) {
		this.hash = hash;
	}

	public Integer getEnviado() {
		return enviado;
	}

	public void setEnviado(Integer enviado) {
		this.enviado = enviado;
	}

	public Integer getNumeroEncuesta() {
		return numeroEncuesta;
	}

	public void setNumeroEncuesta(Integer numeroEncuesta) {
		this.numeroEncuesta = numeroEncuesta;
	}

	public String getCampoExtra1() {
		return campoExtra1;
	}

	public void setCampoExtra1(String campoExtra1) {
		this.campoExtra1 = campoExtra1;
	}

	public String getCampoExtra2() {
		return campoExtra2;
	}

	public void setCampoExtra2(String campoExtra2) {
		this.campoExtra2 = campoExtra2;
	}
}
