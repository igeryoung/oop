package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class MyOrderActivity extends AppCompatActivity {

    private ListView listView;
    private String[] from = {"title" , "code" ,"start_date", "end_date", "adult", "child", "baby" , "price"};
    private int[] to = {R.id.order_title , R.id.order_code, R.id.order_start_date,
            R.id.order_end_date, R.id.order_people_adult, R.id.order_people_child , R.id.order_people_baby , R.id.order_price};
    private LinkedList<HashMap<String , String>> data = new LinkedList<>();
    SimpleAdapter adapter;
    OrderGetData OrderDB;
    int CID;
    ArrayList<CostumerOrder> list;
    int position = -1;


    public MyOrderActivity() throws IOException {
        new Thread(){
            public void run(){
                OrderDB = new OrderGetData((MyOrderActivity.this));
            }
        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //get msg
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        CID = Integer.parseInt(id);
        System.out.println(id);
        //create list
        listView = findViewById(R.id.order_list);
        initList();
        //Click list event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;
                Toast.makeText(MyOrderActivity.this, "你點擊了第" + pos, Toast.LENGTH_SHORT).show();

                String select_info = list.get(pos).allToString();
                Intent next_page = new Intent(MyOrderActivity.this , order_item_Activity.class );
                next_page.putExtra("info" , select_info);
                startActivityForResult(next_page , 0);
            }
        });
    }
    private void initList() {
        adapter = new SimpleAdapter(this, data, R.layout.order_layout, from , to);
        listView.setAdapter(adapter);

        list = OrderDB.getOrderByCI(CID);
        for(int i = 0; i < list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], "title: " + list.get(i).getTitle());
            d.put(from[1], "code: " + list.get(i).getOrderId());
            d.put(from[2], "start date: " + list.get(i).getStart_date());
            d.put(from[3], "end date: " + list.get(i).getEnd_date());
            d.put(from[4], "adult: " + list.get(i).getAdult());
            d.put(from[5], "child: " + list.get(i).getChild());
            d.put(from[6], "baby: " + list.get(i).getBaby());
            d.put(from[7], "price: " + list.get(i).getPrice());
            data.add(d);
        }
    }

    public void finish(){
        setResult(-1000);
        super.finish();
    }

}
