package com.cendra.prayogo.pklmobile2.service;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;

import com.cendra.prayogo.pklmobile2.db.PklDbHelper;

/**
 * Created by Prayogo Cendra on 2/18/2017.
 */

public class PklAccountManager {
    public static final String IS_LOGGED_IN = "IS_LOGGED_IN";
    public static final String LOGGED_IN_EMAIL = "LOGGED_IN_EMAIL";
    public static final String LOGGED_IN_NAME = "LOGGED_IN_NAME";
    public static final String LOGGED_IN_PHONE = "LOGGED_IN_PHONE";
    public static final String LOGGED_IN_ADDRESS = "LOGGED_IN_ADDRESS";
    public static final String LOGGED_IN_BIRTHDAY = "LOGGED_IN_BIRTHDAY";
    private static final String PREFS_NAME = "AccountPreferences";

    private PklAccountManager() {
    }

    public static boolean login(Context context, String email, String birthday) {
        PklDbHelper pklDbHelper = getPklDbHelper(context);
        ContentValues pkl = pklDbHelper.getPkl(email, birthday);
        if (pkl != null) {
            SharedPreferences sharedPreferences = getSharedPreferences(context);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(IS_LOGGED_IN, true);
            editor.putString(LOGGED_IN_EMAIL, pkl.getAsString(PklDbHelper.COLUMN_NAME_EMAIL));
            editor.putString(LOGGED_IN_NAME, pkl.getAsString(PklDbHelper.COLUMN_NAME_NAME));
            editor.putString(LOGGED_IN_PHONE, pkl.getAsString(PklDbHelper.COLUMN_NAME_PHONE));
            editor.putString(LOGGED_IN_ADDRESS, pkl.getAsString(PklDbHelper.COLUMN_NAME_ADDRESS));
            editor.putString(LOGGED_IN_BIRTHDAY, pkl.getAsString(PklDbHelper.COLUMN_NAME_BIRTHDAY));
            editor.apply();
            return true;
        } else {
            return false;
        }
    }

    public static void logout(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static boolean register(Context context, String email, String name, String address, String phone, String birthday) {
        PklDbHelper pklDbHelper = getPklDbHelper(context);
        return pklDbHelper.insertPkl(email, name, address, phone, birthday);
    }

    public static ContentValues getLoggedIn(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(context);
        boolean isLoggedIn = sharedPreferences.getBoolean(IS_LOGGED_IN, false);
        if (isLoggedIn) {
            ContentValues values = new ContentValues();
            values.put(LOGGED_IN_EMAIL, sharedPreferences.getString(LOGGED_IN_EMAIL, ""));
            values.put(LOGGED_IN_NAME, sharedPreferences.getString(LOGGED_IN_NAME, ""));
            values.put(LOGGED_IN_PHONE, sharedPreferences.getString(LOGGED_IN_PHONE, ""));
            values.put(LOGGED_IN_ADDRESS, sharedPreferences.getString(LOGGED_IN_ADDRESS, ""));
            values.put(LOGGED_IN_BIRTHDAY, sharedPreferences.getString(LOGGED_IN_BIRTHDAY, ""));
            return values;
        } else {
            return null;
        }
    }

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    private static PklDbHelper getPklDbHelper(Context context) {
        return new PklDbHelper(context);
    }
}
