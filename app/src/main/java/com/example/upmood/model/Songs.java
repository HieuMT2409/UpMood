package com.example.upmood.model;

import java.io.Serializable;

public class Songs implements Serializable {
    private String idAlbum;
    private String idChude;
    private String image;
    private String liked;
    private String linkSong;
    private String nameSong;
    private String singer;

    public Songs() {
    }

    public Songs(String idAlbum, String idChude, String image, String liked, String linkSong, String nameSong, String singer) {
        this.idAlbum = idAlbum;
        this.idChude = idChude;
        this.image = image;
        this.liked = liked;
        this.linkSong = linkSong;
        this.nameSong = nameSong;
        this.singer = singer;
    }

    public String getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(String idAlbum) {
        this.idAlbum = idAlbum;
    }

    public String getIdChude() {
        return idChude;
    }

    public void setIdChude(String idChude) {
        this.idChude = idChude;
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
                "idAlbum=" + idAlbum +
                ", idChude=" + idChude +
                ", image='" + image + '\'' +
                ", liked=" + liked +
                ", linkSong='" + linkSong + '\'' +
                ", nameSong='" + nameSong + '\'' +
                ", singer='" + singer + '\'' +
                '}';
    }


}
