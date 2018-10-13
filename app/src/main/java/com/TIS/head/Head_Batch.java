package com.TIS.head;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.TIS.MainActivity;
import com.TIS.R;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.List;

public class Head_Batch extends AppCompatActivity implements Head_Batch_Adapter.Head_Batch_AdapterOnClickHandler {
    RecyclerView recyclerView;
    Head_Batch_Adapter adapter;
    int backpress=0;
    String batch_subject[];
    String batch_class[];
    String batch_number[];
    FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head__batch);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

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
                        add.setVisibility(View.GONE);
                        ParseObject gameScore = new ParseObject("batch_table");
                        gameScore.put("batch_subject", sub.getText().toString());
                        gameScore.put("batch_class", cl.getText().toString());
                        gameScore.put("batch_number", n.getText().toString());
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(Head_Batch.this, "Item saved!", Toast.LENGTH_SHORT).show();
                                Background_details_of_batch();
                            }
                        });

                        //Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                   //     background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());
                       /* Background_batch_details background_batch_details=new Background_batch_details();
                        background_batch_details.execute();*/
                        Background_details_of_batch();

                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });


        recyclerView.setAdapter(adapter);
        //Background_batch_details background_batch_details=new Background_batch_details();
        //background_batch_details.execute();

        Background_details_of_batch();

    }


    void Background_details_of_batch()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("batch_table");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Head_Batch.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count=0;

                        int size = scoreList.size();
                        batch_subject = new String[size];
                        batch_class = new String[size];
                        batch_number = new String[size];
                        while (count < size) {

                            batch_subject[count] = scoreList.get(count).getString("batch_subject");
                            batch_class[count] = scoreList.get(count).getString("batch_class");
                            batch_number[count] = scoreList.get(count).getString("batch_number");


                            count++;


                        }
                        if (batch_class != null) {
                            adapter.swapCursor(getApplicationContext(), batch_subject, batch_class, batch_number);
                        }

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(Head_Batch.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });



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
            Intent i=new Intent(Head_Batch.this, MainActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed(){
        backpress = (backpress + 1);
        if(backpress==1) {
            Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        }
        if (backpress>1) {
            this.finish();
        }
    }

    @Override
    public void onClick(int x) {
        //Toast.makeText(Head_Batch.this,x+"",Toast.LENGTH_LONG).show();
        Intent intent=new Intent(Head_Batch.this,head_navigation.class);
        intent.putExtra("batch_subject", batch_subject[x]);
        intent.putExtra("batch_class", batch_class[x]);
        intent.putExtra("batch_number", batch_number[x]);
        Toast.makeText(Head_Batch.this,batch_class[x]+" "+batch_subject[x]+" "+batch_number[x]+"",Toast.LENGTH_SHORT).show();

        startActivity(intent);


    }
    /*
    public class Background_batch_details extends AsyncTask<Void,Void,String>
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

            if(JSON_STRING!=null) {


                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    int count = 0;


                    jsonArray = jsonObject.getJSONArray("server response");
                    int size = jsonArray.length();
                    batch_subject = new String[size];
                    batch_class = new String[size];
                    batch_number = new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        batch_subject[count] = JO.getString("batch_subject");
                        batch_class[count] = JO.getString("batch_class");
                        batch_number[count] = JO.getString("batch_number");


                        count++;


                    }
                    if (batch_class != null) {
                        adapter.swapCursor(getApplicationContext(), batch_subject, batch_class, batch_number);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(Head_Batch.this,"No Internet",Toast.LENGTH_SHORT).show();
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
