package net.gshp.observatoriociudadano.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by omara on 06/04/2017.
 */

public class DtoEaRespuestaPdv {

    @SerializedName("id_poll")
    private long idPoll;
    @SerializedName("id_pdv")
    private long idPdv;

    public long getIdPoll() {
        return idPoll;
    }

    public void setIdPoll(long idPoll) {
        this.idPoll = idPoll;
    }

    public long getIdPdv() {
        return idPdv;
    }

    public void setIdPdv(long idPdv) {
        this.idPdv = idPdv;
    }
}
