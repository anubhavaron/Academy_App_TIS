package com.example.pc.academy_app_tis.teacher;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.You_Tube_links;
import com.example.pc.academy_app_tis.head.Students_details_adapter;
import com.example.pc.academy_app_tis.head.fees;
import com.example.pc.academy_app_tis.head.fees_adapter;
import com.example.pc.academy_app_tis.head.head_navigation;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import junit.framework.Test;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Test_Record extends AppCompatActivity implements Test_record_adapter.Test_record_adapterOnClickHandler {
    String test_name;
    String test_marks;
    FloatingActionButton floatingActionButton;
    String name[];
    String username[];
    String marks[];

    ArrayAdapter<String> arrayList;
    ArrayList<String> names;
     AutoCompleteTextView sub;
     Test_record_adapter adapter;
     Context context;
     RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test__record);



        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();



        test_name=getIntent().getStringExtra("test_name");
        test_marks=getIntent().getStringExtra("total_marks");
        Toast.makeText(Test_Record.this,test_name+test_marks,Toast.LENGTH_LONG).show();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_32);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_32);

        context=getApplicationContext();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Test_Record.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_test_record,null);
                 sub=(AutoCompleteTextView) mView.findViewById(R.id.name_33);
                final EditText cl=(EditText)mView.findViewById(R.id.marks_33);



                final Button add=(Button)mView.findViewById(R.id.add_33);
                getting_names();
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);
                        add.setVisibility(View.GONE);

                        ParseObject gameScore = new ParseObject("test_record_table");
                        gameScore.put("test_name", test_name);
                        gameScore.put("username", sub.getText().toString());
                        gameScore.put("batch_subject",Teacher_navigation.batch_subject);
                        gameScore.put("batch_class", Teacher_navigation.batch_class);
                        gameScore.put("batch_number", Teacher_navigation.batch_number);
                        gameScore.put("total_marks", test_marks);
                        gameScore.put("Marks_obtained", cl.getText().toString());



                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(Test_Record.this, "Item saved!", Toast.LENGTH_SHORT).show();
                               // Background_youtube_links();
                                getting_test_record();
                            }
                        });



                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();

            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Test_record_adapter(Test_Record.this);
        recyclerView.setAdapter(adapter);
        getting_test_record();



    }
    void getting_test_record()
    {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("test_record_table");
        query.whereEqualTo("batch_subject",Teacher_navigation.batch_subject);
        query.whereEqualTo("batch_class",Teacher_navigation.batch_class);
        query.whereEqualTo("batch_number",Teacher_navigation.batch_number);
        query.whereEqualTo("test_name",test_name);
       // query.orderByDescending("marks_obtained");

        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Test_Record.this,"Nothing_Found",Toast.LENGTH_LONG).show();
                        //f_adapter.swapCursor(getApplicationContext(), null, null, null);

                    }
                    else {

                        int count = 0;


                        //jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        username = new String[size];
                        marks = new String[size];

                        while (count < size) {

                            username[count] = scoreList.get(count).getString("username");
                            marks[count] = scoreList.get(count).getString("Marks_obtained");
                            //amount[count] = scoreList.get(count).getString("amount");


                            count++;


                        }

                        //Toast.makeText(getApplicationContext(), count + "         hello", Toast.LENGTH_LONG).show();
                        adapter.swapCursor(getApplicationContext(), username, marks);



                    }
                } else {
                    Toast.makeText(Test_Record.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(int x) {

    }
    void getting_names()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("login_table");
        query.whereEqualTo("batch_subject",Teacher_navigation.batch_subject);
        query.whereEqualTo("batch_class",Teacher_navigation.batch_class);
        query.whereEqualTo("batch_number",Teacher_navigation.batch_number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Test_Record.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {

                        Toast.makeText(Test_Record.this,scoreList.size()+"",Toast.LENGTH_LONG).show();
                        int count = 0;
                       ArrayList<String> names = new ArrayList<String>();



                        int size = scoreList.size();
                        name = new String[size];
                        while (count < size) {

                            name[count] = scoreList.get(count).getString("username");
                            names.add(name[count]);

                            count++;


                        }


                        arrayList = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, names);
                        arrayList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sub.setAdapter(arrayList);



                    }
                } else {
                    Toast.makeText(Test_Record.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });


    }







}
