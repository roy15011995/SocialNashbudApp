package com.estar.nashbud.comments;

/*
 * Created by Mahmoud on 3/13/2017.
 */

public class Comments {

    private long timestamp;
    private long negatedTimestamp;
    private long dayTimestamp;
    private String body;
    private String from;
    private String to;
    private String status;
    private String displayName;
    private String photoUrl;

    public Comments(long timestamp, long negatedTimestamp, long dayTimestamp, String body, String from, String displayName, String photoUrl) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }


    public Comments() {
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getNegatedTimestamp() {
        return negatedTimestamp;
    }

    public String getTo() {
        return to;
    }

    public long getDayTimestamp() {
        return dayTimestamp;
    }

    public String getFrom() {
        return from;
    }

    public String getBody() {
        return body;
    }
}
