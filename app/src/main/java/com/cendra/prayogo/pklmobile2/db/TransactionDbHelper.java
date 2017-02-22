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

public class TransactionDbHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "pkltransaction";
    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_PRODUCT_ID = "product_id";
    public static final String COLUMN_NAME_SOLD_PRICE = "sold_price";
    public static final String COLUMN_NAME_QUANTITY = "quantity";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_PKL_EMAIL = "pkl_email";
    private static final String DATABASE_NAME = "pkltransaction.db";
    private static final int DATABASE_VERSION = 1;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_NAME_PRODUCT_ID + " INTEGER," +
                    COLUMN_NAME_SOLD_PRICE + " INTEGER," +
                    COLUMN_NAME_QUANTITY + " INTEGER," +
                    COLUMN_NAME_DATE + " TEXT," +
                    COLUMN_NAME_PKL_EMAIL + " TEXT," +
                    "FOREIGN KEY(" + COLUMN_NAME_PRODUCT_ID +
                    ") REFERENCES " + ProductDbHelper.TABLE_NAME +
                    "(" + ProductDbHelper.COLUMN_NAME_ID + ")," +
                    "FOREIGN KEY(" + COLUMN_NAME_PKL_EMAIL +
                    ") REFERENCES " + PklDbHelper.TABLE_NAME +
                    "(" + PklDbHelper.COLUMN_NAME_EMAIL + "))";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public TransactionDbHelper(Context context) {
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

    public void insertTransaction(int productId, int soldPrice, int quantity, @NonNull String date, @NonNull String pklEmail) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PRODUCT_ID, productId);
        values.put(COLUMN_NAME_SOLD_PRICE, soldPrice);
        values.put(COLUMN_NAME_QUANTITY, quantity);
        values.put(COLUMN_NAME_DATE, date);
        values.put(COLUMN_NAME_PKL_EMAIL, pklEmail);
        db.insert(TABLE_NAME, null, values);
    }

    public List<ContentValues> getAllTransaction() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<ContentValues> rows = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContentValues row = new ContentValues();
            row.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
            row.put(COLUMN_NAME_PRODUCT_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PRODUCT_ID)));
            row.put(COLUMN_NAME_SOLD_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SOLD_PRICE)));
            row.put(COLUMN_NAME_QUANTITY, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_QUANTITY)));
            row.put(COLUMN_NAME_DATE, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
            row.put(COLUMN_NAME_PKL_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PKL_EMAIL)));
            rows.add(row);
        }
        cursor.close();
        return rows;
    }

    public List<ContentValues> getAllTransaction(@NonNull String pklEmail) {
        SQLiteDatabase db = getReadableDatabase();
        String[] pklEmailConstraint = {pklEmail};
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_PKL_EMAIL + " = ?", pklEmailConstraint);
        List<ContentValues> rows = new ArrayList<>();
        while (cursor.moveToNext()) {
            ContentValues row = new ContentValues();
            row.put(COLUMN_NAME_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_ID)));
            row.put(COLUMN_NAME_PRODUCT_ID, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_PRODUCT_ID)));
            row.put(COLUMN_NAME_SOLD_PRICE, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_SOLD_PRICE)));
            row.put(COLUMN_NAME_QUANTITY, cursor.getInt(cursor.getColumnIndex(COLUMN_NAME_QUANTITY)));
            row.put(COLUMN_NAME_DATE, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_DATE)));
            row.put(COLUMN_NAME_PKL_EMAIL, cursor.getString(cursor.getColumnIndex(COLUMN_NAME_PKL_EMAIL)));
            rows.add(row);
        }
        cursor.close();
        return rows;
    }

    public void updateTransaction(int _id, int productId, int soldPrice, int quantity, @NonNull String date) {
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_PRODUCT_ID, productId);
        values.put(COLUMN_NAME_SOLD_PRICE, soldPrice);
        values.put(COLUMN_NAME_QUANTITY, quantity);
        values.put(COLUMN_NAME_DATE, date);
        String selection = COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {_id + ""};
        db.update(TABLE_NAME, values, selection, selectionArgs);
    }
}
