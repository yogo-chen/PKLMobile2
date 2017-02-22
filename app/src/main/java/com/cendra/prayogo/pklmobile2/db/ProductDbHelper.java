package com.cendra.prayogo.pklmobile2.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Admin on 2/18/2017.
 */

public class ProductDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "pklproduct";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_BASE_PRICE = "base_price";
    public static final String COLUMN_NAME_SELL_PRICE = "sell_price";
    public static final String COLUMN_NAME_PKL_EMAIL = "pkl_email";
    private static final String DATABASE_NAME = "pklproduct.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_NAME + " TEXT," +
                    COLUMN_NAME_BASE_PRICE + " INTEGER," +
                    COLUMN_NAME_SELL_PRICE + " INTEGER," +
                    COLUMN_NAME_PKL_EMAIL + " TEXT," +
                    "FOREIGN KEY(" + COLUMN_NAME_PKL_EMAIL +
                    ") REFERENCES " + PklDbHelper.TABLE_NAME +
                    "(" + PklDbHelper.COLUMN_NAME_EMAIL + "))";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public ProductDbHelper(Context context) {
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

    public void insertProduct(@NonNull String name, int basePrice, int sellPrice, @NonNull String pklEmail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, name);
        values.put(COLUMN_NAME_BASE_PRICE, basePrice);
        values.put(COLUMN_NAME_SELL_PRICE, sellPrice);
        values.put(COLUMN_NAME_PKL_EMAIL, pklEmail);
        db.insert(TABLE_NAME, null, values);
    }

    public ContentValues getProduct(@NonNull int id) {
        SQLiteDatabase db = getReadableDatabase();
        String[] productIdConstraint = {id + ""};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_ID + " = ?", productIdConstraint);
        ContentValues result = null;
        if (cursor.moveToNext()) {
            result = new ContentValues();
            result.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
            result.put(COLUMN_NAME_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)));
            result.put(COLUMN_NAME_BASE_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_BASE_PRICE)));
            result.put(COLUMN_NAME_SELL_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SELL_PRICE)));
            result.put(COLUMN_NAME_PKL_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PKL_EMAIL)));
        }
        cursor.close();
        return result;
    }

    public List<ContentValues> getAllProduct() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<ContentValues> rows = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContentValues row = new ContentValues();
            row.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
            row.put(COLUMN_NAME_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)));
            row.put(COLUMN_NAME_BASE_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_BASE_PRICE)));
            row.put(COLUMN_NAME_SELL_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SELL_PRICE)));
            row.put(COLUMN_NAME_PKL_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PKL_EMAIL)));
            rows.add(row);
        }
        cursor.close();
        return rows;
    }

    public List<ContentValues> getAllProduct(@NonNull String pklEmail) {
        SQLiteDatabase db = getReadableDatabase();
        String[] pklEmailConstraint = {pklEmail};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_PKL_EMAIL + " = ?", pklEmailConstraint);
        List<ContentValues> rows = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContentValues row = new ContentValues();
            row.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
            row.put(COLUMN_NAME_NAME, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_NAME)));
            row.put(COLUMN_NAME_BASE_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_BASE_PRICE)));
            row.put(COLUMN_NAME_SELL_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SELL_PRICE)));
            row.put(COLUMN_NAME_PKL_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PKL_EMAIL)));
            rows.add(row);
        }
        cursor.close();
        return rows;
    }

    public void updateProduct(int _id, @NonNull String name, int basePrice, int sellPrice) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_NAME, name);
        values.put(COLUMN_NAME_BASE_PRICE, basePrice);
        values.put(COLUMN_NAME_SELL_PRICE, sellPrice);
        String selection = COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {_id + ""};
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }
}
