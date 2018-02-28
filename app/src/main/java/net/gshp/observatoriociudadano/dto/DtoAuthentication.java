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
    private boolean cameraFront;
    private String RAM;
    private String memoryExternal;
    private String memoryInternal;
    private String carrier;
    private String phoneNumber;
    private String battery;

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

    public String getUuid() {
        return uuid;
    }

    public DtoAuthentication setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public boolean isCameraFront() {
        return cameraFront;
    }

    public DtoAuthentication setCameraFront(boolean cameraFront) {
        this.cameraFront = cameraFront;
        return this;
    }

    public String getRAM() {
        return RAM;
    }

    public DtoAuthentication setRAM(String RAM) {
        this.RAM = RAM;
        return this;
    }

    public String getMemoryExternal() {
        return memoryExternal;
    }

    public DtoAuthentication setMemoryExternal(String memoryExternal) {
        this.memoryExternal = memoryExternal;
        return this;
    }

    public String getMemoryInternal() {
        return memoryInternal;
    }

    public DtoAuthentication setMemoryInternal(String memoryInternal) {
        this.memoryInternal = memoryInternal;
        return this;
    }

    public String getCarrier() {
        return carrier;
    }

    public DtoAuthentication setCarrier(String carrier) {
        this.carrier = carrier;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public DtoAuthentication setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getBattery() {
        return battery;
    }

    public DtoAuthentication setBattery(String battery) {
        this.battery = battery;
        return this;
    }

    @Override
    public String toString() {
        return "DtoAuthentication{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", imei='" + imei + '\'' +
                ", brand='" + brand + '\'' +
                ", os='" + os + '\'' +
                ", osVersion='" + osVersion + '\'' +
                ", phone='" + phone + '\'' +
                ", model='" + model + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", uuid='" + uuid + '\'' +
                ", cameraFront='" + cameraFront + '\'' +
                ", RAM='" + RAM + '\'' +
                ", memoryExternal='" + memoryExternal + '\'' +
                ", memoryInternal='" + memoryInternal + '\'' +
                ", carrier='" + carrier + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", battery='" + battery + '\'' +
                '}';
    }
}
