package com.example.mainpage.allTravel;

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

import com.example.mainpage.customerOrder.MyOrderActivity;
import com.example.mainpage.R;
import com.example.mainpage.database.TripSetGetData;
import com.example.mainpage.model.TripSet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
/*
* menu page : show the list of trip and provide searching operation
*       searching operation : searching by subtitle & travel code
* two button in Activity :
*       (a) my order : check in account by ID and show the order of it
*       (b) search : get string in text blank and searching in database
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class menu_Activity extends AppCompatActivity {

    //list arg
    private ListView listView;
    private String[] from = {"title" , "start_date", "end_date", "price", "people_min", "people_max"};
    private int[] to = {R.id.item_title , R.id.item_start_date, R.id.item_end_date};
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
        list = TripDB.getAll(500);
        listView = findViewById(R.id.my_list_view);
        initList();

        //Click list event : find which position user selected , and change to detail page of selected item ( select_Activity )
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                position = pos;
                String select_info = list.get(pos).allToString();
                Intent next_page = new Intent(menu_Activity.this , select_Activity.class );
                next_page.putExtra("info" , select_info);
                startActivityForResult(next_page , 0);
            }
        });
    }
    /*
    *when back to this page , we renew the list information.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data2) {
        super.onActivityResult(requestCode, resultCode, data2);

        if(resultCode == -1000){    //back from user_order ( MyOrderActivity )
            EditText text = findViewById(R.id.text_input);
            String input = text.getText().toString();

            //clear old data
            data.clear();

            //set new data
            list = TripDB.searchBySubtitle(input);
            renewList();
            return;
        }

        //back from select_Activity and renew database
        System.out.println("order = "+ resultCode);
        TripSet target = list.get(position);
        int err = TripDB.updateTripSet(target.getTitle() , target.getStart_date() , target.getEnd_date() , resultCode);
        if(err == -1 ){
            System.out.println("update err!!!");
        }

        //renew current list
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
        //put data into list info manager
        for(int i=0 ; i< list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], list.get(i).getTitle());
            d.put(from[1], list.get(i).getStart_date());
            d.put(from[2], list.get(i).getEnd_date());
//            d.put(from[3], list.get(i).getPrice());
//            d.put(from[4], list.get(i).getPeople_min());
//            d.put(from[5], list.get(i).getPeople_max());
            data.add(d);
        }
    }
    // search : handle click event when search is clicked
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
        //clear old list
        data.clear();
        //put new data into list info manager
        for(int i=0 ; i< list.size() ; i++){
            HashMap<String , String> d = new HashMap<>();
            d.put(from[0], list.get(i).getTitle());
            d.put(from[1], list.get(i).getStart_date());
            d.put(from[2], list.get(i).getEnd_date());
//            d.put(from[3], String.valueOf(list.get(i).getPrice()));
//            d.put(from[4], String.valueOf(list.get(i).getPeople_min()));
//            d.put(from[5], String.valueOf(list.get(i).getPeople_max()));
            data.add(d);
        }
        adapter.notifyDataSetChanged();
    }

    // MyOrder : handle click event when MyOrder is clicked
    public void MyOrder(View view) {
        //init AlertDialog
        LayoutInflater inflater = LayoutInflater.from(menu_Activity.this);
        final View v = inflater.inflate(R.layout.alertdialog_layout, null);
        //show AlertDialog of ID request and login
        new AlertDialog.Builder(menu_Activity.this)
                .setTitle("請輸入你的id")
                .setView(v)
                .setPositiveButton("確定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText editText = (EditText) (v.findViewById(R.id.editText1));
                        String id = editText.getText().toString();

                        //check id is provided or not
                        if(editText.getText().length() == 0){
                            Toast.makeText(menu_Activity.this, "please enter costumer id", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //check id is integer or not
                        if(isInteger(editText.getText().toString()) == false){
                            Toast.makeText(menu_Activity.this, "please enter a number as costumer id", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //parse ID to int and check is positive or not
                        int CID = Integer.parseInt(editText.getText().toString());
                        if(CID <= 0){
                            Toast.makeText(menu_Activity.this, "please enter a number larger than 0 as costumer id", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        //jump to MyOrderActivity
                        Intent next_page = new Intent(menu_Activity.this , MyOrderActivity.class );
                        next_page.putExtra("id" , id);
                        startActivityForResult(next_page , 0);
                    }
                })
                .show();
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
