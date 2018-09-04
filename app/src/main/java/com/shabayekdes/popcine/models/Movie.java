package com.shabayekdes.popcine.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movie implements Parcelable {

    private int id;
    private String title;
    private String voteAverage;
    private String overview;
    private String releaseDate;
    private String posterPath;

    protected Movie(Parcel in) {
        id = in.readInt();
        title = in.readString();
        voteAverage = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
    }
    public Movie() {

    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setVoteAverage(String vote_average) {
        this.voteAverage = vote_average;
    }

    public String getVoteAverage() {
        return voteAverage;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    public void setReleaseDate(String release_date) {
        this.releaseDate = release_date;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setPosterPath(String poster_path) {
        this.posterPath = poster_path;
    }

    public String getPosterPath() {
        return posterPath;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.voteAverage);
        parcel.writeString(this.overview);
        parcel.writeString(this.releaseDate);
        parcel.writeString(this.posterPath);

    }
}
