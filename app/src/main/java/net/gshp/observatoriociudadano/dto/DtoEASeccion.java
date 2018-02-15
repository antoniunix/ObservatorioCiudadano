package net.gshp.observatoriociudadano.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class DtoEASeccion implements Parcelable {

	private Integer id;
	@SerializedName("poll")
	private Integer idEncuesta;
	@SerializedName("parent")
	private Integer idParent;
	@SerializedName("order_by")
	private Integer orden;
	@SerializedName("weight")
	private Integer peso;
	@SerializedName("name")
	private String nombre;
	
	public DtoEASeccion(){}

    protected DtoEASeccion(Parcel in) {
        id = in.readByte() == 0x00 ? null : in.readInt();
        idEncuesta = in.readByte() == 0x00 ? null : in.readInt();
        idParent = in.readByte() == 0x00 ? null : in.readInt();
        orden = in.readByte() == 0x00 ? null : in.readInt();
        peso = in.readByte() == 0x00 ? null : in.readInt();
        nombre = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (id == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(id);
        }
        if (idEncuesta == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idEncuesta);
        }
        if (idParent == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(idParent);
        }
        if (orden == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(orden);
        }
        if (peso == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(peso);
        }
        dest.writeString(nombre);
    }

    public static final Creator<DtoEASeccion> CREATOR = new Creator<DtoEASeccion>() {
        @Override
        public DtoEASeccion createFromParcel(Parcel in) {
            return new DtoEASeccion(in);
        }

        @Override
        public DtoEASeccion[] newArray(int size) {
            return new DtoEASeccion[size];
        }
    };

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getIdEncuesta() {
		return idEncuesta;
	}

	public void setIdEncuesta(Integer idEncuesta) {
		this.idEncuesta = idEncuesta;
	}

	public Integer getIdParent() {
		return idParent;
	}

	public void setIdParent(Integer idParent) {
		this.idParent = idParent;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public Integer getPeso() {
		return peso;
	}

	public void setPeso(Integer peso) {
		this.peso = peso;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
    
}