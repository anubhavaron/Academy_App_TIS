package com.TIS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class You_Tube_links extends AppCompatActivity implements you_tube_adapter.you_tube_adapterOnClickHandler {
    RecyclerView recyclerView;
    String[] links;
    you_tube_adapter adapter;
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you__tube_links);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_110);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_110);
       // new Background_getting_links().execute();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new you_tube_adapter(You_Tube_links.this);
        recyclerView.setAdapter(adapter);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
       // Toast.makeText(You_Tube_links.this,pref.getString("username", null),Toast.LENGTH_SHORT).show();

        if(pref.getString("h_t_s", null).equals("Head"))
        {
            floatingActionButton.setVisibility(View.VISIBLE);

        }
        else
        {
            floatingActionButton.setVisibility(View.GONE);

        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(You_Tube_links.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_links,null);
                final EditText name=(EditText)mView.findViewById(R.id.name_112);
                final EditText link=(EditText)mView.findViewById(R.id.link_112);

                final Button add=(Button)mView.findViewById(R.id.add_112);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);
                        add.setVisibility(View.GONE);
                        ParseObject gameScore = new ParseObject("youtube_table");
                        gameScore.put("name", name.getText().toString());
                        gameScore.put("links", link.getText().toString());
                        //gameScore.put("batch_number", n.getText().toString());
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(You_Tube_links.this, "Item saved!", Toast.LENGTH_SHORT).show();
                                Background_youtube_links();
                            }
                        });
                       // new Background_getting_links().execute();

                   /*     Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                        background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());*/


                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });
        Background_youtube_links();

    }

    void Background_youtube_links()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("youtube_table");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(You_Tube_links.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {


                        int count = 0;


                       // jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        String[] name = new String[size];
                        links = new String[size];
                        while (count < size) {
                            //getJSONObject(count);
                            name[count] = scoreList.get(count).getString("name");
                            links[count] = scoreList.get(count).getString("links");

                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), name);

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(You_Tube_links.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });






    }

    @Override
    public void onClick(int x) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(links[links.length-x-1])));
    }


    /*class Background_getting_links extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/you.php";

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
                    String[] name = new String[size];
                    links = new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        name[count] = JO.getString("name");
                        links[count] = JO.getString("link");

                        count++;


                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new you_tube_adapter(You_Tube_links.this);
                    recyclerView.setAdapter(adapter);
                    adapter.swapCursor(getApplicationContext(), name);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(You_Tube_links.this,"No Internet",Toast.LENGTH_SHORT).show();
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
    }*/
}
