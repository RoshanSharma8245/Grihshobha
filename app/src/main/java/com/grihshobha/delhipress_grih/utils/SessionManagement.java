package com.grihshobha.delhipress_grih.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import com.conscent.models.UserDetails;
import com.google.gson.Gson;

public class SessionManagement {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "AndroidHivePref";

    // All Shared Preferences Keys


    private static final String CONCENT_USER_DETAIL = "concent_user_detail";



    // Constructor
    @SuppressLint("CommitPrefEdits")
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }


    public void removeUserSession(){
        editor.putString(CONCENT_USER_DETAIL, null);
        editor.commit();
    }

    public void saveConcentUserDetail(UserDetails userDetail) {

        Gson gson = new Gson();
        String json = gson.toJson(userDetail);
        editor.putString(CONCENT_USER_DETAIL, json);
        editor.commit();
    }

    public UserDetails getConcentUserDetail() {
        if (pref != null) {
            Gson gson = new Gson();
            String json = pref.getString(CONCENT_USER_DETAIL, null);
            return gson.fromJson(json, UserDetails.class);
        }
        return null;
    }


}
