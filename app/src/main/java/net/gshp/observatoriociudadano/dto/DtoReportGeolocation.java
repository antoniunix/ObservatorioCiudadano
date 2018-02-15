package net.gshp.observatoriociudadano.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by leo on 20/08/17.
 */

public class DtoReportGeolocation {
    private int id;
    @SerializedName("lat")
    private double latitude;
    @SerializedName("lon")
    private double longitude;
    private String battery;
    private String accuracy;
    @SerializedName("IMEI")
    private String imei;
    @SerializedName("satelliteUTC")
    private String satelliteUtc;
    private String date;
    private String tz;
    @SerializedName("appVersion")
    private String version;
    private int send;
    private String hash;
    private String provider;
    @SerializedName("fakeLocEnabled")
    private boolean fakelocationEnabled;

    public int getId() {
        return id;
    }

    public DtoReportGeolocation setId(int id) {
        this.id = id;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public DtoReportGeolocation setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public DtoReportGeolocation setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getBattery() {
        return battery;
    }

    public DtoReportGeolocation setBattery(String battery) {
        this.battery = battery;
        return this;
    }

    public String getAccuracy() {
        return accuracy;
    }

    public DtoReportGeolocation setAccuracy(String accuracy) {
        this.accuracy = accuracy;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public DtoReportGeolocation setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getSatelliteUtc() {
        return satelliteUtc;
    }

    public DtoReportGeolocation setSatelliteUtc(String satelliteUtc) {
        this.satelliteUtc = satelliteUtc;
        return this;
    }

    public String getDate() {
        return date;
    }

    public DtoReportGeolocation setDate(String date) {
        this.date = date;
        return this;
    }

    public String getTz() {
        return tz;
    }

    public DtoReportGeolocation setTz(String tz) {
        this.tz = tz;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public DtoReportGeolocation setVersion(String version) {
        this.version = version;
        return this;
    }

    public int getSend() {
        return send;
    }

    public DtoReportGeolocation setSend(int send) {
        this.send = send;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public DtoReportGeolocation setHash(String hash) {
        this.hash = hash;
        return this;
    }

    public String getProvider() {
        return provider;
    }

    public DtoReportGeolocation setProvider(String provider) {
        this.provider = provider;
        return this;
    }

    public boolean isFakelocationEnabled() {
        return fakelocationEnabled;
    }

    public DtoReportGeolocation setFakelocationEnabled(boolean fakelocationEnabled) {
        this.fakelocationEnabled = fakelocationEnabled;
        return this;
    }
}
