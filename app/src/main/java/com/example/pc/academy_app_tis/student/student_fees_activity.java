package com.example.pc.academy_app_tis.student;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
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

public class student_fees_activity extends AppCompatActivity implements com.example.pc.academy_app_tis.head.fees_adapter.fees_adapterOnClickHandler {
    String start_date[];
    String end_date[];
    String amount[];
    RecyclerView recyclerView;
    String username;
    com.example.pc.academy_app_tis.head.fees_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_fees_activity);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_53);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //Toast.makeText(student_fees_activity.this,pref.getString("username", null),Toast.LENGTH_SHORT).show();

        username=pref.getString("username", null);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new com.example.pc.academy_app_tis.head.fees_adapter(student_fees_activity.this);
        recyclerView.setAdapter(adapter);

        new Background_getting_fees().execute();

    }

    @Override
    public void onClick(int x) {

    }


    class Background_getting_fees extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/student/getting_fees.php?username="+username;

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {

            JSONObject jsonObject;
            JSONArray jsonArray;
             Toast.makeText(getApplicationContext(),Student_Navigation.username+ student_batch.subject_s+student_batch.class_s+student_batch.number_s,Toast.LENGTH_LONG).show();




            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;



                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                start_date=new String[size];
                end_date=new String[size];
                amount=new String[size];
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    start_date[count]=JO.getString("batch_subject");
                    end_date[count]=JO.getString("start_date");
                    amount[count]=JO.getString("end_date");


                    count++;


                }

                Toast.makeText(getApplicationContext(),count+"         hello",Toast.LENGTH_LONG).show();
                adapter.swapCursor(getApplicationContext(),start_date,end_date,amount);

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
