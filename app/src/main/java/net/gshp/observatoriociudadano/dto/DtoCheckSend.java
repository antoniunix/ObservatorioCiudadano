package net.gshp.observatoriociudadano.dto;

/**
 * Created by leo on 22/01/18.
 */

public class DtoCheckSend {
    private long idAgenda;
    private long idPdv;
    private String version;
    private String hash;
    private long checkInDate;
    private String checkInTz;
    private double checkInLat;
    private double checkInLon;
    private String checkInIMEI;
    private String checkInAccuracy;
    private long checkInSatelliteUTC;
    private String idUser = "@user";
    private String idPerson = "@person";
    private long idReportLocal;

    public long getIdAgenda() {
        return idAgenda;
    }

    public DtoCheckSend setIdAgenda(long idAgenda) {
        this.idAgenda = idAgenda;
        return this;
    }

    public long getIdPdv() {
        return idPdv;
    }

    public DtoCheckSend setIdPdv(long idPdv) {
        this.idPdv = idPdv;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public DtoCheckSend setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public DtoCheckSend setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public long getCheckInDate() {
        return checkInDate;
    }

    public DtoCheckSend setCheckInDate(long checkInDate) {
        this.checkInDate = checkInDate;
        return this;
    }

    public String getCheckInTz() {
        return checkInTz;
    }

    public DtoCheckSend setCheckInTz(String checkInTz) {
        this.checkInTz = checkInTz;
        return this;
    }

    public double getCheckInLat() {
        return checkInLat;
    }

    public DtoCheckSend setCheckInLat(double checkInLat) {
        this.checkInLat = checkInLat;
        return this;
    }

    public double getCheckInLon() {
        return checkInLon;
    }

    public DtoCheckSend setCheckInLon(double checkInLon) {
        this.checkInLon = checkInLon;
        return this;
    }

    public String getCheckInIMEI() {
        return checkInIMEI;
    }

    public DtoCheckSend setCheckInIMEI(String checkInIMEI) {
        this.checkInIMEI = checkInIMEI;
        return this;
    }

    public String getCheckInAccuracy() {
        return checkInAccuracy;
    }

    public DtoCheckSend setCheckInAccuracy(String checkInAccuracy) {
        this.checkInAccuracy = checkInAccuracy;
        return this;
    }

    public long getCheckInSatelliteUTC() {
        return checkInSatelliteUTC;
    }

    public DtoCheckSend setCheckInSatelliteUTC(long checkInSatelliteUTC) {
        this.checkInSatelliteUTC = checkInSatelliteUTC;
        return this;
    }

    public String getIdUser() {
        return idUser;
    }

    public DtoCheckSend setIdUser(String idUser) {
        this.idUser = idUser;
        return this;
    }

    public String getIdPerson() {
        return idPerson;
    }

    public DtoCheckSend setIdPerson(String idPerson) {
        this.idPerson = idPerson;
        return this;
    }

    public long getIdReportLocal() {
        return idReportLocal;
    }

    public DtoCheckSend setIdReportLocal(long idReportLocal) {
        this.idReportLocal = idReportLocal;
        return this;
    }

    @Override
    public String toString() {
        return "DtoCheckSend{" +
                "idAgenda=" + idAgenda +
                ", idPdv=" + idPdv +
                ", version='" + version + '\'' +
                ", hash='" + hash + '\'' +
                ", checkInDate=" + checkInDate +
                ", checkInTz='" + checkInTz + '\'' +
                ", checkInLat=" + checkInLat +
                ", checkInLon=" + checkInLon +
                ", checkInIMEI='" + checkInIMEI + '\'' +
                ", checkInAccuracy='" + checkInAccuracy + '\'' +
                ", checkInSatelliteUTC=" + checkInSatelliteUTC +
                ", idUser='" + idUser + '\'' +
                ", idPerson='" + idPerson + '\'' +
                ", idReportLocal=" + idReportLocal +
                '}';
    }
}
