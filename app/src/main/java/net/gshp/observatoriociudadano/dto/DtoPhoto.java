package net.gshp.observatoriociudadano.dto;

/**
 * Created by alejandro on 15/02/18.
 */

public class DtoPhoto {

    private int id;
    private String path;
    private String name;
    private int face_id;
    private int sent;
    private int rol;
    private String user;
    private long report_id;

    public DtoPhoto(String path, String name, int face_id, int sent, int rol, String user, long report_id) {
        this.path = path;
        this.name = name;
        this.face_id = face_id;
        this.sent = sent;
        this.rol = rol;
        this.user = user;
        this.report_id = report_id;
    }

    public DtoPhoto() {
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

    public int getFace_id() {
        return face_id;
    }

    public void setFace_id(int face_id) {
        this.face_id = face_id;
    }

    public int getSent() {
        return sent;
    }

    public void setSent(int sent) {
        this.sent = sent;
    }

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public long getReport_id() {
        return report_id;
    }

    public void setReport_id(long report_id) {
        this.report_id = report_id;
    }
}
