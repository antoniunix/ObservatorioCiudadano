package net.gshp.observatoriociudadano.dto;

/**
 * Created by gnu on 8/03/17.
 */

public class DtoAuthentication {

    private String username;
    private String password;
    private String imei;
    private String brand;
    private String os;
    private String osVersion;
    private String phone;
    private String model;
    private String deviceId;
    private String newPassword;
    private String uuid;

    public String getUuid() {
        return uuid;
    }

    public DtoAuthentication setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public String getUsername() {
        return username;
    }

    public DtoAuthentication setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public DtoAuthentication setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getImei() {
        return imei;
    }

    public DtoAuthentication setImei(String imei) {
        this.imei = imei;
        return this;
    }

    public String getBrand() {
        return brand;
    }

    public DtoAuthentication setBrand(String brand) {
        this.brand = brand;
        return this;
    }

    public String getOs() {
        return os;
    }

    public DtoAuthentication setOs(String os) {
        this.os = os;
        return this;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public DtoAuthentication setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public DtoAuthentication setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getModel() {
        return model;
    }

    public DtoAuthentication setModel(String model) {
        this.model = model;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public DtoAuthentication setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public DtoAuthentication setNewPassword(String newPassword) {
        this.newPassword = newPassword;
        return this;
    }
}
