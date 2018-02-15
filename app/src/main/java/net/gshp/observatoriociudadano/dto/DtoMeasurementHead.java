package net.gshp.observatoriociudadano.dto;

/**
 * Created by leo on 28/08/17.
 */

public class DtoMeasurementHead {
    private int id;
    //    @SerializedName("start_date")
    private String startDate;
    //    @SerializedName("end_date")
    private String endDate;
    private String description;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
