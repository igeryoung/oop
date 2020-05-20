package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.HashMap;
import java.util.LinkedList;

public class MyOrderActivity extends AppCompatActivity {

    private ListView listView;
    private String[] from = {"title" , "code" ,"start_date", "end_date", "adult", "child", "baby" , "price"};
    private int[] to = {R.id.order_title , R.id.order_code, R.id.order_start_date,
            R.id.order_end_date, R.id.order_people_adult, R.id.order_people_child , R.id.order_people_baby , R.id.order_price};
    private LinkedList<HashMap<String , String>> data = new LinkedList<>();
    SimpleAdapter adapter;
    TripSet[] tripsets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        //get msg
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        System.out.println(id);
        //create list
        listView = findViewById(R.id.order_list);
        initList();
        //Click list event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //data in tripset[i]
                //select_info = tripsets[pos].allToString();

                String select_info = "title,100,date1,date2,10,10,100,1000,20"/*20 is id*/;
                System.out.println("ready to intent");
                Intent next_page = new Intent(MyOrderActivity.this , order_item_Activity.class );
                next_page.putExtra("order" , select_info);
                //startActivity(next_page);
                startActivityForResult(next_page , 1);
            }
        });
    }
    private void initList() {
        adapter = new SimpleAdapter(this, data, R.layout.order_layout, from , to);
        listView.setAdapter(adapter);
        HashMap<String , String> d = new HashMap<>();
        for(int k = 0; k < 20 ; k++){
            for(int i = 0; i < 8 ; i++){
                d.put(from[i] , from[i]);
            }
            data.add(d);
        }
    }
}
