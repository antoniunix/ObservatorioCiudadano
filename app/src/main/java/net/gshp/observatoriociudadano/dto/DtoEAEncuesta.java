package net.gshp.observatoriociudadano.dto;

import com.google.gson.annotations.SerializedName;


public class DtoEAEncuesta {

    public int id;
    public String name;
    public long date_start;
    public long date_end;

    @SerializedName("repeat_ea_poll")
    public int repeat;
    public String description;
    public String canal;

    public String rtm;
    public String cliente;
    public String pdv;
    public String query;
    public boolean rtEnabled;

    @SerializedName("status")
    public long estado;
    public String region;
    public int restriction;

    public String getRegion() {
        return region;
    }

    public DtoEAEncuesta setRegion(String region) {
        this.region = region;
        return this;
    }

    public int getRestriction() {
        return restriction;
    }

    public DtoEAEncuesta setRestriction(int restriction) {
        this.restriction = restriction;
        return this;
    }

    public boolean isRtEnabled() {
        return rtEnabled;
    }

    public void setRtEnabled(boolean rtEnabled) {
        this.rtEnabled = rtEnabled;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDate_start() {
        return date_start;
    }

    public void setDate_start(long date_start) {
        this.date_start = date_start;
    }

    public long getDate_end() {
        return date_end;
    }

    public void setDate_end(long date_end) {
        this.date_end = date_end;
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCanal() {
        return canal;
    }

    public void setCanal(String canal) {
        this.canal = canal;
    }

    public String getRtm() {
        return rtm;
    }

    public void setRtm(String rtm) {
        this.rtm = rtm;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getPdv() {
        return pdv;
    }

    public void setPdv(String pdv) {
        this.pdv = pdv;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public long getEstado() {
        return estado;
    }

    public void setEstado(long estado) {
        this.estado = estado;
    }

}