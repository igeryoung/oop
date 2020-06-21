package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.mainpage.database.OrderGetData;
import com.example.mainpage.model.CostumerOrder;

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
    private OrderGetData OrderDB;
    int CID;
    private ArrayList<CostumerOrder> list;
    int position = -1;

    //initial database
    public MyOrderActivity() {
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

        //get msg from last page
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        CID = Integer.parseInt(id);
        System.out.println(id);

        //create list
        listView = findViewById(R.id.order_list);
        initList();

        //Click list event : find which position user selected , and change to detail page of selected item ( order_item_Activity )
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;
                String select_info = list.get(pos).allToString();
                System.out.println("In MyOrderActivity select_info is " + select_info);
                Intent next_page = new Intent(MyOrderActivity.this , order_item_Activity.class );
                next_page.putExtra("info" , select_info);
                startActivityForResult(next_page , 0);
            }
        });
    }

    //when back to this page , we renew the list information.
    @Override
    protected void onResume() {
        //clean old list
        data.clear();
        list = OrderDB.getOrderByCI(CID);
        //put data into list info manager
        for(int i = 0; i < list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], list.get(i).getTitle());
            d.put(from[1], "order number: " + list.get(i).getOrderId());
            d.put(from[2], "start date: " + list.get(i).getStart_date());
            d.put(from[3], "end date: " + list.get(i).getEnd_date());
            d.put(from[4], "adult: " + list.get(i).getAdult());
            d.put(from[5], "child: " + list.get(i).getChild());
            d.put(from[6], "baby: " + list.get(i).getBaby());
            d.put(from[7], "total price: " + list.get(i).getPrice());
            data.add(d);
        }
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    //list create
    private void initList() {
        adapter = new SimpleAdapter(this, data, R.layout.order_layout, from , to);
        listView.setAdapter(adapter);
        //put data into list info manager
        list = OrderDB.getOrderByCI(CID);
        for(int i = 0; i < list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], list.get(i).getTitle());
            d.put(from[1], "order number: " + list.get(i).getOrderId());
            d.put(from[2], "start date: " + list.get(i).getStart_date());
            d.put(from[3], "end date: " + list.get(i).getEnd_date());
            d.put(from[4], "adult: " + list.get(i).getAdult());
            d.put(from[5], "child: " + list.get(i).getChild());
            d.put(from[6], "baby: " + list.get(i).getBaby());
            d.put(from[7], "total price: " + list.get(i).getPrice());
            data.add(d);
        }
    }
    // Override finish to send result code
    @Override
    public void finish(){
        setResult(-1000);
        super.finish();
    }

}
