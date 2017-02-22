package com.cendra.prayogo.pklmobile2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

/**
 * Created by Prayogo Cendra on 2/18/2017.
 */

public class PklDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "pkl";
    public static final String COLUMN_NAME_EMAIL = "email";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_ADDRESS = "address";
    public static final String COLUMN_NAME_PHONE = "phone";
    public static final String COLUMN_NAME_BIRTHDAY = "birthday";
    private static final String DATABASE_NAME = "pkl.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_EMAIL + " TEXT PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_ADDRESS + " TEXT," +
                    COLUMN_NAME_PHONE + " TEXT," +
                    COLUMN_NAME_BIRTHDAY + " TEXT)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public PklDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public boolean insertPkl(@NonNull String email, @NonNull String name, @NonNull String address, @NonNull String phone, @NonNull String birthday) {
        Cursor cursor = getPklCursor(email);
        if (cursor.moveToNext()) {
            cursor.close();
            return false;
        } else {
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_NAME_EMAIL, email);
            values.put(COLUMN_NAME_NAME, name);
            values.put(COLUMN_NAME_ADDRESS, address);
            values.put(COLUMN_NAME_PHONE, phone);
            values.put(COLUMN_NAME_BIRTHDAY, birthday);
            db.insert(TABLE_NAME, null, values);
            cursor.close();
            return true;
        }
    }

    public ContentValues getPkl(@NonNull String email) {
        Cursor cursor = getPklCursor(email);
        ContentValues row = null;
        if (cursor.moveToNext()) {
            row = new ContentValues();
            row.put(COLUMN_NAME_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EMAIL)));
            row.put(COLUMN_NAME_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)));
            row.put(COLUMN_NAME_ADDRESS, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ADDRESS)));
            row.put(COLUMN_NAME_PHONE, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONE)));
            row.put(COLUMN_NAME_BIRTHDAY, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_BIRTHDAY)));
        }
        cursor.close();
        return row;
    }

    public ContentValues getPkl(@NonNull String email, @NonNull String birthday) {
        SQLiteDatabase db = getReadableDatabase();
        String[] constraint = {email, birthday};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_EMAIL + " = ? AND " + COLUMN_NAME_BIRTHDAY + " = ?", constraint);
        ContentValues row = null;
        if (cursor.moveToNext()) {
            row = new ContentValues();
            row.put(COLUMN_NAME_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_EMAIL)));
            row.put(COLUMN_NAME_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)));
            row.put(COLUMN_NAME_ADDRESS, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_ADDRESS)));
            row.put(COLUMN_NAME_PHONE, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PHONE)));
            row.put(COLUMN_NAME_BIRTHDAY, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_BIRTHDAY)));
        }
        cursor.close();
        return row;
    }

    private Cursor getPklCursor(String email) {
        SQLiteDatabase db = getReadableDatabase();
        String[] emailConstraint = {email};
        return db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_EMAIL + " = ?", emailConstraint);
    }
}
