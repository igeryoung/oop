package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainpage.database.OrderGetData;
import com.example.mainpage.model.CostumerOrder;
import com.example.mainpage.model.TripSet;

import java.io.IOException;

public class select_Activity extends AppCompatActivity {
    private TripSet tripset;
    private int[] order = {0,0,0};
    private int CID;
    private OrderGetData OrderDB;

    //initial database
    public select_Activity() throws IOException {
        new Thread(){
            public void run(){
                OrderDB = new OrderGetData((select_Activity.this));
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_);

        //get msg from last page
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        System.out.println(info);
        tripset = new TripSet(info);

        //show info
        showInfo();
    }

    //show the info of Order
    public void showInfo(){
        TextView title = findViewById(R.id.title);
        title.setText(tripset.getTitle());
        TextView date = findViewById(R.id.date);
        date.setText("from : " + tripset.getStart_date() + " to : " + tripset.getEnd_date());
        TextView people = findViewById(R.id.people);
        people.setText("min : " + tripset.getPeople_min() + " max : " + tripset.getPeople_max());
        TextView price = findViewById(R.id.price);
        price.setText("price : " + tripset.getPrice());
        TextView order_amount = findViewById(R.id.oder_amount);
        order_amount.setText("" + tripset.getOrder_amount());
    }

    //onClick event of button certain : check the order is valid or not . If not, throw to exception to handle
    public void certain(View view) {
        //get number from order blank
        EditText text_old = findViewById(R.id.input_old);
        String input_old = text_old.getText().toString();
        EditText text_adult = findViewById(R.id.input_adult);
        String input_adult = text_adult.getText().toString();
        EditText text_baby = findViewById(R.id.input_baby);
        String input_baby = text_baby.getText().toString();
        EditText text_id = findViewById(R.id.input_id);

        //check id is provided or not
        if(text_id.getText().length() == 0){
            Toast.makeText(select_Activity.this, "please enter costumer id", Toast.LENGTH_SHORT).show();
            return;
        }

        //check id is integer or not
        if(isInteger(text_id.getText().toString()) == false){
            Toast.makeText(select_Activity.this, "please enter a number as costumer id", Toast.LENGTH_SHORT).show();
            return;
        }

        //parse ID to int and check is positive or not
        CID = Integer.parseInt(text_id.getText().toString());
        if(CID <= 0){
            Toast.makeText(select_Activity.this, "please enter a number larger than 0 as costumer id", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //check input is integer or not
            if(isInteger(input_old) == false || isInteger(input_adult) == false || isInteger(input_baby) == false ) {
                throw new Exception();
            }

            //parse String to int
            order[0] = Integer.parseInt(input_old);
            order[1] = Integer.parseInt(input_adult);
            order[2] = Integer.parseInt(input_baby);

            // if number is negative , reject request
            if(order[0] < 0 || order[1] < 0 || order[2] < 0){
                Toast.makeText(select_Activity.this, "please enter non-negative number in order", Toast.LENGTH_SHORT).show();
                return;
            }

            int total = order[0] + order[1] + order[2];
            int price_total = tripset.getPrice() * total;

            //check order number is satisfy given range or not
            if(total + tripset.getOrder_amount() > tripset.getPeople_max() || total  + tripset.getOrder_amount() < tripset.getPeople_min()){
                Toast.makeText(select_Activity.this, "error range", Toast.LENGTH_SHORT).show();
                return;
            }else{
                //pass all exception and make an order
                CostumerOrder final_order = new CostumerOrder( CID , 0, order[0] , order[1] , order[2] , price_total,
                tripset.getTitle() , tripset.getStart_date() , tripset.getEnd_date());
                int number = OrderDB.insert(final_order);
                System.out.println("Order# = " + number);
                Cfinish();
            }

        }catch (Exception e){
            Toast.makeText(select_Activity.this, "please enter a number in order", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //onClick event of button cancel : finish current page
    public void cancel(View view) {
        finish();
    }

    // Override finish to send total number of order to menu
    public void Cfinish(){
        setResult(order[0] + order[1] + order[2]);
        super.finish();
    }

    //check whether given String is Integer or not
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

}
