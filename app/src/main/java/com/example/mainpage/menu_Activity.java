package com.example.mainpage;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class menu_Activity<Tripset> extends AppCompatActivity {

    //list arg
    private ListView listView;
    private String[] from = {"title" , "start_date", "end_date", "price", "people_min", "people_max"};
    private int[] to = {R.id.item_title , R.id.item_start_date, R.id.item_end_date,
            R.id.item_price, R.id.item_people_min, R.id.item_people_max};
    private LinkedList<HashMap<String , String>> data = new LinkedList<>();
    SimpleAdapter adapter;
    TripSet[] tripsets;
    //
    private int position = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);
        //read data
        //readCSV();

        //set listView
        listView = findViewById(R.id.my_list_view);
        initList();
        //Click list event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;
                Toast.makeText(menu_Activity.this, "你點擊了第" + pos, Toast.LENGTH_SHORT).show();
                if(false/*people lef == 0*/){
                    Toast.makeText(menu_Activity.this, "機位已售罄" + pos, Toast.LENGTH_SHORT).show();
                    position = -1;
                    return;
                }

                //data in tripset[i]
                //select_info = tripsets[pos].allToString();
                String select_info = "title,date1,date2,1000,10,100";

                Intent next_page = new Intent(menu_Activity.this , select_Activity.class );
                next_page.putExtra("info" , select_info);
                startActivityForResult(next_page , 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("order = "+ resultCode);
        position = -1;
        //renew the data base
        //add travel to my_travel in data base
    }

    //list create
    private void initList() {
        adapter = new SimpleAdapter(this, data, R.layout.list_layout, from , to);
        listView.setAdapter(adapter);
        HashMap<String , String> d = new HashMap<>();
        for(int i = 0; i < 6 ; i++){
            d.put(from[i] , from[i]);
        }
        data.add(d);
    }

    //renew the list

    private  void renewList(/*tripset*/){
        HashMap<String , String> d = new HashMap<>();
        for(int i = 0; i < 6 ; i++){
            d.put(from[i] , from[i]);
        }
        data.add(d);
    }

    //file reading
    private void readCSV(){

        InputStream is = getResources().openRawResource(R.raw.trip_data_all);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is, Charset.forName("UTF-8"))
        );

        String line = "";
        try {
            while ((line = reader.readLine()) != null) {
                //data deal
                System.out.println(line);
            }
        }
            catch (IOException e) {
                e.printStackTrace();
            }
    }

    //click! search
    public void search(View v) {
        //get searching input
        EditText text = findViewById(R.id.text_input);
        String input = text.getText().toString();

        //clean old search
        data.remove();
        tripsets = null;
        /*
        *get data through db
        * tripsets = ;
        */

        //show new search

        for(int i = 0; i < 10; i++)
            renewList(/*tripset[i]*/);
        adapter.notifyDataSetChanged();
    }

    public void MyOrder(View view) {
        Intent my_order = new Intent(menu_Activity.this , MyOrderActivity.class);
        //System.out.println("ready to jump");
        startActivity(my_order);
    }
}
