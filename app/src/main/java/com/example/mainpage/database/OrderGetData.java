package com.example.mainpage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.mainpage.model.CostumerOrder;

import java.util.ArrayList;

/** Using SQLite as order's database, and create some api*/
public class OrderGetData {
    private OrderDB dbService;
    public OrderGetData(Context context) {
        dbService = new OrderDB(context);
    }

    /** insert CustumerOrder order into order table and return order_Id, which is primary key autoincrement*/
    public int insert(CostumerOrder order) {
        SQLiteDatabase db = dbService.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CostumerOrder.KEY_CI, order.getCostumerId());
        values.put(CostumerOrder.KEY_AD, order.getAdult());
        values.put(CostumerOrder.KEY_CH, order.getChild());
        values.put(CostumerOrder.KEY_BA, order.getBaby());
        values.put(CostumerOrder.KEY_PR, order.getPrice());
        values.put(CostumerOrder.KEY_TI, order.getTitle());
        values.put(CostumerOrder.KEY_SD, order.getStart_date());
        values.put(CostumerOrder.KEY_ED, order.getEnd_date());

        // Inserting Row
        long Order_Id = db.insert(CostumerOrder.DATABASE_TABLE, null, values);
        db.close(); // Closing database connection
        return (int) Order_Id;
    }

    /** Get all Costumer order and return as ArrayList*/
    public ArrayList<CostumerOrder> getAll(){
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
                                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_PR)),
                                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_TI)),
                                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_SD)),
                                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_ED)));
                CostumerOrderList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return CostumerOrderList;
    }

    /** Get CostumerOrder with specific costumerId and return as ArrayList*/
    public ArrayList<CostumerOrder> getOrderByCI(int costumerId){
        SQLiteDatabase db = dbService.getReadableDatabase();

        ArrayList<CostumerOrder> CostumerOrderList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + CostumerOrder.DATABASE_TABLE + " where costumerId=?", new String[]{String.valueOf(costumerId)});

        if (cursor.moveToFirst()) {
            do {
                CostumerOrder checkpoint = new CostumerOrder(cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CI)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_OI)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_AD)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CH)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_BA)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_PR)),
                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_TI)),
                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_SD)),
                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_ED))
                                            );
                CostumerOrderList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return CostumerOrderList;
    }

    /** Get CostumerOrder with specific orderId and return as ArrayList*/
    public ArrayList<CostumerOrder> getOrderByOI(int orderId){
        SQLiteDatabase db = dbService.getReadableDatabase();

        ArrayList<CostumerOrder> CostumerOrderList = new ArrayList<>();
        Cursor cursor = db.rawQuery("select * from " + CostumerOrder.DATABASE_TABLE + " where orderId=?", new String[]{String.valueOf(orderId)});

        if (cursor.moveToFirst()) {
            do {
                CostumerOrder checkpoint = new CostumerOrder(cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CI)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_OI)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_AD)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_CH)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_BA)),
                                            cursor.getInt(cursor.getColumnIndex(CostumerOrder.KEY_PR)),
                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_TI)),
                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_SD)),
                                            cursor.getString(cursor.getColumnIndex(CostumerOrder.KEY_ED))
                                            );
                CostumerOrderList.add(checkpoint);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return CostumerOrderList;
    }

    /** Delete CostumerOrder with specific costumerId and orderId, return 1 if success, otherwise -1*/
    public int cancelOrder(int costumerId, int orderId){
        SQLiteDatabase db = dbService.getWritableDatabase();
        int ret = db.delete(CostumerOrder.DATABASE_TABLE, "costumerId=? AND orderId=?", new String[]{String.valueOf(costumerId), String.valueOf(orderId)});
        db.close();
        return ret;
    }

    /** Modify CostumerOrder with specific costumerId and orderId, set the person amount according to parameter adult, child and baby*/
    public int modifyOrderPeople(int costumerId, int orderId, int adult, int child, int baby){
        ArrayList<CostumerOrder> order = getOrderByOI(orderId);
        if(order.size() != 1) {
            return -1;
        }
        else {
            SQLiteDatabase db = dbService.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("adult", String.valueOf(order.get(0).getAdult() + adult));
            values.put("child", String.valueOf(order.get(0).getChild() + child));
            values.put("baby", String.valueOf(order.get(0).getBaby() + baby));
            db.update(CostumerOrder.DATABASE_TABLE, values, "costumerId=? AND orderId=?", new String[]{String.valueOf(costumerId), String.valueOf(orderId)});
            db.close();
            return 1;
        }
    }
}
