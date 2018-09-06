package com.estar.nashbud.post;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by User on 28-02-2018.
 */

public class Post_Model {
    String postImage;
    ArrayList<String> tagPeople_Path ,al_imagepath , likeInput;


    String postMessage,pics,mobile_number,uid,profilename,profilePic,postDate,postTime,postFullName,place,imagePath,like,timestamp;
    public Post_Model() {
    }

    public ArrayList<String> getLikeInput() {
        return likeInput;
    }


    public void setLikeInput(ArrayList<String> likeInput) {
        this.likeInput = likeInput;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;

    }

    public String getLike() {
        return like;
    }

    public void setLike(String like) {
        this.like = like;
    }

//    public ArrayList<String> getTagPeople_Path() {
//        return tagPeople_Path;
//    }
//
//    public void setTagPeople_Path(ArrayList<String> tagPeople_Path) {
//        this.tagPeople_Path = tagPeople_Path;
//    }


    public ArrayList<String> getTagPeople_Path() {
        return tagPeople_Path;
    }

    public void setTagPeople_Path(ArrayList<String> tagPeople_Path) {
        this.tagPeople_Path = tagPeople_Path;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }


    public String getPostMessage() {
        return postMessage;
    }

    public void setPostMessage(String postMessage) {
        this.postMessage = postMessage;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfilename() {
        return profilename;
    }

    public void setProfilename(String profilename) {
        this.profilename = profilename;
    }


    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    public String getPostFullName() {
        return postFullName;
    }

    public void setPostFullName(String postFullName) {
        this.postFullName = postFullName;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


}
