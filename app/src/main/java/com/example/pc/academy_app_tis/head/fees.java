package com.example.pc.academy_app_tis.head;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class fees extends AppCompatActivity implements fees_adapter.fees_adapterOnClickHandler{
    String batch_subject;
    String batch_class;
    String batch_number;
    TextView sub;
    TextView cla;
    TextView num;
    String name[];
    Button button;
    Context context;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayList;
    ArrayList<String> names;
    String start_date[];
    String end_date[];
    String amount[];
    RecyclerView recyclerView;
    fees_adapter f_adapter;
    FloatingActionButton floatingActionButton;
    LinearLayout linearLayout;
    View view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        batch_subject=head_navigation.batch_subject;
        batch_class=head_navigation.batch_class;
        batch_number=head_navigation.batch_number;


        sub=(TextView)findViewById(R.id.subject_12);
        cla=(TextView)findViewById(R.id.class_12);
        num=(TextView)findViewById(R.id.number_12);
        button=(Button)findViewById(R.id.previous_fees_12);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_12);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_12);
        linearLayout=(LinearLayout)findViewById(R.id.ll);
        view=(View)findViewById(R.id.lll);





        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        f_adapter=new fees_adapter(fees.this);
        recyclerView.setAdapter(f_adapter);

        autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.student_name_12);
        sub.setText(batch_subject);
        cla.setText(batch_class);
        num.setText(batch_number);
        context=fees.this;
        getting_names();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayout.setVisibility(View.VISIBLE);
                view.setVisibility(View.VISIBLE);
                getting_previous_fees();
            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(fees.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_adding_fees,null);
                final EditText sub=(EditText)mView.findViewById(R.id.start_date_14);
                final EditText cl=(EditText)mView.findViewById(R.id.end_date_14);
                final EditText n=(EditText)mView.findViewById(R.id.amount_14);
                final Button add=(Button)mView.findViewById(R.id.add_14);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);
                        add.setVisibility(View.GONE);

                        ParseObject gameScore = new ParseObject("fees_table");

                        gameScore.put("username", autoCompleteTextView.getText().toString());
                        gameScore.put("start_date", sub.getText().toString());
                        gameScore.put("end_date",cl.getText().toString());
                        gameScore.put("amount",n.getText().toString());
                        gameScore.put("batch_class",head_navigation.batch_class);
                        gameScore.put("batch_number",head_navigation.batch_number);
                        gameScore.put("batch_subject",head_navigation.batch_subject);
                        // gameScore.put("batch_number", n.getText().toString());
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(fees.this, "Item saved!", Toast.LENGTH_SHORT).show();
                                // Background_wall_of_fame_parse();
                                // Background_details_of_batch();
                                getting_previous_fees();
                            }
                        });

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();




            }
        });


    }
    void getting_previous_fees()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("fees_table");
        query.whereEqualTo("batch_subject",head_navigation.batch_subject);
        query.whereEqualTo("batch_class",head_navigation.batch_class);
        query.whereEqualTo("batch_number",head_navigation.batch_number);
        query.whereEqualTo("username",autoCompleteTextView.getText().toString());
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(fees.this,"Nothing_Found",Toast.LENGTH_LONG).show();
                        f_adapter.swapCursor(getApplicationContext(), null, null, null);

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
                            amount[count] = scoreList.get(count).getString("amount");


                            count++;


                        }

                        //Toast.makeText(getApplicationContext(), count + "         hello", Toast.LENGTH_LONG).show();
                        f_adapter.swapCursor(getApplicationContext(), start_date, end_date, amount);



                    }
                } else {
                    Toast.makeText(fees.this,"Connection_problem",Toast.LENGTH_LONG).show();
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
        query.whereEqualTo("batch_subject",head_navigation.batch_subject);
        query.whereEqualTo("batch_class",head_navigation.batch_class);
        query.whereEqualTo("batch_number",head_navigation.batch_number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(fees.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {

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
                        autoCompleteTextView.setAdapter(arrayList);



                    }
                } else {
                    Toast.makeText(fees.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });


    }







}
