package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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

public class head_navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Students_details_adapter.Students_details_adapterOnClickHandler {

    String name[];
    static String batch_subject;
    static String batch_class;
    static String batch_number;
    RecyclerView recyclerView;
    Context context;
    Students_details_adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        context=head_navigation.this;

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        batch_subject=getIntent().getStringExtra("batch_subject");
        batch_class=getIntent().getStringExtra("batch_class");
        batch_number=getIntent().getStringExtra("batch_number");

        Toast.makeText(head_navigation.this,batch_subject+batch_class+batch_number,Toast.LENGTH_LONG).show();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_6);


        Background_getting_students background_getting_students=new Background_getting_students();
        background_getting_students.execute();

























    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {

            Intent i=new Intent(head_navigation.this,teachers_profile.class);
            startActivity(i);


            // Handle the camera action
        } else if (id == R.id.nav_gallery) {
            Intent i=new Intent(head_navigation.this,fees.class);
            startActivity(i);

        } else if (id == R.id.nav_slideshow) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(int x) {

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


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                name=new String[size];
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    name[count]=JO.getString("username");

                    count++;


                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(head_navigation.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapter=new Students_details_adapter(head_navigation.this);
                recyclerView.setAdapter(adapter);
                adapter.swapCursor(context,name);







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
