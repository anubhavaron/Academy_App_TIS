package com.example.pc.academy_app_tis.student;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.You_Tube_links;
import com.example.pc.academy_app_tis.teacher.Feed_Adapter;
import com.example.pc.academy_app_tis.teacher.Teacher_feed;
import com.example.pc.academy_app_tis.teacher.Teacher_navigation;
import com.example.pc.academy_app_tis.you_tube_adapter;
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

public class students_feed extends AppCompatActivity implements  Feed_Adapter.Feed_AdapterOnClickHandler {
    RecyclerView recyclerView;
    Feed_Adapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_students_feed);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_101);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Feed_Adapter(students_feed.this);
        recyclerView.setAdapter(adapter);

        getting_feed();


    }

    @Override
    public void onClick(int x) {

    }

    void getting_feed()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("feed_table");
        query.whereEqualTo("batch_subject",student_batch.subject_s);
        query.whereEqualTo("batch_class",student_batch.class_s);
        query.whereEqualTo("batch_number",student_batch.number_s);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(students_feed.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count = 0;


                        int size=scoreList.size();
                       String[] title_array = new String[size];
                       String[] message_Array=new String[size];
                       String[] fwion_array=new String[size];
                        ArrayList<ParseFile> parseFiles = new ArrayList<ParseFile>(size);
                        // file=new ArrayList<ParseFile>(size);
                        while (count < scoreList.size()) {
                            //JSONObject JO = jsonArray.getJSONObject(count);
                            title_array[count] = scoreList.get(count).getString("title");
                            message_Array[count] = scoreList.get(count).getString("message");
                            fwion_array[count] = scoreList.get(count).getString("fwion");
                            // description[count] = scoreList.get(count).getString("description");

                            parseFiles.add(count, scoreList.get(count).getParseFile("image"));

                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), title_array, message_Array,fwion_array,parseFiles);


                        // Toast.makeText(Teacher_feed.this,scoreList.size()+"",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(students_feed.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });





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
                  //  adapter.swapCursor(getApplicationContext(), title_array, message_Array, fwion_array);


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
