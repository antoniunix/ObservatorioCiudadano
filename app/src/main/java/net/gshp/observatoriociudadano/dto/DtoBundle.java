package net.gshp.observatoriociudadano.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class DtoBundle implements Parcelable {

    private long idPdv;
    private long idReportLocal;
    private long idRoll;
    private long idTypeMenuReport;// es para saber si esta entrando a menu report Supervisor ó representante de casilla

    public long getIdPdv() {
        return idPdv;
    }

    public DtoBundle setIdPdv(long idPdv) {
        this.idPdv = idPdv;
        return this;
    }

    public long getIdReportLocal() {
        return idReportLocal;
    }

    public DtoBundle setIdReportLocal(long idReportLocal) {
        this.idReportLocal = idReportLocal;
        return this;
    }

    public long getIdRoll() {
        return idRoll;
    }

    public DtoBundle setIdRoll(long idRoll) {
        this.idRoll = idRoll;
        return this;
    }

    public long getIdTypeMenuReport() {
        return idTypeMenuReport;
    }

    public DtoBundle setIdTypeMenuReport(long idTypeMenuReport) {
        this.idTypeMenuReport = idTypeMenuReport;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.idPdv);
        dest.writeLong(this.idReportLocal);
        dest.writeLong(this.idRoll);
        dest.writeLong(this.idTypeMenuReport);
    }

    public DtoBundle() {
    }

    protected DtoBundle(Parcel in) {
        this.idPdv = in.readLong();
        this.idReportLocal = in.readLong();
        this.idRoll = in.readLong();
        this.idTypeMenuReport = in.readLong();
    }

    public static final Parcelable.Creator<DtoBundle> CREATOR = new Parcelable.Creator<DtoBundle>() {
        @Override
        public DtoBundle createFromParcel(Parcel source) {
            return new DtoBundle(source);
        }

        @Override
        public DtoBundle[] newArray(int size) {
            return new DtoBundle[size];
        }
    };
}