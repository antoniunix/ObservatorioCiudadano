package net.gshp.observatoriociudadano.dto;

import com.google.gson.annotations.Expose;

/**
 * Created by alejandro on 16/02/18.
 */

public class DtoImageLogin {

    private int id;
    @Expose(serialize = false)
    private String path;
    @Expose(serialize = false)
    private String name;
    @Expose(serialize = false)
    private int sent;
    private String tableName;
    private String md5;
    private String version;
    private String hash;
    private String description;
    private String personId;
    private String userId;
    private int placeId;

    public DtoImageLogin(String path, String name, int sentl) {
        this.path = path;
        this.name = name;
        this.sent = sent;
    }

    public DtoImageLogin() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }
}
