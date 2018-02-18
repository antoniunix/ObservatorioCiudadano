package net.gshp.observatoriociudadano.dto;

/**
 * Created by leo on 22/01/18.
 */

public class DtoReportVisit {
    private long id;
    private long idPdv;
    private int send;
    private String name;
    private String address;
    private String password;
    private long dateCheckIn;
    private long dateCheckOut;
    private String hash;

    public String getHash() {
        return hash;
    }

    public DtoReportVisit setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public long getId() {
        return id;
    }

    public DtoReportVisit setId(long id) {
        this.id = id;
        return this;
    }

    public long getIdPdv() {
        return idPdv;
    }

    public DtoReportVisit setIdPdv(long idPdv) {
        this.idPdv = idPdv;
        return this;
    }

    public int getSend() {
        return send;
    }

    public DtoReportVisit setSend(int send) {
        this.send = send;
        return this;
    }

    public String getName() {
        return name;
    }

    public DtoReportVisit setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public DtoReportVisit setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DtoReportVisit setPassword(String password) {
        this.password = password;
        return this;
    }

    public long getDateCheckIn() {
        return dateCheckIn;
    }

    public DtoReportVisit setDateCheckIn(long dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
        return this;
    }

    public long getDateCheckOut() {
        return dateCheckOut;
    }

    public DtoReportVisit setDateCheckOut(long dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
        return this;
    }

    @Override
    public String toString() {
        return "DtoReportVisit{" +
                "id=" + id +
                ", idPdv=" + idPdv +
                ", send=" + send +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", password='" + password + '\'' +
                ", dateCheckIn=" + dateCheckIn +
                ", dateCheckOut=" + dateCheckOut +
                '}';
    }
}
