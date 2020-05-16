package com.example.mainpage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class OrderGetData {
    private OrderDB dbService;
    public OrderGetData(Context context) {
        dbService = new OrderDB(context);
    }

    public int insert(CostumerOrder order) {

        SQLiteDatabase db = dbService.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CostumerOrder.KEY_OI, order.getOrderId());
        values.put(CostumerOrder.KEY_CI, order.getCostumerId());
        values.put(CostumerOrder.KEY_AD, order.getAdult());
        values.put(CostumerOrder.KEY_CH, order.getChild());
        values.put(CostumerOrder.KEY_BA, order.getBaby());
        values.put(CostumerOrder.KEY_PR, order.getPrice());

        // Inserting Row
        long Order_Id = db.insert(CostumerOrder.DATABASE_TABLE, null, values);
        db.close(); // Closing database connection
        return (int) Order_Id;
    }

    public ArrayList<CostumerOrder> getAll() {
        SQLiteDatabase db = dbService.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                CostumerOrder.KEY_OI + "," +
                CostumerOrder.KEY_CI + "," +
                CostumerOrder.KEY_AD + "," +
                CostumerOrder.KEY_CH + "," +
                CostumerOrder.KEY_BA + "," +
                CostumerOrder.KEY_PR +
                " FROM " + CostumerOrder.DATABASE_TABLE;

        ArrayList<CostumerOrder> CostumerOrderList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CostumerOrder checkpoint = new CostumerOrder(cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CI)),
                                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_OI)),
                                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_AD)),
                                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CH)),
                                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_BA)),
                                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_PR))
                                                            );
                CostumerOrderList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return CostumerOrderList;
    }

    public ArrayList<CostumerOrder> getCertain(int costumerId){
        SQLiteDatabase db = dbService.getReadableDatabase();
        String selectQuery =  "SELECT  " +
                CostumerOrder.KEY_OI + "," +
                CostumerOrder.KEY_CI + "," +
                CostumerOrder.KEY_AD + "," +
                CostumerOrder.KEY_CH + "," +
                CostumerOrder.KEY_BA + "," +
                CostumerOrder.KEY_PR +
                " FROM " + CostumerOrder.DATABASE_TABLE +
                " WHERE " + "costumerId=?";

        ArrayList<CostumerOrder> CostumerOrderList = new ArrayList<>();
        Cursor cursor = db.rawQuery(selectQuery, String.valueOf(costumerId));

        if (cursor.moveToFirst()) {
            do {
                CostumerOrder checkpoint = new CostumerOrder(cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CI)),
                        cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_OI)),
                        cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_AD)),
                        cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CH)),
                        cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_BA)),
                        cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_PR))
                );
                CostumerOrderList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return CostumerOrderList;
    }
}
