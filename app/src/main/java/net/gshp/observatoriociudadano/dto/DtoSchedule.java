package net.gshp.observatoriociudadano.dto;

/**
 * Created by Antoniunix on 4/10/17.
 */

public class DtoSchedule {

    private long id_agenda;
    private String start_datetime;
    private String end_datetime;
    private String start;
    private String end;
    private long id_pdv;
    private int type;
    private String name;
    private String address;
    private String pdv_code;
    private double lat;
    private double lon;
    private long id_client;
    private String client;
    private boolean isCheckIn;
    private boolean isCheckOut;
    private long idReport;
    private int typePdv;
    private int numTask;
    private int qualification;
    private long idUser;

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public int getQualification() {
        return qualification;
    }

    public void setQualification(int qualification) {
        this.qualification = qualification;
    }

    public int getNumTask() {
        return numTask;
    }

    public void setNumTask(int numTask) {
        this.numTask = numTask;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public long getId_agenda() {
        return id_agenda;
    }

    public void setId_agenda(long id_agenda) {
        this.id_agenda = id_agenda;
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

    public long getId_pdv() {
        return id_pdv;
    }

    public void setId_pdv(long id_pdv) {
        this.id_pdv = id_pdv;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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

    public String getPdv_code() {
        return pdv_code;
    }

    public void setPdv_code(String pdv_code) {
        this.pdv_code = pdv_code;
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

    public long getId_client() {
        return id_client;
    }

    public void setId_client(long id_client) {
        this.id_client = id_client;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public boolean isCheckIn() {
        return isCheckIn;
    }

    public void setCheckIn(boolean checkIn) {
        isCheckIn = checkIn;
    }

    public boolean isCheckOut() {
        return isCheckOut;
    }

    public void setCheckOut(boolean checkOut) {
        isCheckOut = checkOut;
    }

    public long getIdReport() {
        return idReport;
    }

    public void setIdReport(long idReport) {
        this.idReport = idReport;
    }

    public int getTypePdv() {
        return typePdv;
    }

    public void setTypePdv(int typePdv) {
        this.typePdv = typePdv;
    }

    @Override
    public String toString() {
        return "DtoSchedule{" +
                "id_agenda=" + id_agenda +
                ", start_datetime='" + start_datetime + '\'' +
                ", end_datetime='" + end_datetime + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                ", id_pdv=" + id_pdv +
                ", type=" + type +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", pdv_code='" + pdv_code + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", id_client=" + id_client +
                ", client='" + client + '\'' +
                ", isCheckIn=" + isCheckIn +
                ", isCheckOut=" + isCheckOut +
                ", idReport=" + idReport +
                ", typePdv=" + typePdv +
                ", numTask=" + numTask +
                ", qualification=" + qualification +
                ", idUser=" + idUser +
                '}';
    }
}
