package com.example.pc.academy_app_tis.head;

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

public class Head_Batch extends AppCompatActivity implements Head_Batch_Adapter.Head_Batch_AdapterOnClickHandler {
    RecyclerView recyclerView;
    Head_Batch_Adapter adapter;
    String batch_subject[];
    String batch_class[];
    String batch_number[];
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head__batch);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_3);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Head_Batch_Adapter(Head_Batch.this);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_3);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Head_Batch.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_batch_adding,null);
                final EditText sub=(EditText)mView.findViewById(R.id.subject_5);
                final EditText cl=(EditText)mView.findViewById(R.id.class_5);
                final EditText n=(EditText)mView.findViewById(R.id.batch_5);
                final Button add=(Button)mView.findViewById(R.id.add_5);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);

                        Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                        background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());
                        Background_batch_details background_batch_details=new Background_batch_details();
                        background_batch_details.execute();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });


        recyclerView.setAdapter(adapter);
        Background_batch_details background_batch_details=new Background_batch_details();
        background_batch_details.execute();

    }

    @Override
    public void onClick(int x) {
        //Toast.makeText(Head_Batch.this,x+"",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(Head_Batch.this,head_navigation.class);
        intent.putExtra("batch_subject", batch_subject[x]);
        intent.putExtra("batch_class", batch_class[x]);
        intent.putExtra("batch_number", batch_number[x]);

        startActivity(intent);


    }

    class Background_batch_details extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/batches_details.php";

        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {
                JSONObject  jsonObject;
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
