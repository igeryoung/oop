package com.example.mainpage;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static android.os.Build.VERSION_CODES.KITKAT;

public class TripSetGetData {
    private TripDB dbService;

    public TripSetGetData(Context context) throws IOException {
        dbService = new TripDB(context);
    }

    public int insert(TripSet trip) {
        SQLiteDatabase db = dbService.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TripSet.KEY_TI, trip.getTitle());
        values.put(TripSet.KEY_SD, trip.getStart_date());
        values.put(TripSet.KEY_ED, trip.getEnd_date());
        values.put(TripSet.KEY_PR, trip.getPrice());
        values.put(TripSet.KEY_P_min, trip.getPeople_min());
        values.put(TripSet.KEY_P_max, trip.getPeople_max());
        values.put(TripSet.KEY_TC, trip.getTravel_code());
        values.put(TripSet.KEY_TCN, trip.getTravel_code_name());

        // Inserting Row
        db.insert(TripSet.DATABASE_TABLE, null, values);
        db.close(); // Closing database connection
        return 1;
    }

    public ArrayList<TripSet> getAll(){
        SQLiteDatabase db = dbService.getReadableDatabase();
        String selectQuery =  "SELECT " +
                TripSet.KEY_TI + "," +
                TripSet.KEY_SD + "," +
                TripSet.KEY_ED + "," +
                TripSet.KEY_PR + "," +
                TripSet.KEY_P_min + "," +
                TripSet.KEY_P_max + "," +
                TripSet.KEY_TC + "," +
                TripSet.KEY_TCN +
                " FROM " + TripSet.DATABASE_TABLE +
                " LIMIT 5";

        ArrayList<TripSet> TripSetList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TripSet checkpoint = new TripSet(cursor.getString(cursor.getColumnIndex(TripSet.KEY_TI)),
                                    cursor.getString(cursor.getColumnIndex(TripSet.KEY_SD)),
                                    cursor.getString(cursor.getColumnIndex(TripSet.KEY_ED)),
                                    cursor.getInt(cursor.getColumnIndex(TripSet.KEY_PR)),
                                    cursor.getInt(cursor.getColumnIndex(TripSet.KEY_P_min)),
                                    cursor.getInt(cursor.getColumnIndex(TripSet.KEY_P_max)),
                                    cursor.getInt(cursor.getColumnIndex(TripSet.KEY_TC)),
                                    cursor.getString((cursor.getColumnIndex(TripSet.KEY_TCN)))
                                     );
                TripSetList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return TripSetList;
    }

    public ArrayList<TripSet> searchBySubtitle(String target){
        SQLiteDatabase db = dbService.getReadableDatabase();

        ArrayList<TripSet> TripSetList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select distinct * from " + TripSet.DATABASE_TABLE + " where " + TripSet.KEY_TI +
                                    " LIKE '%?%' OR " + TripSet.KEY_TCN + " LIKE '%?%'", new String[]{target, target});

        if (cursor.moveToFirst()) {
            do {
                TripSet checkpoint = new TripSet(cursor.getString(cursor.getColumnIndex(TripSet.KEY_TI)),
                        cursor.getString(cursor.getColumnIndex(TripSet.KEY_SD)),
                        cursor.getString(cursor.getColumnIndex(TripSet.KEY_ED)),
                        cursor.getInt(cursor.getColumnIndex(TripSet.KEY_PR)),
                        cursor.getInt(cursor.getColumnIndex(TripSet.KEY_P_min)),
                        cursor.getInt(cursor.getColumnIndex(TripSet.KEY_P_max)),
                        cursor.getInt(cursor.getColumnIndex(TripSet.KEY_TC)),
                        cursor.getString((cursor.getColumnIndex(TripSet.KEY_TCN)))
                );
                TripSetList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return TripSetList;
    }

    public ArrayList<TripSet> getCertain(String title, String start_date, String end_date){
        SQLiteDatabase db = dbService.getWritableDatabase();
        ArrayList<TripSet> tripList = new ArrayList<>();

        ArrayList<TripSet> TripSetList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + TripSet.DATABASE_TABLE
                + " where title=? AND start_date=? AND end_date=?", new String[]{title, start_date, end_date});

        if(cursor.moveToFirst()){
            do{
                TripSet checkpoint = new TripSet(cursor.getString(cursor.getColumnIndex(TripSet.KEY_TI)),
                                                cursor.getString(cursor.getColumnIndex(TripSet.KEY_SD)),
                                                cursor.getString(cursor.getColumnIndex(TripSet.KEY_ED)),
                                                cursor.getInt(cursor.getColumnIndex(TripSet.KEY_PR)),
                                                cursor.getInt(cursor.getColumnIndex(TripSet.KEY_P_min)),
                                                cursor.getInt(cursor.getColumnIndex(TripSet.KEY_P_max)),
                                                cursor.getInt(cursor.getColumnIndex(TripSet.KEY_TC)),
                                                cursor.getString((cursor.getColumnIndex(TripSet.KEY_TCN)))
                                                );
                TripSetList.add(checkpoint);
            }
            while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return TripSetList;
    }

    public int updateTripSet(String title, String start_date, String end_date, int amount){
        ArrayList<TripSet> list = getCertain(title, start_date, end_date);
        if(list.size() != 1){
            return -1;
        }
        else{
            SQLiteDatabase db = dbService.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("people_max", String.valueOf(list.get(0).getPeople_max() + amount));
            values.put("people_min", String.valueOf(list.get(0).getPeople_min() + amount));
            db.update(TripSet.DATABASE_TABLE, values, "title=? AND start_date=? AND end_date=?", new String[]{title, start_date, end_date});
            db.close();
            return 1;
        }
    }

    public int updateTripSetTravelName(int travel_code, String travel_code_name, TripDB dbService){
        SQLiteDatabase db = dbService.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("travel_code_name", travel_code_name);
        db.update(TripSet.DATABASE_TABLE, values, "travel_code=?", new String[]{String.valueOf(travel_code)});
        db.close();
        return 1;
    }
}
