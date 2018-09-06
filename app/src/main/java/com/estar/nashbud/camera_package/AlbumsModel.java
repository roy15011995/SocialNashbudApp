package com.estar.nashbud.camera_package;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.TreeSet;

public class AlbumsModel implements Serializable {

    private static final long serialVersionUID = 1L;
    public TreeSet<String> folderImages;
    protected String folderName;
    protected String folderImagePath;
    private boolean isSelected;
    ArrayList<String> al_imagepath;

    public ArrayList<String> getAl_imagepath() {
        return al_imagepath;
    }

    public void setAl_imagepath(ArrayList<String> al_imagepath) {
        this.al_imagepath = al_imagepath;
    }

    public AlbumsModel () {
        folderImages = new TreeSet<String>();
    }
    public boolean isSelected () {
        return isSelected;
    }
    public void setSelected (boolean isSelected) {
        this.isSelected = isSelected;
    }
    public String getFolderName () {
        return folderName;
    }
    public void setFolderName (String folderName) {
        this.folderName = folderName;
    }
    public String getFolderImagePath () {
        return folderImagePath;
    }
    public void setFolderImagePath (String folderImagePath) {
        this.folderImagePath = folderImagePath;
    }
}
