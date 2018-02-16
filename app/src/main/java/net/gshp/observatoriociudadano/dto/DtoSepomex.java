package net.gshp.observatoriociudadano.dto;

/**
 * Created by leo on 15/02/18.
 */

public class DtoSepomex {
    private String postalCode;
    private String suburb;
    private String type_suburb;
    private String town;
    private String state;

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public String getType_suburb() {
        return type_suburb;
    }

    public void setType_suburb(String type_suburb) {
        this.type_suburb = type_suburb;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
