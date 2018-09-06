package com.estar.nashbud.upload_photo;

/*
 * Created by Mahmoud on 3/13/2017.
 */

public class Message {

    private long timestamp;
    private long negatedTimestamp;
    private long dayTimestamp;
    private String body;
    private String from;
    private String to;
    private String status;
    private String displayName;
    private String photoUrl;
    public String captionphotoUrl;
    public String currentuserName;
    public String currentuserPhoto;

    /*public Message(long timestamp, long negatedTimestamp, long dayTimestamp, String body, String from, String to, String status, String displayName, String photoUrl) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.to = to;
        this.status = status;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }*/

    public Message(long timestamp, long negatedTimestamp, long dayTimestamp, String body, String from, String to, String status, String displayName, String photoUrl, String captionphotoUrl,String currentuserName, String currentuserPhoto) {
        this.timestamp = timestamp;
        this.negatedTimestamp = negatedTimestamp;
        this.dayTimestamp = dayTimestamp;
        this.body = body;
        this.from = from;
        this.to = to;
        this.status = status;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
        this.captionphotoUrl = captionphotoUrl;
        this.currentuserName = currentuserName;
        this.currentuserPhoto = currentuserPhoto;
    }

    public Message() {
    }


    public String getCurrentuserName() {
        return currentuserName;
    }

    public void setCurrentuserName(String currentuserName) {
        this.currentuserName = currentuserName;
    }

    public String getCurrentuserPhoto() {
        return currentuserPhoto;
    }

    public void setCurrentuserPhoto(String currentuserPhoto) {
        this.currentuserPhoto = currentuserPhoto;
    }

    public String getCaptionphotoUrl() {
        return captionphotoUrl;
    }

    public void setCaptionphotoUrl(String captionphotoUrl) {
        this.captionphotoUrl = captionphotoUrl;
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
