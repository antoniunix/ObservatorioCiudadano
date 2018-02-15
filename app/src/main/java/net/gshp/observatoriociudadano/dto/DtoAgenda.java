package net.gshp.observatoriociudadano.dto;

public class DtoAgenda {

	private long id_agenda;
	private long id_user;
	private long id_pdv;
	private String start_datetime;
	private String end_datetime;
	private int idRol;

	public int getIdRol() {
		return idRol;
	}

	public void setIdRol(int idRol) {
		this.idRol = idRol;
	}

	public long getId_agenda() {
		return id_agenda;
	}

	public void setId_agenda(long id_agenda) {
		this.id_agenda = id_agenda;
	}

	public long getId_user() {
		return id_user;
	}

	public void setId_user(long id_user) {
		this.id_user = id_user;
	}

	public long getId_pdv() {
		return id_pdv;
	}

	public void setId_pdv(long id_pdv) {
		this.id_pdv = id_pdv;
	}

	public String getStart_datetime() {
		return start_datetime;
	}

	public void setStart_datetime(String start_datetime) {
		this.start_datetime = start_datetime;
	}

	public String getEnd_datetime() {
		return end_datetime;
	}

	public void setEnd_datetime(String end_datetime) {
		this.end_datetime = end_datetime;
	}
}
