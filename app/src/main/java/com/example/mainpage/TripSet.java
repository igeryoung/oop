package com.example.mainpage;

import android.telephony.mbms.StreamingServiceInfo;

public class TripSet {
    public  static final String DATABASE_TABLE = "TRIP_SET_TABLE";

    public static final String KEY_TI = "title";
    public static final String KEY_SD = "start_date";
    public static final String KEY_ED = "end_date";
    public static final String KEY_PR = "price";
    public static final String KEY_P_min = "people_min";
    public static final String KEY_P_max = "people_max";
    public static final String KEY_TC = "travel_code";
    public static final String KEY_TCN = "travel_code_name";
    public static final String KEY_OP = "order_amount";

    private String title;
    private String start_date;
    private String end_date;
    private int price;
    private int people_min;
    private int people_max;
    private int travel_code;
    private String travel_code_name;
    private int order_amount = 0;

    public TripSet(String title, String start_date, String end_date, int price, int people_min,
                   int people_max, int travel_code, String travel_code_name, int order_amount) {
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
        this.price = price;
        this.people_min = people_min;
        this.people_max = people_max;
        this.travel_code = travel_code;
        this.travel_code_name = travel_code_name;
       this.order_amount = order_amount;
    }

    public TripSet (String info) {
        String[] tokens = info.split(",");
        this.title = tokens[0];
        this.start_date = tokens[1];
        this.end_date = tokens[2];
        this.price = Integer.parseInt(tokens[3]);
        this.people_min = Integer.parseInt(tokens[4]);
        this.people_max = Integer.parseInt(tokens[5]);
        this.order_amount = Integer.parseInt(tokens[6]);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPeople_min() {
        return people_min;
    }

    public void setPeople_min(int people_min) {
        this.people_min = people_min;
    }

    public int getPeople_max() {
        return people_max;
    }

    public void setPeople_max(int people_max) {
        this.people_max = people_max;
    }

    public int getTravel_code() { return this.travel_code; }

    public void setTravel_code(int travel_code) { this.travel_code = travel_code; }

    public String getTravel_code_name() { return this.travel_code_name; }

    public void setTravel_code_name(String travel_code_name) { this.travel_code_name = travel_code_name; }

    public int getOrder_amount() {
        return order_amount;
    }

    public void setOrder_amount(int order_amount) {
        this.order_amount = order_amount;
    }

    public String allToString(){
        String info = title;
        info += "," + start_date;
        info += "," + end_date;
        info += "," + price;
        info += "," + people_min;
        info += "," + people_max;
        info += "," + order_amount;
        return info;
    }

}
