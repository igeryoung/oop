package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainpage.database.OrderGetData;
import com.example.mainpage.database.TripSetGetData;
import com.example.mainpage.model.CostumerOrder;
import com.example.mainpage.model.TripSet;

import java.io.IOException;

public class order_item_Activity extends AppCompatActivity {
    private CostumerOrder order;
    private TripSetGetData TripDB;
    private OrderGetData OrderDB;
    private TripSet tripset;
    private int[] order_num = {0,0,0};
    private int old_order_num;
    private int remain;

    public order_item_Activity() throws IOException {
        new Thread(){
            public void run(){
                try {
                    TripDB = new TripSetGetData((order_item_Activity.this));
                    OrderDB = new OrderGetData((order_item_Activity.this));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_);
        //init
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        System.out.println(info);

        //get order and init view
        order = new CostumerOrder(info);
        old_order_num = order.getAdult() + order.getChild() + order.getBaby();
        tripset = TripDB.getCertain(order.getTitle() , order.getStart_date() , order.getEnd_date()).get(0);
        remain = tripset.getOrder_amount() - old_order_num;
        showInfo();
    }

    public void showInfo(){
        TextView title = findViewById(R.id.title);
        title.setText(order.getTitle());
        TextView date = findViewById(R.id.date);
        date.setText("from : " + order.getStart_date() + " to : " + order.getEnd_date());
        TextView price = findViewById(R.id.price);
        price.setText("price : " + order.getPrice());
        TextView people = findViewById(R.id.people);
        people.setText("people : " + tripset.getPeople_min() + "~" + tripset.getPeople_max());
        TextView order_amount = findViewById(R.id.oder_amount);
        order_amount.setText("" + remain);

        TextView adult_num = findViewById(R.id.adult_num);
        adult_num.setText(""+ order.getAdult());
        TextView child_num = findViewById(R.id.child_num);
        child_num.setText(""+ order.getChild());
        TextView baby_num = findViewById(R.id.baby_num);
        baby_num.setText(""+ order.getBaby());
    }

    public void certain(View view) {
        EditText text_old = findViewById(R.id.input_old);
        String input_old = text_old.getText().toString();
        EditText text_adult = findViewById(R.id.input_adult);
        String input_adult = text_adult.getText().toString();
        EditText text_baby = findViewById(R.id.input_baby);
        String input_baby = text_baby.getText().toString();

        try {
            if(isInteger(input_old) == false || isInteger(input_adult) == false || isInteger(input_baby) == false ) {
                throw new Exception();
            }
            order_num[0] = Integer.parseInt(input_old);
            order_num[1] = Integer.parseInt(input_adult);
            order_num[2] = Integer.parseInt(input_baby);
            System.out.println( "" + order_num[0] + order_num[1] + order_num[2]);
            int total = order_num[0] + order_num[1] + order_num[2];
            System.out.println( "" + tripset.getPeople_max() + "~" + tripset.getPeople_min());
            if(total + remain > tripset.getPeople_max() || total + remain < tripset.getPeople_min()){
                Toast.makeText(order_item_Activity.this, "error range", Toast.LENGTH_SHORT).show();
                return;
            }else{
                //all exception pass and update data
                System.out.println("pass and ready to update");
                TripDB.updateTripSet(order.getTitle() , order.getStart_date() , order.getEnd_date() , total - old_order_num);

                System.out.println(order.getCostumerId() + ", " + order.getOrderId() + ", " + (order_num[0] - order.getAdult()));

                int err = OrderDB.modifyOrderPeople(order.getCostumerId() , order.getOrderId() ,
                        order_num[0] - order.getAdult(),  order_num[1] - order.getChild(), order_num[2] - order.getBaby());

                finish();
            }

        }catch (Exception e){
            Toast.makeText(order_item_Activity.this, "please enter a number in order", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void cancel(View view) {
        finish();
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(NumberFormatException e) {
            return false;
        } catch(NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public void delete(View view){
        TripDB.updateTripSet(order.getTitle() , order.getStart_date() , order.getEnd_date() , old_order_num * -1);
        OrderDB.cancelOrder(order.getCostumerId() , order.getOrderId());
        finish();
    }

    @Override
    public void finish(){
        setResult(order_num[0] + order_num[1] + order_num[2]);

        //add this order to database
        super.finish();
    }
}
