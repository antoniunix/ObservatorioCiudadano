package net.gshp.observatoriociudadano.dto;

/**
 * Created by alejandro on 16/02/18.
 */

public class DtoImageLogin {

    private int id;
    private String path;
    private String name;
    private int sent;
    private int rol;

    public DtoImageLogin(String path, String name, int sent, int rol) {
        this.path = path;
        this.name = name;
        this.sent = sent;
        this.rol = rol;
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

    public int getRol() {
        return rol;
    }

    public void setRol(int rol) {
        this.rol = rol;
    }
}
