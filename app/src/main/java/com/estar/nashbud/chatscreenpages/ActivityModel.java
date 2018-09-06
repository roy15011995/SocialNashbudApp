package com.estar.nashbud.chatscreenpages;

/**
 * Created by User on 24-03-2018.
 */

public class ActivityModel {
    private String TagWith,NoUsers,Others;

    public ActivityModel(String tagWith, String noUsers, String others) {
        TagWith = tagWith;
        NoUsers = noUsers;
        Others = others;
    }

    public String getTagWith() {
        return TagWith;
    }

    public void setTagWith(String tagWith) {
        TagWith = tagWith;
    }

    public String getNoUsers() {
        return NoUsers;
    }

    public void setNoUsers(String noUsers) {
        NoUsers = noUsers;
    }

    public String getOthers() {
        return Others;
    }

    public void setOthers(String others) {
        Others = others;
    }
}
