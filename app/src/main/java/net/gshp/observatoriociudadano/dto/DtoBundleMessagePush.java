package net.gshp.observatoriociudadano.dto;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Antoniunix on 25/07/17.
 */

public class DtoBundleMessagePush implements Parcelable {

    private String title;
    private String message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.message);
    }

    public DtoBundleMessagePush() {
    }

    protected DtoBundleMessagePush(Parcel in) {
        this.title = in.readString();
        this.message = in.readString();
    }

    public static final Creator<DtoBundleMessagePush> CREATOR = new Creator<DtoBundleMessagePush>() {
        @Override
        public DtoBundleMessagePush createFromParcel(Parcel source) {
            return new DtoBundleMessagePush(source);
        }

        @Override
        public DtoBundleMessagePush[] newArray(int size) {
            return new DtoBundleMessagePush[size];
        }
    };

    @Override
    public String toString() {
        return "DtoBundleMessagePush{" +
                "title='" + title + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
