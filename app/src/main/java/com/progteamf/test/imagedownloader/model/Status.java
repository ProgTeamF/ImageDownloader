package com.progteamf.test.imagedownloader.model;

public enum Status {

    DOWNLOADED, ERROR, UNKNOWN;

    private String message;

    Status() {
    }

    Status(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Status{" +
                "message='" + message + '\'' +
                '}';
    }
}
