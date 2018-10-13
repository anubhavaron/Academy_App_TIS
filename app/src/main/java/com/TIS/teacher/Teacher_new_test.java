package com.TIS.teacher;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.TIS.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class  Teacher_new_test extends AppCompatActivity implements  new_test_adapter.new_test_adapterOnClickHandler {
    String test_name[];
    String test_marks[];
    new_test_adapter adapter;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_new_test);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        recyclerView=(RecyclerView)findViewById(R.id.recycler_29);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_29);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Teacher_new_test.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_new_test,null);
                final EditText sub=(EditText)mView.findViewById(R.id.test_name_31);
                final EditText cl=(EditText)mView.findViewById(R.id.marks_31);

                final Button add=(Button)mView.findViewById(R.id.add_31);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);
                        add.setVisibility(View.GONE);
                        ParseObject gameScore = new ParseObject("test_name_table");
                        gameScore.put("test_name", sub.getText().toString());
                        gameScore.put("total_marks", cl.getText().toString());
                        gameScore.put("batch_number", Teacher_navigation.batch_number);
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.put("batch_class", Teacher_navigation.batch_class);
                        gameScore.put("batch_subject", Teacher_navigation.batch_subject);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(Teacher_new_test.this, "Item saved!", Toast.LENGTH_SHORT).show();
                               // Background_youtube_links();
                                getting_test();
                            }
                        });


                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });
        context=getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(Teacher_new_test.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new new_test_adapter(Teacher_new_test.this);
        recyclerView.setAdapter(adapter);
        getting_test();
       // new Background_test_info().execute();
    }
    void getting_test()
    {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("test_name_table");
        query.whereEqualTo("batch_subject",Teacher_navigation.batch_subject);
        query.whereEqualTo("batch_class",Teacher_navigation.batch_class);
        query.whereEqualTo("batch_number",Teacher_navigation.batch_number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Teacher_new_test.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {


                        int count = 0;


                        // jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                         test_name = new String[size];
                        test_marks = new String[size];
                        while (count < size) {
                            //getJSONObject(count);
                            test_name[count] = scoreList.get(count).getString("test_name");
                            test_marks[count] = scoreList.get(count).getString("total_marks");

                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), test_name,test_marks);

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(Teacher_new_test.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    @Override
    public void onClick(int x) {

       Intent intent=new Intent(Teacher_new_test.this,Test_Record.class);
        intent.putExtra("test_name", test_name[x]);
        intent.putExtra("total_marks",test_marks[x]);
        Toast.makeText(Teacher_new_test.this,test_name[x]+" "+test_marks[x],Toast.LENGTH_LONG).show();
        startActivity(intent);

    }



}
