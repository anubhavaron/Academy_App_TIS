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
import com.example.pc.academy_app_tis.head.Students_details_adapter;
import com.example.pc.academy_app_tis.head.head_navigation;

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
                Background_getting_students_here background_getting_students_here=new Background_getting_students_here();
                background_getting_students_here.execute();

                final Button add=(Button)mView.findViewById(R.id.add_33);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);
                        add.setVisibility(View.GONE);


                       Background_test_record background_new_test_add=new Background_test_record(Test_Record.this);
                        background_new_test_add.execute(Teacher_navigation.batch_subject,Teacher_navigation.batch_class,Teacher_navigation.batch_number,test_name,sub.getText().toString(),cl.getText().toString(),test_marks);

                        new Background_getting_test_here().execute();
                   /*     Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                        background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());*/


                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();

            }
        });
        new Background_getting_test_here().execute();


    }

    @Override
    public void onClick(int x) {

    }


    class Background_getting_students_here extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_students.php?batch_subject="+ Teacher_navigation.batch_subject+"&batch_class="+Teacher_navigation.batch_class+"&batch_number="+Teacher_navigation.batch_number;

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();




            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                name=new String[size];
                names=new ArrayList<String>();
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    name[count]=JO.getString("username");
                    names.add(name[count]);

                    count++;


                }
                arrayList = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, names);
                arrayList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sub.setAdapter(arrayList);









            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(JSON_STRING);
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String json_string;
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }


    class Background_getting_test_here extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/teacher/getting_test.php?batch_subject="+Teacher_navigation.batch_subject+"&batch_class="+Teacher_navigation.batch_class+"&batch_number="+Teacher_navigation.batch_number+"&test_name="+test_name;

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            //Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();




            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                username=new String[size];
                marks=new String[size];

                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    username[count]=JO.getString("username");
                    marks[count]=JO.getString("marks_obtained");

                    count++;


                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(Test_Record.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapter=new Test_record_adapter(Test_Record.this);
                recyclerView.setAdapter(adapter);
                adapter.swapCursor(context,username,marks);







            } catch (JSONException e) {
                e.printStackTrace();
            }


            super.onPostExecute(JSON_STRING);
        }



        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String json_string;
            try {
                URL url=new URL(json_url);
                HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder=new StringBuilder();
                while((json_string=bufferedReader.readLine())!=null)
                {
                    stringBuilder.append(json_string+"\n");

                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }
}
