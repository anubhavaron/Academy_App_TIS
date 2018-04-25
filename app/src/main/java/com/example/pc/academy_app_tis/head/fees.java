package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class fees extends AppCompatActivity implements fees_adapter.fees_adapterOnClickHandler{
    String batch_subject;
    String batch_class;
    String batch_number;
    TextView sub;
    TextView cla;
    TextView num;
    String name[];
    Button button;
    Context context;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> arrayList;
    ArrayList<String> names;
    String start_date[];
    String end_date[];
    String amount[];
    RecyclerView recyclerView;
    fees_adapter f_adapter;
    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fees);


        batch_subject=head_navigation.batch_subject;
        batch_class=head_navigation.batch_class;
        batch_number=head_navigation.batch_number;


        sub=(TextView)findViewById(R.id.subject_12);
        cla=(TextView)findViewById(R.id.class_12);
        num=(TextView)findViewById(R.id.number_12);
        button=(Button)findViewById(R.id.previous_fees_12);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_12);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_12);




        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        f_adapter=new fees_adapter(fees.this);
        recyclerView.setAdapter(f_adapter);

        autoCompleteTextView=(AutoCompleteTextView)findViewById(R.id.student_name_12);
        sub.setText(batch_subject);
        cla.setText(batch_class);
        num.setText(batch_number);
        context=fees.this;
        Background_getting_students background_getting_students=new Background_getting_students();
        background_getting_students.execute();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Background_getting_previousfees background_getting_previousfees=new Background_getting_previousfees();
                background_getting_previousfees.execute();
            }
        });



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(fees.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_adding_fees,null);
                final EditText sub=(EditText)mView.findViewById(R.id.start_date_14);
                final EditText cl=(EditText)mView.findViewById(R.id.end_date_14);
                final EditText n=(EditText)mView.findViewById(R.id.amount_14);
                final Button add=(Button)mView.findViewById(R.id.add_14);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);

                       /* Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                        background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());


                        */
                        Background_getting_previousfees background_getting_previousfees=new Background_getting_previousfees();
                        background_getting_previousfees.execute();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();




            }
        });







    }

    @Override
    public void onClick(int x) {

    }


    class Background_getting_previousfees extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_fees.php?batch_subject="+head_navigation.batch_subject+"&batch_class="+head_navigation.batch_class+"&batch_number="+head_navigation.batch_number+"&username="+autoCompleteTextView.getText().toString();

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
                    start_date[count]=JO.getString("start_date");
                    end_date[count]=JO.getString("end_datet");
                    amount[count]=JO.getString("amount");


                    count++;


                }

                Toast.makeText(getApplicationContext(),count+"         hello",Toast.LENGTH_LONG).show();
                f_adapter.swapCursor(getApplicationContext(),start_date,end_date,amount);

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




    class Background_getting_students extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_students.php?batch_subject="+head_navigation.batch_subject+"&batch_class="+head_navigation.batch_class+"&batch_number="+head_navigation.batch_number;

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




            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;
                names=new ArrayList<String>();


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                name=new String[size];
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    name[count]=JO.getString("username");
                    names.add(name[count]);

                    count++;


                }


                arrayList = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, names);
                arrayList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView.setAdapter(arrayList);








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
