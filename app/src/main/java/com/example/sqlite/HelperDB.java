package com.example.sqlite;

import static com.example.sqlite.Students.*;
import static com.example.sqlite.Grades.*;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "database.db";
    private static final int DATABASE_VERSION = 1;
    String strCreate, strDelete;

    /**
     *  Constructor
     */
    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate = "CREATE TABLE " + TABLE_STUDENTS + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + Students.NAME + " TEXT, " + ADDRESS + " TEXT, " + PHONE + " TEXT" + ");";

        db.execSQL(strCreate);

        strCreate = "CREATE TABLE " + TABLE_GRADES + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + Grades.NAME + " TEXT, " + SUBJECT + " TEXT, " + ASSIGNMENT_TYPE + " TEXT, " + QUARTER + " TEXT, " + GRADE + " INTEGER" + ");";

        db.execSQL(strCreate);
    }

    /**
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        strDelete = "DROP TABLE IF EXISTS " + TABLE_STUDENTS;
        db.execSQL(strDelete);
        strDelete = "DROP TABLE IF EXISTS " + TABLE_GRADES;
        db.execSQL(strDelete);

        onCreate(db);
    }
}
