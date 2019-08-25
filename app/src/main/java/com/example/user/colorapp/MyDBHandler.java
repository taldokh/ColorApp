package com.example.user.colorapp;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.text.Spannable;
import android.widget.Toast;

import java.sql.ResultSet;
import java.util.ArrayList;

public class MyDBHandler extends SQLiteOpenHelper {

    //information of database
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UnmatchedColor.db";
    public static final String TABLE_NAME = "UnmatchedColor";
    public static final String COLUMN_RESULT = "Result";
    public static final String COLUMN_WRONG_COLOR = "WrongColor";
    //initialize the database
    public MyDBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME +" (Result TEXT PRIMARY KEY,WrongColor TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
    }

    public ArrayList<UnmatchedColor> loadHandler() {
        ArrayList<UnmatchedColor> result = new ArrayList<>();
        String query = "Select * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            String result_0 = cursor.getString(0);
            String result_1 = cursor.getString(1);
            UnmatchedColor unmatchedColor = new UnmatchedColor(result_0, result_1);
            result.add(unmatchedColor);
        }
        cursor.close();
        db.close();
        return result;
    }

    public boolean addHandler(UnmatchedColor unmatchedColor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_RESULT, unmatchedColor.getResult().toString());
        values.put(COLUMN_WRONG_COLOR, unmatchedColor.getWrongColor().toString());
        long result = db.insert(TABLE_NAME, null, values);
        if (result == -1)
            return false;
        else
            return true;
        }

        public int deleteHandler(String Result){
            SQLiteDatabase db = this.getWritableDatabase();
            return db.delete(TABLE_NAME, "Result = ?", new String[] {Result});
        }
    }

