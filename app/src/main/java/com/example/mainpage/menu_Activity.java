package com.example.mainpage;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.mainpage.database.TripSetGetData;
import com.example.mainpage.model.TripSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class menu_Activity extends AppCompatActivity {

    //list arg
    private ListView listView;
    private String[] from = {"title" , "start_date", "end_date", "price", "people_min", "people_max"};
    private int[] to = {R.id.item_title , R.id.item_start_date, R.id.item_end_date,
            R.id.item_price, R.id.item_people_min, R.id.item_people_max};
    private LinkedList<HashMap<String , String>> data = new LinkedList<>();
    SimpleAdapter adapter;

    private TripSetGetData TripDB;
    private ArrayList<TripSet> list;
    private int position = -1;

    public menu_Activity() {
        new Thread(){
            public void run(){
                try {
                    TripDB = new TripSetGetData((menu_Activity.this));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_);

        //set listView
        list = TripDB.getAll(20);
        listView = findViewById(R.id.my_list_view);
        initList();

        //Click list event
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;
                Toast.makeText(menu_Activity.this, "你點擊了第" + pos, Toast.LENGTH_SHORT).show();

                String select_info = list.get(pos).allToString();
                Intent next_page = new Intent(menu_Activity.this , select_Activity.class );
                next_page.putExtra("info" , select_info);
                startActivityForResult(next_page , 0);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data2) {
        super.onActivityResult(requestCode, resultCode, data2);
        if(resultCode == -1000){
            return;
        }
        System.out.println("order = "+ resultCode);
        TripSet target = list.get(position);

        int err = TripDB.updateTripSet(target.getTitle() , target.getStart_date() , target.getEnd_date() , resultCode);
        if(err == -1 ){
            System.out.println("update err!!!");
        }
        position = -1;

        EditText text = findViewById(R.id.text_input);
        String input = text.getText().toString();
        data.clear();
        list = TripDB.searchBySubtitle(input);
        renewList();
    }

    //list create
    private void initList() {
        adapter = new SimpleAdapter(this, data, R.layout.list_layout, from , to);
        listView.setAdapter(adapter);
        for(int i=0 ; i< list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], "title: " + list.get(i).getTitle());
            d.put(from[1], "start date: " + list.get(i).getStart_date());
            d.put(from[2], "end date: " + list.get(i).getEnd_date());
            d.put(from[3], "price: " + list.get(i).getPrice());
            d.put(from[4], "min people: " + list.get(i).getPeople_min());
            d.put(from[5], "max people: " + list.get(i).getPeople_max());
            data.add(d);
        }
    }
    //click! search
    public void search(View v) {
        //get searching input
        EditText text = findViewById(R.id.text_input);
        String input = text.getText().toString();
        //clean old search
        data.clear();
        list = TripDB.searchBySubtitle(input);
        renewList();
    }
    //renew the list
    private  void renewList(){
        data.clear();
        for(int i=0 ; i< list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], "title: " + list.get(i).getTitle());
            d.put(from[1], "start date: " + list.get(i).getStart_date());
            d.put(from[2], "end date: " + list.get(i).getEnd_date());
            d.put(from[3], "price: " + String.valueOf(list.get(i).getPrice()));
            d.put(from[4], "min people: " + String.valueOf(list.get(i).getPeople_min()));
            d.put(from[5], "max people: " + String.valueOf(list.get(i).getPeople_max()));
            data.add(d);
        }
        adapter.notifyDataSetChanged();
    }

    public void MyOrder(View view) {
        //init
        LayoutInflater inflater = LayoutInflater.from(menu_Activity.this);
        final View v = inflater.inflate(R.layout.alertdialog_layout, null);
        //ID request and login
        new AlertDialog.Builder(menu_Activity.this)
                .setTitle("請輸入你的id")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) (v.findViewById(R.id.editText1));
                        String id = editText.getText().toString();
                        Toast.makeText(getApplicationContext(), "你的id是" +
                                id, Toast.LENGTH_SHORT).show();
                        //跳頁
                        Intent next_page = new Intent(menu_Activity.this , MyOrderActivity.class );
                        next_page.putExtra("id" , id);
                        startActivityForResult(next_page , 0);
                    }
                })
                .show();
    }
}
