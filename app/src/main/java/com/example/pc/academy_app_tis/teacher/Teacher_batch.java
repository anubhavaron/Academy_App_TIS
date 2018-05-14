package com.example.pc.academy_app_tis.teacher;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pc.academy_app_tis.MainActivity;
import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.Background_add_batch;
import com.example.pc.academy_app_tis.head.Head_Batch;
import com.example.pc.academy_app_tis.head.Head_Batch_Adapter;
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

public class Teacher_batch extends AppCompatActivity implements Head_Batch_Adapter.Head_Batch_AdapterOnClickHandler {


    RecyclerView recyclerView;
    Head_Batch_Adapter adapter;
    String batch_subject[];
    String batch_class[];
    String batch_number[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_batch);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_21);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Head_Batch_Adapter(Teacher_batch.this);



        recyclerView.setAdapter(adapter);
        Teacher_batch.Background_batch_details background_batch_details=new Teacher_batch.Background_batch_details();
        background_batch_details.execute();
    }













    @Override
    public void onClick(int x) {
        Toast.makeText(Teacher_batch.this,x+"",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(Teacher_batch.this,Teacher_navigation.class);
        intent.putExtra("batch_subject", batch_subject[x]);
        intent.putExtra("batch_class", batch_class[x]);
        intent.putExtra("batch_number", batch_number[x]);

        startActivity(intent);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.head_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();

            editor.putBoolean("is", false);

            editor.apply();
            Intent i=new Intent(Teacher_batch.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
