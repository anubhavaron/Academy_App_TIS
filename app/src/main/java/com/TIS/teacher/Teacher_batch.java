package com.TIS.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.TIS.MainActivity;
import com.TIS.R;
import com.TIS.head.Head_Batch_Adapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class Teacher_batch extends AppCompatActivity implements Head_Batch_Adapter.Head_Batch_AdapterOnClickHandler {


    RecyclerView recyclerView;
    Head_Batch_Adapter adapter;
    String batch_subject[];
    String batch_class[];
    String batch_number[];
    int backpress=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_batch);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_21);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Head_Batch_Adapter(Teacher_batch.this);



        recyclerView.setAdapter(adapter);
        Background_details_of_batch();
    }



    void Background_details_of_batch()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("batch_table");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Teacher_batch.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count=0;

                        int size = scoreList.size();
                        batch_subject = new String[size];
                        batch_class = new String[size];
                        batch_number = new String[size];
                        while (count < size) {

                            batch_subject[count] = scoreList.get(count).getString("batch_subject");
                            batch_class[count] = scoreList.get(count).getString("batch_class");
                            batch_number[count] = scoreList.get(count).getString("batch_number");


                            count++;


                        }
                        if (batch_class != null) {
                            adapter.swapCursor(getApplicationContext(), batch_subject, batch_class, batch_number);
                        }

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(Teacher_batch.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });



    }









    @Override
    public void onClick(int x) {
        //Toast.makeText(Teacher_batch.this,x+"",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(Teacher_batch.this,Teacher_navigation.class);
        intent.putExtra("batch_subject", batch_subject[x]);
        intent.putExtra("batch_class", batch_class[x]);
        intent.putExtra("batch_number", batch_number[x]);

        startActivity(intent);


    }
    @Override
    public void onBackPressed(){
        backpress = (backpress + 1);
        if(backpress==1) {
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
        if (backpress>1) {
            this.finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.head_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("is", false);

            editor.apply();
            Intent i=new Intent(Teacher_batch.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
