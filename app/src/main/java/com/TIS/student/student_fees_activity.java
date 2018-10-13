package com.TIS.student;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.TIS.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class student_fees_activity extends AppCompatActivity implements com.TIS.head.fees_adapter.fees_adapterOnClickHandler {
    String start_date[];
    String end_date[];
    String amount[];
    RecyclerView recyclerView;
    String username;
    com.TIS.head.fees_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fees_activity);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_53);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //Toast.makeText(student_fees_activity.this,pref.getString("username", null),Toast.LENGTH_SHORT).show();

        username=pref.getString("username", null);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new com.TIS.head.fees_adapter(student_fees_activity.this);
        recyclerView.setAdapter(adapter);

        //new Background_getting_fees().execute();
        getting_previous_fees();

    }



    void getting_previous_fees()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("fees_table");
        query.whereEqualTo("username",Student_Navigation.username);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(student_fees_activity.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {

                        int count = 0;


                        //jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        start_date = new String[size];
                        end_date = new String[size];
                        amount = new String[size];
                        while (count < size) {

                            start_date[count] = scoreList.get(count).getString("start_date");
                            end_date[count] = scoreList.get(count).getString("end_date");
                            amount[count] = scoreList.get(count).getString("batch_subject");


                            count++;


                        }

                        //Toast.makeText(getApplicationContext(), count + "         hello", Toast.LENGTH_LONG).show();
                        adapter.swapCursor(getApplicationContext(), amount, end_date, start_date);



                    }
                } else {
                    Toast.makeText(student_fees_activity.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onClick(int x) {

    }



}
