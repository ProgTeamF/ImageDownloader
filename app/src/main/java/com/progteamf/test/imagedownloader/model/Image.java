package com.progteamf.test.imagedownloader.model;

public class Image {

    private int id;
    private String link;
    private Status status;
    private String time;

    public Image() {
    }

    public Image(int id, String link, Status status, String time) {
        this.id = id;
        this.link = link;
        this.status = status;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", link='" + link + '\'' +
                ", status=" + status +
                ", time='" + time + '\'' +
                '}';
    }
}
