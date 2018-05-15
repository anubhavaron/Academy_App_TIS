package com.example.pc.academy_app_tis.teacher;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
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

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.Background_add_batch;
import com.example.pc.academy_app_tis.head.Head_Batch;
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

public class Teacher_new_test extends AppCompatActivity implements  new_test_adapter.new_test_adapterOnClickHandler {
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
                        Background_new_test_add background_new_test_add=new Background_new_test_add(Teacher_new_test.this);
                        background_new_test_add.execute(Teacher_navigation.batch_subject,Teacher_navigation.batch_class,Teacher_navigation.batch_number,sub.getText().toString(),cl.getText().toString());
                        new Background_test_info().execute();

                   /*     Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                        background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());*/


                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });
        context=getApplicationContext();
        new Background_test_info().execute();
    }

    @Override
    public void onClick(int x) {

        Intent intent=new Intent(Teacher_new_test.this,Test_Record.class);
        intent.putExtra("test_name", test_name[x]);
        intent.putExtra("total_marks",test_marks[x]);

        startActivity(intent);

    }


    class Background_test_info extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/teacher/getting_new_test.php?batch_subject="+ Teacher_navigation.batch_subject+"&batch_class="+Teacher_navigation.batch_class+"&batch_number="+Teacher_navigation.batch_number;

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


            if(JSON_STRING!=null) {

                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    int count = 0;


                    jsonArray = jsonObject.getJSONArray("server response");
                    int size = jsonArray.length();
                    test_marks = new String[size];

                    test_name = new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        test_name[count] = JO.getString("test_name");
                        test_marks[count] = JO.getString("total_marks");

                        count++;


                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Teacher_new_test.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new new_test_adapter(Teacher_new_test.this);
                    recyclerView.setAdapter(adapter);
                    adapter.swapCursor(context, test_name, test_marks);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                Toast.makeText(Teacher_new_test.this,"No Internet",Toast.LENGTH_SHORT).show();
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
