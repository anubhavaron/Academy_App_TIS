package com.example.pc.academy_app_tis.student;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.teacher.Feed_Adapter;
import com.example.pc.academy_app_tis.teacher.Teacher_feed;
import com.example.pc.academy_app_tis.teacher.Teacher_navigation;

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

public class students_feed extends AppCompatActivity implements  Feed_Adapter.Feed_AdapterOnClickHandler {
    RecyclerView recyclerView;
    Feed_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_feed);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_101);

        Background_getting_feed background_getting_feed=new Background_getting_feed();
        background_getting_feed.execute();
    }

    @Override
    public void onClick(int x) {

    }

    class Background_getting_feed extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/teacher/getting_feed.php?batch_subject="+ student_batch.subject_s+"&batch_class="+student_batch.class_s+"&batch_number="+student_batch.number_s;

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
                    String[] title_array = new String[size];
                    String[] message_Array = new String[size];
                    String[] fwion_array = new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        fwion_array[count] = JO.getString("fwion");
                        title_array[count] = JO.getString("title");
                        message_Array[count] = JO.getString("message");


                        count++;


                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(students_feed.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new Feed_Adapter(students_feed.this);
                    recyclerView.setAdapter(adapter);
                    adapter.swapCursor(getApplicationContext(), title_array, message_Array, fwion_array);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(students_feed.this,"No Internet",Toast.LENGTH_SHORT).show();
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
