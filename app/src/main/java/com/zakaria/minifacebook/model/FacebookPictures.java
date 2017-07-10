package com.zakaria.minifacebook.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zakaria on 17-07-10.
 */

public class FacebookPictures implements Parcelable{

    private int height;
    private int width;
    private String source;

    public FacebookPictures(int height, int width, String source) {
        this.height = height;
        this.width = width;
        this.source = source;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(height);
        parcel.writeInt(width);
        parcel.writeString(source);
    }

    public static final Parcelable.Creator<FacebookPictures> CREATOR = new Parcelable.Creator<FacebookPictures>()
    {
        @Override
        public FacebookPictures createFromParcel(Parcel source)
        {
            return new FacebookPictures(source);
        }

        @Override
        public FacebookPictures[] newArray(int size)
        {
            return new FacebookPictures[size];
        }
    };

    public FacebookPictures(Parcel in) {
        this.source = in.readString();
        this.height = in.readInt();
        this.width = in.readInt();
    }
}
