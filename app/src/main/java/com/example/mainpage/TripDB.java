package com.example.mainpage;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TripDB extends SQLiteOpenHelper {
    private final static int _DBVersion = 16; //<-- version
    private final static String _DBName = "tripSet.db";  //<-- db name
    private Context context;

    public TripDB(Context context) {
        super(context, _DBName, null, _DBVersion);
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL =  "CREATE TABLE IF NOT EXISTS " + TripSet.DATABASE_TABLE + "( "
                            + TripSet.KEY_TI + " TEXT, "
                            + TripSet.KEY_SD + " TEXT, "
                            + TripSet.KEY_ED + " TEXT, "
                            + TripSet.KEY_PR + " INTEGER, "
                            + TripSet.KEY_P_max + " INTEGER, "
                            + TripSet.KEY_P_min + " INTEGER, "
                            + TripSet.KEY_TC + " INTEGER, "
                            + TripSet.KEY_TCN + " TEXT, "
                            + TripSet.KEY_OP + " INTEGER) ";
        db.execSQL(SQL);
        //new readFile().execute(this);
        try {
            AssetManager am = context.getAssets();
            InputStream is_csv = am.open("trip_data_all.csv");
            BufferedReader reader_csv = new BufferedReader(new InputStreamReader(is_csv));

            String csvLine;
            while ((csvLine = reader_csv.readLine()) != null) {
                String[] row = csvLine.split(",");
                System.out.println(csvLine);
                System.out.println(row[3] + " " + row[1]);

                ContentValues values = new ContentValues();
                values.put(TripSet.KEY_TI, row[0]);
                values.put(TripSet.KEY_SD, row[4]);
                values.put(TripSet.KEY_ED, row[5]);
                values.put(TripSet.KEY_PR, Integer.parseInt(row[3]));
                values.put(TripSet.KEY_P_min, Integer.parseInt(row[6]));
                values.put(TripSet.KEY_P_max, Integer.parseInt(row[7]));
                values.put(TripSet.KEY_TC, Integer.parseInt(row[1]));
                values.put(TripSet.KEY_TCN, "");
                values.put(TripSet.KEY_OP, 0);

                // Inserting Row
                db.insert(TripSet.DATABASE_TABLE, null, values);
                //TripSetGetData.insert(new TripSet(row[0], row[4], row[5], Integer.parseInt(row[3]), Integer.parseInt(row[6]), Integer.parseInt(row[7]), Integer.parseInt(row[1]), ""), this);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading CSV file: " + ex);
        }

        StringBuilder stringBuilder = new StringBuilder();

        try {
            AssetManager am = context.getAssets();
            InputStream is_json = am.open("travel_code.json");
            BufferedReader reader_json = new BufferedReader(new InputStreamReader(is_json));

            String line;
            while ((line = reader_json.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Error in reading JSON file: " + ex);
        }

        try {
            JSONArray array = new JSONArray(stringBuilder.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                int travel_code = jsonObject.getInt("travel_code");
                String travel_code_name = jsonObject.getString("travel_code_name");

                System.out.println(travel_code + " " + travel_code_name);

                ContentValues values = new ContentValues();
                values.put("travel_code_name", travel_code_name);
                db.update(TripSet.DATABASE_TABLE, values, "travel_code=?", new String[]{String.valueOf(travel_code)});
                //TripSetGetData.updateTripSetTravelName(travel_code, travel_code_name, this);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed, all data will be gone
        db.execSQL("DROP TABLE IF EXISTS " + TripSet.DATABASE_TABLE);
        // Create tables again
        onCreate(db);
    }
}
