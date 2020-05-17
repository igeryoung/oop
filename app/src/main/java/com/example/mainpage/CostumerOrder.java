package com.example.mainpage;

public class CostumerOrder {
    public  static final String DATABASE_TABLE = "ORDER_TABLE";

    public static final String KEY_OI = "orderId";
    public static final String KEY_CI = "costumerId";
    public static final String KEY_AD = "adult";
    public static final String KEY_CH = "child";
    public static final String KEY_BA = "baby";
    public static final String KEY_PR = "price";
    public static final String KEY_TI = "title";
    public static final String KEY_SD = "start_date";
    public static final String KEY_ED = "end_date";

    private int costumerId;
    private int orderId;
    private int adult;
    private int child;
    private int baby;
    private int price;
    private String title;
    private String start_date;
    private String end_date;

    public CostumerOrder(int costumerId, int orderId, int adult, int child, int baby, int price) {
        this.costumerId = costumerId;
        this.orderId = orderId;
        this.adult = adult;
        this.child = child;
        this.baby = baby;
        this.price = price;
    }

    public CostumerOrder(int costumerId, int orderId, int adult, int child, int baby, int price, String title, String start_date, String end_date) {
        this.costumerId = costumerId;
        this.orderId = orderId;
        this.adult = adult;
        this.child = child;
        this.baby = baby;
        this.price = price;
        this.title = title;
        this.start_date = start_date;
        this.end_date = end_date;
    }

    public int getCostumerId() {
        return costumerId;
    }

    public void setCostumerId(int costumerId) {
        this.costumerId = costumerId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAdult() {
        return adult;
    }

    public void setAdult(int adult) {
        this.adult = adult;
    }

    public int getChild() {
        return child;
    }

    public void setChild(int child) {
        this.child = child;
    }

    public int getBaby() {
        return baby;
    }

    public void setBaby(int baby) {
        this.baby = baby;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getTitle(){ return this.title; }

    public void setTitle(String title){ this.title = title; }

    public String getStart_date(){ return this.start_date; }

    public void setStart_date(String start_date){ this.start_date = start_date; }

    public String getEnd_date(){ return this.end_date; }

    public void setEnd_date(String end_date){ this.end_date = end_date; }

}
