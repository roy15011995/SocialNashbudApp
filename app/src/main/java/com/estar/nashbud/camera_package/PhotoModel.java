package com.estar.nashbud.camera_package;

import android.net.Uri;

import java.util.ArrayList;

public class PhotoModel {

    String str_folder,time;
    ArrayList<String> al_imagepath;
    Uri uri;
    int headerId;
    String all_imagePath;

    public String getAll_imagePath() {
        return all_imagePath;
    }

    public void setAll_imagePath(String all_imagePath) {
        this.all_imagePath = all_imagePath;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public String getStr_folder() {
        return str_folder;
    }

    public void setStr_folder(String str_folder) {
        this.str_folder = str_folder;
    }

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getHeaderId() {
        return headerId;
    }

    public void setHeaderId(int headerId) {
        this.headerId = headerId;
    }
}
