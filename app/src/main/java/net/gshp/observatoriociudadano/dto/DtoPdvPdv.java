package net.gshp.observatoriociudadano.dto;

import com.google.gson.annotations.SerializedName;


public class DtoPdvPdv {

	private long id;
	private long idClient;
	private long idRtm;
	private long idCanal;
	private String name;
	private String address;
	private String pdvCode;
	private double lat;
	private double lon;
	private long town;
	@SerializedName("id_format")
	private long idFormat;
	private String extraField1;
	private String extraField2;
	private String extraField3;
	private int type;
	private float distanceToPdv;
	private double myLocationLat, myLocationLon;
	@SerializedName("id_region")
	private long idRegion;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdClient() {
		return idClient;
	}

	public void setIdClient(long idClient) {
		this.idClient = idClient;
	}

	public long getIdRtm() {
		return idRtm;
	}

	public void setIdRtm(long idRtm) {
		this.idRtm = idRtm;
	}

	public long getIdCanal() {
		return idCanal;
	}

	public void setIdCanal(long idCanal) {
		this.idCanal = idCanal;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPdvCode() {
		return pdvCode;
	}

	public void setPdvCode(String pdvCode) {
		this.pdvCode = pdvCode;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public long getTown() {
		return town;
	}

	public void setTown(long town) {
		this.town = town;
	}


	public long getIdFormat() {
		return idFormat;
	}

	public void setIdFormat(long idFormat) {
		this.idFormat = idFormat;
	}

	public String getExtraField1() {
		return extraField1;
	}

	public void setExtraField1(String extraField1) {
		this.extraField1 = extraField1;
	}

	public String getExtraField2() {
		return extraField2;
	}

	public void setExtraField2(String extraField2) {
		this.extraField2 = extraField2;
	}

	public String getExtraField3() {
		return extraField3;
	}

	public void setExtraField3(String extraField3) {
		this.extraField3 = extraField3;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public float getDistanceToPdv() {
		return distanceToPdv;
	}

	public void setDistanceToPdv(float distanceToPdv) {
		this.distanceToPdv = distanceToPdv;
	}

	public double getMyLocationLat() {
		return myLocationLat;
	}

	public void setMyLocationLat(double myLocationLat) {
		this.myLocationLat = myLocationLat;
	}

	public double getMyLocationLon() {
		return myLocationLon;
	}

	public void setMyLocationLon(double myLocationLon) {
		this.myLocationLon = myLocationLon;
	}

	public long getIdRegion() {
		return idRegion;
	}

	public void setIdRegion(long idRegion) {
		this.idRegion = idRegion;
	}
}

