package com.example.pc.academy_app_tis.teacher;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class Teachers_students extends AppCompatActivity implements  Students_details_adapter.Students_details_adapterOnClickHandler {
    Students_details_adapter adapter;
    RecyclerView recyclerView;
    String name[];
    String photoname[];
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_students);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_22);



        context=getApplicationContext();
        Background_getting_students background_getting_students=new Background_getting_students();
        background_getting_students.execute();
    }

    @Override
    public void onClick(int x) {

    }

    class Background_getting_students extends AsyncTask<Void,Void,String>
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
           // Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();


            if(JSON_STRING!=null) {

                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    int count = 0;


                    jsonArray = jsonObject.getJSONArray("server response");
                    int size = jsonArray.length();
                    name = new String[size];
                    photoname=new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        photoname[count] = JO.getString("username");
                        name[count]=JO.getString("batch_subject");

                        count++;


                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(Teachers_students.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new Students_details_adapter(Teachers_students.this);
                    recyclerView.setAdapter(adapter);
                    adapter.swapCursor(context, name,photoname);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Toast.makeText(Teachers_students.this,"No Internet",Toast.LENGTH_SHORT).show();
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
