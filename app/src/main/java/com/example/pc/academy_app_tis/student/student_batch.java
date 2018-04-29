package com.example.pc.academy_app_tis.student;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.Head_Batch;
import com.example.pc.academy_app_tis.head.Head_Batch_Adapter;

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

        recyclerView=(RecyclerView)findViewById(R.id.recycler_51);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Batch_Adapter(student_batch.this);
        recyclerView.setAdapter(adapter);
        Background_batch_from_id background_batch_from_id=new Background_batch_from_id();
        background_batch_from_id.execute();
    }

    @Override
    public void onClick(int x) {
        subject_s=batch_subject[x];
        class_s=batch_class[x];
        number_s=batch_number[x];
        Intent i=new Intent(student_batch.this,student_fees_activity.class);
        startActivity(i);

    }


    public class Background_batch_from_id extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/student/getting_batches.php?username="+Student_Navigation.username;

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {
            JSONObject jsonObject;
            JSONArray jsonArray;




            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                batch_subject=new String[size];
                batch_class=new String[size];
                batch_number=new String[size];
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    batch_subject[count]=JO.getString("batch_subject");
                    batch_class[count]=JO.getString("batch_class");
                    batch_number[count]=JO.getString("batch_number");


                    count++;


                }
                if(batch_class!=null)
                {
                    adapter.swapCursor(getApplicationContext(),batch_subject,batch_class,batch_number);
                    Toast.makeText(getApplicationContext(),"Click to see fees",Toast.LENGTH_SHORT).show();
                }






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
