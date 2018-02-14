package net.gshp.observatoriociudadano.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leo on 21/08/17.
 */

public class DtoPolitic {
    @SerializedName("version")
    private String version;
    @SerializedName("md5")
    private String value;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
