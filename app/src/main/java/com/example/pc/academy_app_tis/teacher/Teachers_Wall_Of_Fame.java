package com.example.pc.academy_app_tis.teacher;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.Wall_of_fame_Adapter;
import com.example.pc.academy_app_tis.head.Wall_of_fame_head;

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

public class Teachers_Wall_Of_Fame extends AppCompatActivity implements Wall_of_fame_Adapter.Wall_of_fame_AdapterOnClickHandler {
    String title[];
    String description[];

    Wall_of_fame_Adapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers__wall__of__fame);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_28);
        LinearLayoutManager layoutManager=new LinearLayoutManager(Teachers_Wall_Of_Fame.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Wall_of_fame_Adapter(Teachers_Wall_Of_Fame.this);
        recyclerView.setAdapter(adapter);
        Background_getting_wall_fame background_getting_wall_fame=new Background_getting_wall_fame();
        background_getting_wall_fame.execute();

    }

    @Override
    public void onClick(int x) {

    }


    class Background_getting_wall_fame extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_wall_fame.php";

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
                    title = new String[size];
                    description = new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        title[count] = JO.getString("title");
                        description[count] = JO.getString("description");

                        count++;


                    }

                    //adapter.swapCursor(getApplicationContext(), title, description);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else
            {
                Toast.makeText(Teachers_Wall_Of_Fame.this,"No Internet",Toast.LENGTH_SHORT).show();
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
