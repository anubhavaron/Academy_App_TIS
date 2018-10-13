package com.TIS.teacher;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.TIS.R;
import com.TIS.head.Students_details_adapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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
import java.util.List;

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


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();




        context=getApplicationContext();
        LinearLayoutManager layoutManager = new LinearLayoutManager(Teachers_students.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Students_details_adapter(Teachers_students.this);
        recyclerView.setAdapter(adapter);
        Background_students_details();

    }

    @Override
    public void onClick(int x) {

    }


    void Background_students_details()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("login_table");
        query.whereEqualTo("batch_subject",Teacher_navigation.batch_subject);
        query.whereEqualTo("batch_class",Teacher_navigation.batch_class);
        query.whereEqualTo("batch_number",Teacher_navigation.batch_number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Teachers_students.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count = 0;


                        //  jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        name = new String[size];
                        ArrayList<ParseFile> parseFiles = new ArrayList<ParseFile>(size);
                        // file=new ArrayList<ParseFile>(size);
                        while (count < scoreList.size()) {
                            //JSONObject JO = jsonArray.getJSONObject(count);
                            name[count] = scoreList.get(count).getString("username");
                            // description[count] = scoreList.get(count).getString("description");
                            parseFiles.add(count,scoreList.get(count).getParseFile("image"));
                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), name, parseFiles);

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(Teachers_students.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });


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
                    //adapter.swapCursor(context, name,photoname);


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
