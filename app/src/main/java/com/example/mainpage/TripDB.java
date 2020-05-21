package com.example.mainpage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TripDB extends SQLiteOpenHelper {
    private final static int _DBVersion = 1; //<-- version
    private final static String _DBName = "tripSet.db";  //<-- db name

    public TripDB(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL =  "CREATE TABLE IF NOT EXISTS " + TripSet.DATABASE_TABLE + "( "
                            + TripSet.KEY_TI + " TEXT, "
                            + TripSet.KEY_SD + " TEXT, "
                            + TripSet.KEY_ED + " TEXT, "
                            + TripSet.KEY_PR + " INTEGER, "
                            + TripSet.KEY_P_max + "INTEGER, "
                            + TripSet.KEY_P_min + " INTEGER, "
                            + TripSet.KEY_TC + "INTEGER, "
                            + TripSet.KEY_TCN + "TEXT )";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + TripSet.DATABASE_TABLE);
        // Create tables again
        onCreate(db);
    }
}
