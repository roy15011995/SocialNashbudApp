package com.estar.nashbud.upload_photo;

/*
 * Created by User on 12/7/2017.
 */

public class User {

    private String displayName;
    private String email;
    private String uid;
    private String photoUrl;
    private String instanceId;
    private String mobileNo;
    private String userName;
    private String bio;
    private String website;
    private String gender;
    private Boolean online;
    private Long time;
    private String message;
    private Long timeStamp;
    private String status;
    private String current_time;
    private Boolean check;
    private String DefaultText;

    public User() {
    }

    public User(String displayName, String email, String uid, String photoUrl, String instanceId, String mobileNo, String userName, String bio, String website, String gender, Boolean online, Long time, String message, Long timeStamp, String status, String current_time, Boolean check) {
        this.displayName = displayName;
        this.email = email;
        this.uid = uid;
        this.photoUrl = photoUrl;
        this.instanceId = instanceId;
        this.mobileNo = mobileNo;
        this.userName = userName;
        this.bio = bio;
        this.website = website;
        this.gender = gender;
        this.online = online;
        this.time = time;
        this.message = message;
        this.timeStamp = timeStamp;
        this.status = status;
        this.current_time = current_time;
        this.check = check;
    }

    public void setDefaultText(String defaultText) {
        DefaultText = defaultText;
    }

    public String getDefaultText() {
        return DefaultText;
    }

    public String getCurrent_time() {
        return current_time;
    }

    public void setCurrent_time(String current_time) {
        this.current_time = current_time;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmail() {
        return email;
    }

    public String getUid() {
        return uid;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }
}
