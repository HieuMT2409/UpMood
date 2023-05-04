package com.example.upmood.model;

import java.io.Serializable;

public class Chill implements Serializable {
    private String image;
    private String liked;
    private String linkSong;
    private String nameSong;
    private String singer;

    public Chill() {
    }

    public Chill(String image, String liked, String linkSong, String nameSong, String singer) {
        this.image = image;
        this.liked = liked;
        this.linkSong = linkSong;
        this.nameSong = nameSong;
        this.singer = singer;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLiked() {
        return liked;
    }

    public void setLiked(String liked) {
        this.liked = liked;
    }

    public String getLinkSong() {
        return linkSong;
    }

    public void setLinkSong(String linkSong) {
        this.linkSong = linkSong;
    }

    public String getNameSong() {
        return nameSong;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    @Override
    public String toString() {
        return "Songs{" +
                ", image='" + image + '\'' +
                ", liked=" + liked +
                ", linkSong='" + linkSong + '\'' +
                ", nameSong='" + nameSong + '\'' +
                ", singer='" + singer + '\'' +
                '}';
    }


}
