package com.npdevelopment.gamebacklog.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

@Entity(tableName = "gamebacklog_table")
public class Game implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String title;
    private String platform;
    private String status;
    private String date;
    private String date_edited;

    public Game(String title, String platform, String status, String date, String date_edited) {
        this.title = title;
        this.platform = platform;
        this.status = status;
        this.date= date;
        this.date_edited = date_edited;
    }

    protected Game(Parcel parcel) {
        id = parcel.readInt();
        title = parcel.readString();
        platform = parcel.readString();
        status = parcel.readString();
        date =  parcel.readString();
        date_edited = parcel.readString();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_edited() {
        return date_edited;
    }

    public void setDate_edited(String date_edited) {
        this.date_edited = date_edited;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", platform='" + platform + '\'' +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", date_edited='" + date_edited + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.platform);
        parcel.writeString(this.status);
        parcel.writeString(this.date);
        parcel.writeString(this.date_edited);
    }

    public static final Creator<Game> CREATOR = new Creator<Game>() {
        @Override
        public Game createFromParcel(Parcel parcel) {
            return new Game(parcel);
        }

        @Override
        public Game[] newArray(int i) {
            return new Game[i];
        }
    };
}
