package com.estar.nashbud.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Set;

/**
 * Created by saurabh on 8/16/2017.
 */

public class SharedPreference {
    public static final String PREFS_NAME_USERNAME = "AOP_PREFS_USERNAME";
    public static final String PREFS_KEY_EMP = "AOP_PREFS_OPERATOR";
    public static final String PREF_KEY_PHNO = "AOP_PREF_PHNO";
    public static final String PREF_KEY_PROFILEPIC = "AOP_PREF_PROFILEPIC";
    public static final String PREF_KEY_FULLPICDATA = "AOP_PREF_FULLPICDATA";
    public static final String PREF_KEY_lASTSENDMESSAGE = "AOP_PREF_lASTSENDMESSAGE";
    public static final String PREF_KEY_TAGPEOPLE = "AOP_PREF_TAGPEOPLE";


    public SharedPreference() {
        super();
    }

    public void saveUserName(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME_USERNAME, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY_EMP, text);
        editor.commit();
    }

    public String getUserName(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREFS_NAME_USERNAME, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY_EMP, null);
        return text;
    }

    public void savePhNo(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_KEY_PHNO, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY_EMP, text);
        editor.commit();
    }

    public String getPhNo(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREF_KEY_PHNO, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY_EMP, null);
        return text;
    }

    public void saveProfilePic(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_KEY_PROFILEPIC, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY_EMP, text);
        editor.commit();
    }

    public String getProfilePic(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREF_KEY_PROFILEPIC, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY_EMP, null);
        return text;
    }

    public void saveFullPicData(Context context, String text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_KEY_FULLPICDATA, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putString(PREFS_KEY_EMP, text);
        editor.commit();
    }

    public String getFullPicData(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREF_KEY_FULLPICDATA, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY_EMP, null);
        return text;
    }

    public void clearPhNo(Context context) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences(PREF_KEY_PHNO, Context.MODE_PRIVATE);

        editor = settings.edit();

        editor.clear();
        editor.commit();
    }

    public String lastSendMessage(Context context) {
        SharedPreferences settings;
        String text;
        settings = context.getSharedPreferences(PREF_KEY_lASTSENDMESSAGE, Context.MODE_PRIVATE);
        text = settings.getString(PREFS_KEY_EMP, null);
        return text;
    }

    public void saveTagPeople(Context context, Set<String> text) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;
        settings = context.getSharedPreferences(PREF_KEY_PHNO, Context.MODE_PRIVATE);
        editor = settings.edit();
        editor.putStringSet(PREFS_KEY_EMP, text);
        editor.commit();
    }
}
