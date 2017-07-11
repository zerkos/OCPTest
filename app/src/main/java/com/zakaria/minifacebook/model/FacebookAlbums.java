package com.zakaria.minifacebook.model;

import android.os.Parcel;
import android.os.Parcelable;


public class FacebookAlbums implements Parcelable {


    private String created_time;
    private String name;
    private String id;

    public FacebookAlbums(String created_time, String name, String id) {
        this.created_time = created_time;
        this.name = name;
        this.id = id;
    }

    public String getCreated_time() {
        return created_time;
    }

    public void setCreated_time(String created_time) {
        this.created_time = created_time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(created_time);
        parcel.writeString(name);
        parcel.writeString(id);
    }
    public static final Parcelable.Creator<FacebookAlbums> CREATOR = new Parcelable.Creator<FacebookAlbums>()
    {
        @Override
        public FacebookAlbums createFromParcel(Parcel source)
        {
            return new FacebookAlbums(source);
        }

        @Override
        public FacebookAlbums[] newArray(int size)
        {
            return new FacebookAlbums[size];
        }
    };

    public FacebookAlbums(Parcel in) {
        this.created_time = in.readString();
        this.name = in.readString();
        this.id = in.readString();
    }
}
