package com.estar.nashbud.camera_package;

import java.util.ArrayList;

public class ImageModel {
    ArrayList<String> al_imagepath;
    String imagePath,sendFeedback;


    public ImageModel(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }
    public ImageModel(){

    }

    public String getSendFeedback() {
        return sendFeedback;
    }

    public void setSendFeedback(String sendFeedback) {
        this.sendFeedback = sendFeedback;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }
}
