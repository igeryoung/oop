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
    private String[] from = {"title" , "start_date", "end_date", "price", "people_min", "people_max"};
    private int[] to = {R.id.item_title , R.id.item_start_date, R.id.item_end_date,
            R.id.item_price, R.id.item_people_min, R.id.item_people_max};
    private LinkedList<HashMap<String , String>> data = new LinkedList<>();
    SimpleAdapter adapter;
    TripSet[] tripsets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);

        listView = findViewById(R.id.my_list_view);
        initList();
        //Click list event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                //data in tripset[i]
                //select_info = tripsets[pos].allToString();

                String select_info = "title,date1,date2,1000,10,100";
                System.out.println("ready to intent");
                Intent next_page = new Intent(MyOrderActivity.this , order_item_Activity.class );
                next_page.putExtra("info" , select_info);
                //startActivity(next_page);
                startActivityForResult(next_page , 0);
            }
        });
    }
        private void initList() {
        adapter = new SimpleAdapter(this, data, R.layout.list_layout, from , to);
        listView.setAdapter(adapter);
        HashMap<String , String> d = new HashMap<>();
        for(int k = 0; k < 20 ; k++){
            for(int i = 0; i < 6 ; i++){
                d.put(from[i] , from[i]);
            }
            data.add(d);
        }
    }
}
