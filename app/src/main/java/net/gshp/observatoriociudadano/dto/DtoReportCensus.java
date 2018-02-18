package net.gshp.observatoriociudadano.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by leo on 17/02/18.
 */

public class DtoReportCensus implements Parcelable {

    private long idReporteLocal;
    private double lat;
    private double lon;
    private String suburb;
    private String state;
    private String town;
    private String cp;
    private String externalNumber;
    private String hash;
    private int send;
    private long date;
    private String address;
    private String provider;
    private String address_left;
    private String address_right;
    private String internalNumber;


    @Override
    public String toString() {
        return "DtoReportCensus{" +
                "idReporteLocal=" + idReporteLocal +
                ", lat=" + lat +
                ", lon=" + lon +
                ", suburb='" + suburb + '\'' +
                ", state='" + state + '\'' +
                ", town='" + town + '\'' +
                ", cp='" + cp + '\'' +
                ", externalNumber='" + externalNumber + '\'' +
                ", hash='" + hash + '\'' +
                ", send=" + send +
                ", date=" + date +
                ", address='" + address + '\'' +
                ", provider='" + provider + '\'' +
                ", address_left='" + address_left + '\'' +
                ", address_right='" + address_right + '\'' +
                ", internalNumber='" + internalNumber + '\'' +
                '}';
    }

    public long getIdReporteLocal() {
        return idReporteLocal;
    }

    public DtoReportCensus setIdReporteLocal(long idReporteLocal) {
        this.idReporteLocal = idReporteLocal;
        return this;
    }

    public double getLat() {
        return lat;
    }

    public DtoReportCensus setLat(double lat) {
        this.lat = lat;
        return this;
    }

    public double getLon() {
        return lon;
    }

    public DtoReportCensus setLon(double lon) {
        this.lon = lon;
        return this;
    }

    public String getSuburb() {
        return suburb;
    }

    public DtoReportCensus setSuburb(String suburb) {
        this.suburb = suburb;
        return this;
    }

    public String getState() {
        return state;
    }

    public DtoReportCensus setState(String state) {
        this.state = state;
        return this;
    }

    public String getTown() {
        return town;
    }

    public DtoReportCensus setTown(String town) {
        this.town = town;
        return this;
    }

    public String getCp() {
        return cp;
    }

    public DtoReportCensus setCp(String cp) {
        this.cp = cp;
        return this;
    }

    public String getExternalNumber() {
        return externalNumber;
    }

    public DtoReportCensus setExternalNumber(String externalNumber) {
        this.externalNumber = externalNumber;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public DtoReportCensus setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public int getSend() {
        return send;
    }

    public DtoReportCensus setSend(int send) {
        this.send = send;
        return this;
    }

    public long getDate() {
        return date;
    }

    public DtoReportCensus setDate(long date) {
        this.date = date;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public DtoReportCensus setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public DtoReportCensus setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public String getAddress_left() {
        return address_left;
    }

    public DtoReportCensus setAddress_left(String address_left) {
        this.address_left = address_left;
        return this;
    }

    public String getAddress_right() {
        return address_right;
    }

    public DtoReportCensus setAddress_right(String address_right) {
        this.address_right = address_right;
        return this;
    }

    public String getInternalNumber() {
        return internalNumber;
    }

    public DtoReportCensus setInternalNumber(String internalNumber) {
        this.internalNumber = internalNumber;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.idReporteLocal);
        dest.writeDouble(this.lat);
        dest.writeDouble(this.lon);
        dest.writeString(this.suburb);
        dest.writeString(this.state);
        dest.writeString(this.town);
        dest.writeString(this.cp);
        dest.writeString(this.externalNumber);
        dest.writeString(this.hash);
        dest.writeInt(this.send);
        dest.writeLong(this.date);
        dest.writeString(this.address);
        dest.writeString(this.provider);
        dest.writeString(this.address_left);
        dest.writeString(this.address_right);
        dest.writeString(this.internalNumber);
    }

    public DtoReportCensus() {
    }

    protected DtoReportCensus(Parcel in) {
        this.idReporteLocal = in.readLong();
        this.lat = in.readDouble();
        this.lon = in.readDouble();
        this.suburb = in.readString();
        this.state = in.readString();
        this.town = in.readString();
        this.cp = in.readString();
        this.externalNumber = in.readString();
        this.hash = in.readString();
        this.send = in.readInt();
        this.date = in.readLong();
        this.address = in.readString();
        this.provider = in.readString();
        this.address_left = in.readString();
        this.address_right = in.readString();
        this.internalNumber = in.readString();
    }

    public static final Creator<DtoReportCensus> CREATOR = new Creator<DtoReportCensus>() {
        @Override
        public DtoReportCensus createFromParcel(Parcel source) {
            return new DtoReportCensus(source);
        }

        @Override
        public DtoReportCensus[] newArray(int size) {
            return new DtoReportCensus[size];
        }
    };
}
