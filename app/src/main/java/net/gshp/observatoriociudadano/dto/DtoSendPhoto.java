package net.gshp.observatoriociudadano.dto;

/**
 * Created by gnu on 19/02/18.
 */

public class DtoSendPhoto {


    private long id;
    private long placeId;
    private String personId;
    private String userId;
    private String tableName;
    private String description;
    private String md5;
    private String version;
    private String hash;
    private String path;

    public String getPath() {
        return path;
    }

    public DtoSendPhoto setPath(String path) {
        this.path = path;
        return this;
    }

    public long getId() {
        return id;
    }

    public DtoSendPhoto setId(long id) {
        this.id = id;
        return this;
    }

    public long getPlaceId() {
        return placeId;
    }

    public DtoSendPhoto setPlaceId(long placeId) {
        this.placeId = placeId;
        return this;
    }

    public String getPersonId() {
        return personId;
    }

    public DtoSendPhoto setPersonId(String personId) {
        this.personId = personId;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public DtoSendPhoto setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public String getTableName() {
        return tableName;
    }

    public DtoSendPhoto setTableName(String tableName) {
        this.tableName = tableName;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public DtoSendPhoto setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getMd5() {
        return md5;
    }

    public DtoSendPhoto setMd5(String md5) {
        this.md5 = md5;
        return this;
    }

    public String getVersion() {
        return version;
    }

    public DtoSendPhoto setVersion(String version) {
        this.version = version;
        return this;
    }

    public String getHash() {
        return hash;
    }

    public DtoSendPhoto setHash(String hash) {
        this.hash = hash;
        return this;
    }
}
