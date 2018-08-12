package com.example.pc.academy_app_tis.student;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.fees;
import com.example.pc.academy_app_tis.head.head_navigation;
import com.example.pc.academy_app_tis.teacher.Feed_Adapter;
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

public class student_test_records extends AppCompatActivity implements Test_Marks_Adapter.Test_Marks_AdapterOnClickHandler {
    String username;
    RecyclerView recyclerView;
    Test_Marks_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_test_records);


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_102);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
      //  Toast.makeText(student_test_records.this,pref.getString("username", null),Toast.LENGTH_SHORT).show();

        username=pref.getString("username", null);
       // new Background_getting_marks().execute();
        LinearLayoutManager layoutManager=new LinearLayoutManager(student_test_records.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Test_Marks_Adapter(student_test_records.this);
        recyclerView.setAdapter(adapter);
        getting_records();
    }


    void getting_records()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("test_record_table");
        query.whereEqualTo("username",Student_Navigation.username);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(student_test_records.this,"Nothing_Found",Toast.LENGTH_LONG).show();
                        //f_adapter.swapCursor(getApplicationContext(), null, null, null);

                    }
                    else {

                        int count = 0;


                        //jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        String[] start_date = new String[size];
                        String[] test_name = new String[size];
                        String[] marks = new String[size];
                        String[] total=new String [size];
                        while (count < size) {

                            start_date[count] = scoreList.get(count).getString("batch_subject");
                            test_name[count] = scoreList.get(count).getString("test_name");
                            marks[count] = Integer.toString(scoreList.get(count).getInt("marks_obtained"));
                            total[count] = scoreList.get(count).getString("total_marks");


                            count++;


                        }

                        //Toast.makeText(getApplicationContext(), count + "         hello", Toast.LENGTH_LONG).show();
                        adapter.swapCursor(getApplicationContext(), start_date, test_name, marks,total);



                    }
                } else {
                    Toast.makeText(student_test_records.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    @Override
    public void onClick(int x) {

    }



}
