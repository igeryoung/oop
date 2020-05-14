package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class select_Activity extends AppCompatActivity {
    private TripSet tripset;
    private int[] order;
    private String order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_);
        //init
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");
        System.out.println(info);
        tripset = new TripSet(info);
        //show info
        showInfo();


    }

    public void certain(View view) {
        EditText text_old = findViewById(R.id.input_old);
        String input_old = text_old.getText().toString();
        EditText text_adult = findViewById(R.id.input_adult);
        String input_adult = text_adult.getText().toString();
        EditText text_baby = findViewById(R.id.input_baby);
        String input_baby = text_baby.getText().toString();
        EditText text_id = findViewById(R.id.input_id);
        order_id = text_id.getText().toString();
        try {
            if(isInteger(input_old) == false || isInteger(input_adult) == false || isInteger(input_baby) == false ) {
                throw new Exception();
            }
            order[0] = Integer.parseInt(input_old);
            order[1] = Integer.parseInt(input_adult);
            order[2] = Integer.parseInt(input_baby);
            int total = order[0] + order[1] + order[2];

            if(total > tripset.getPeople_max() || total < tripset.getPeople_min()){
                Toast.makeText(select_Activity.this, "error range", Toast.LENGTH_SHORT).show();
                return;
            }else{
                finish();
            }

        }catch (Exception e){
            Toast.makeText(select_Activity.this, "please enter a number in order", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    public void cancel(View view) {
        finish();
    }

    @Override
    public void finish(){
        setResult(order[0] + order[1] + order[2]);

        //add this order to database

        super.finish();
    }

    public void showInfo(){
        TextView title = findViewById(R.id.title);
        title.setText(tripset.getTitle());
        TextView date = findViewById(R.id.date);
        date.setText("from : " + tripset.getStart_date() + " to : " + tripset.getEnd_date());
        TextView people = findViewById(R.id.people);
        people.setText("min : " + tripset.getPeople_min() + " max : " + tripset.getPeople_max());
        TextView price = findViewById(R.id.price);
        price.setText("price : " + tripset.getPrice());
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

}
