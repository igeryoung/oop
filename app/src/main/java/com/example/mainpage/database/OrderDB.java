package com.example.mainpage.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mainpage.model.CostumerOrder;

public class OrderDB extends SQLiteOpenHelper {
    private final static int _DBVersion = 1; //<-- version
    private final static String _DBName = "order.db";  //<-- db name

    public OrderDB(Context context) {
        super(context, _DBName, null, _DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL =  "CREATE TABLE IF NOT EXISTS " + CostumerOrder.DATABASE_TABLE + "( "
                            + CostumerOrder.KEY_OI + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                            + CostumerOrder.KEY_CI + " INTEGER, "
                            + CostumerOrder.KEY_AD + " INTEGER, "
                            + CostumerOrder.KEY_CH + " INTEGER, "
                            + CostumerOrder.KEY_BA + " INTEGER, "
                            + CostumerOrder.KEY_PR + " INTEGER, "
                            + CostumerOrder.KEY_TI + " TEXT, "
                            + CostumerOrder.KEY_SD + " TEXT, "
                            + CostumerOrder.KEY_ED + " TEXT )";
        db.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + CostumerOrder.DATABASE_TABLE);
        // Create tables again
        onCreate(db);
    }

}
