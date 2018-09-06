package com.estar.nashbud.post;

/**
 * Created by User on 28-02-2018.
 */

public class Tag_People_Model {
    String name;
    String profile_pic;
    String Uid;
//    int image_cross;

    public Tag_People_Model(String name,  String profile_pic) {
        this.name = name;
        this.profile_pic=profile_pic;
        //this.image_cross=image_cross;
    }

    public Tag_People_Model() {
    }
    /*public int getImage_cross() {
        return image_cross;
    }

    public void setImage_cross(int image_cross) {
        this.image_cross = image_cross;
    }*/

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*public int getImage_cross() {
        return image_cross;
    }

    public void setImage_cross(int image_cross) {
        this.image_cross = image_cross;
    }*/
}
