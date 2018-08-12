package com.example.pc.academy_app_tis.student;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.You_Tube_links;
import com.example.pc.academy_app_tis.head.Head_Batch;
import com.example.pc.academy_app_tis.head.Head_Batch_Adapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
import java.util.List;

public class student_batch extends AppCompatActivity implements Batch_Adapter.Batch_AdapterOnClickHandler {
    String batch_subject[];
    String batch_class[];
    String batch_number[];
    Batch_Adapter adapter;
    static  String subject_s;
    static String class_s;
    static String number_s;

    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_batch);


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_51);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        adapter=new Batch_Adapter(student_batch.this);
        recyclerView.setAdapter(adapter);
        getting_batches();
    }

    void getting_batches()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("login_table");
        query.whereEqualTo("username",Student_Navigation.username);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(student_batch.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {


                        int count = 0;


                        // jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        batch_subject = new String[size];
                        batch_class=new String[size];
                        batch_number = new String[size];

                        while (count < size) {
                            //getJSONObject(count);
                            batch_subject[count] = scoreList.get(count).getString("batch_subject");
                            batch_class[count] = scoreList.get(count).getString("batch_class");
                            batch_number[count] = scoreList.get(count).getString("batch_number");

                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), batch_subject,batch_class,batch_number);

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(student_batch.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });


    }
    @Override
    public void onClick(int x) {
        subject_s=batch_subject[x];
        class_s=batch_class[x];
        number_s=batch_number[x];
        Intent i=new Intent(student_batch.this,students_feed.class);
        startActivity(i);

    }



}
