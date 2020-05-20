package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class order_item_Activity extends AppCompatActivity {
    private CostumerOrder order;
    private TripSet tripset;
    private int[] order_num = {0,0,0};
    private String order_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_item_);
        //init
        Intent intent = getIntent();
        String info = intent.getStringExtra("order");
        System.out.println(info);
        //get order and init view
        tripset = null;/*find tripset by title*/
        order = new CostumerOrder(info);
        showInfo();
    }

    public void showInfo(){
        TextView title = findViewById(R.id.title);
        title.setText(order.getTitle());
        TextView date = findViewById(R.id.date);
        date.setText("from : " + order.getStart_date() + " to : " + order.getEnd_date());
        TextView price = findViewById(R.id.price);
        price.setText("price : " + order.getPrice());

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
        EditText text_id = findViewById(R.id.input_id);
        order_id = text_id.getText().toString();

        if(text_id.getText().length() == 0){
            Toast.makeText(order_item_Activity.this, "please enter costumer id", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if(isInteger(input_old) == false || isInteger(input_adult) == false || isInteger(input_baby) == false ) {
                throw new Exception();
            }
            order_num[0] = Integer.parseInt(input_old);
            order_num[1] = Integer.parseInt(input_adult);
            order_num[2] = Integer.parseInt(input_baby);
            System.out.println(""+order_num[0] + "," + order_num[1] + "," + order_num[2] );
            int total = order_num[0] + order_num[1] + order_num[2];
            finish();
            /*if(total > tripset.getPeople_max() || total < tripset.getPeople_min()){
                Toast.makeText(order_item_Activity.this, "error range", Toast.LENGTH_SHORT).show();
                return;
            }else{
                finish();
            }*/

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

    @Override
    public void finish(){
        setResult(order_num[0] + order_num[1] + order_num[2]);

        //add this order to database
        super.finish();
    }
}
