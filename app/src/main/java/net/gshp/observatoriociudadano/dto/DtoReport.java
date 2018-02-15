package net.gshp.observatoriociudadano.dto;

/**
 * Created by leo on 22/01/18.
 */

public class DtoReport {
    private long id;
    private long idPdv;
    private long idSchedule;
    private String version;
    private long date;
    private String tz;
    private String imei;
    private String hash;
    private int send;
    private int typeReport;
    private long idReportServer;
    private String dateInactive;
    private int active;

    public long getId() {
        return id;
    }

    public DtoReport setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdPdv() {
        return idPdv;
    }

    public DtoReport setIdPdv(long idPdv) {
        this.idPdv = idPdv;
        return this;
    }

    public long getIdSchedule() {
        return idSchedule;
    }

    public DtoReport setIdSchedule(long idSchedule) {
        this.idSchedule = idSchedule;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public DtoReport setVersion(String version) {
        this.version = version;
        return this;
    }

    public long getDate() {
        return date;
    }

    public DtoReport setDate(long date) {
        this.date = date;
        return this;
    }

    public String getTz() {
        return tz;
    }

    public DtoReport setTz(String tz) {
        this.tz = tz;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public DtoReport setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public DtoReport setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public int getSend() {
        return send;
    }

    public DtoReport setSend(int send) {
        this.send = send;
        return this;
    }

    public int getTypeReport() {
        return typeReport;
    }

    public DtoReport setTypeReport(int typeReport) {
        this.typeReport = typeReport;
        return this;
    }

    public long getIdReportServer() {
        return idReportServer;
    }

    public DtoReport setIdReportServer(long idReportServer) {
        this.idReportServer = idReportServer;
        return this;
    }

    public String getDateInactive() {
        return dateInactive;
    }

    public DtoReport setDateInactive(String dateInactive) {
        this.dateInactive = dateInactive;
        return this;
    }

    public int getActive() {
        return active;
    }

    public DtoReport setActive(int active) {
        this.active = active;
        return this;
    }
}
