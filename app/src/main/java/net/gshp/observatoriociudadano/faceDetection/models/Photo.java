package net.gshp.observatoriociudadano.faceDetection.models;

/**
 * Created by alejandro on 15/02/18.
 */

public class Photo {
    private String name;
    private int thumbnail;
    private String picture;
    private int rotation;

    public Photo() {
    }

    public Photo(String name, int thumbnail, String picture) {
        this.name = name;
        this.thumbnail = thumbnail;
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }
}
