package net.gshp.observatoriociudadano.dto;

/**
 * Created by leo on 22/01/18.
 */

public class DtoReportVisit {
    private long id;
    private long idPdv;
    private int send;
    private String nombrePdv;
    private String codigoPdv;
    private String tipoPdv;
    private long dateCheckIn;
    private long dateCheckOut;

    public long getIdPdv() {
        return idPdv;
    }

    public void setIdPdv(long idPdv) {
        this.idPdv = idPdv;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSend() {
        return send;
    }

    public void setSend(int send) {
        this.send = send;
    }

    public String getNombrePdv() {
        return nombrePdv;
    }

    public void setNombrePdv(String nombrePdv) {
        this.nombrePdv = nombrePdv;
    }

    public String getCodigoPdv() {
        return codigoPdv;
    }

    public void setCodigoPdv(String codigoPdv) {
        this.codigoPdv = codigoPdv;
    }

    public String getTipoPdv() {
        return tipoPdv;
    }

    public void setTipoPdv(String tipoPdv) {
        this.tipoPdv = tipoPdv;
    }

    public long getDateCheckIn() {
        return dateCheckIn;
    }

    public void setDateCheckIn(long dateCheckIn) {
        this.dateCheckIn = dateCheckIn;
    }

    public long getDateCheckOut() {
        return dateCheckOut;
    }

    public void setDateCheckOut(long dateCheckOut) {
        this.dateCheckOut = dateCheckOut;
    }

    @Override
    public String toString() {
        return "DtoReportVisit{" +
                "id=" + id +
                ", send=" + send +
                ", nombrePdv='" + nombrePdv + '\'' +
                ", codigoPdv='" + codigoPdv + '\'' +
                ", tipoPdv='" + tipoPdv + '\'' +
                ", dateCheckIn='" + dateCheckIn + '\'' +
                ", dateCheckOut='" + dateCheckOut + '\'' +
                '}';
    }
}
