package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.academy_app_tis.MySingleton;
import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.You_Tube_links;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class head_navigation extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,Students_details_adapter.Students_details_adapterOnClickHandler {

    String name[];
    static String batch_subject;
    static String batch_class;
    static String batch_number;
    String photoname[];
    RecyclerView recyclerView;
    Context context;
    Students_details_adapter adapter;
    TextView t_name;
    TextView t_description;
    ImageView t_imageView;
    Button t_button;
    private final int IMG_REQUEST=1;
    Bitmap bitmap;
    String username_s;
    private String UploadUrl="https://tisabcd12.000webhostapp.com/head/adding_login_students.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_15);


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

     //   Toast.makeText(head_navigation.this,batch_subject+batch_class+batch_number,Toast.LENGTH_LONG).show();
        recyclerView=(RecyclerView)findViewById(R.id.recycler_6);


        Background_getting_students background_getting_students=new Background_getting_students();
        background_getting_students.execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //       .setAction("Action", null).show();





                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(head_navigation.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_add_student,null);
                t_name=(TextView)mView.findViewById(R.id.name_16);
                t_description=(TextView)mView.findViewById(R.id.number_16);
                t_imageView=(ImageView)mView.findViewById(R.id.imageView_16);
                t_button=(Button) mView.findViewById(R.id.button_16);
                t_imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });
                t_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        t_button.setEnabled(false);
                        t_button.setVisibility(View.GONE);


                        String name_s=t_name.getText().toString();
                        String number_s=t_description.getText().toString();
                      username_s=name_s+number_s.substring(number_s.length()-2,number_s.length());
                        Toast.makeText(head_navigation.this,username_s,Toast.LENGTH_LONG).show();

                        Background_adding_into_id_batch_table background_adding_into_id_batch_table=new Background_adding_into_id_batch_table(head_navigation.this);
                        background_adding_into_id_batch_table.execute(head_navigation.batch_subject,head_navigation.batch_class,head_navigation.batch_number,username_s);
                        Background_getting_students_info background_getting_students_info=new Background_getting_students_info();
                        background_getting_students_info.execute();
                        Background_getting_students background_getting_students=new Background_getting_students();
                        background_getting_students.execute();
                        //  uploadImage();
                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();



            }
        });

























    }



    private void selectImage()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==IMG_REQUEST)&&(resultCode==RESULT_OK)&&(data!=null))
        {
            Uri path=data.getData();
            try {
                bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),path);
                t_imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

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

        }
        else
            if(id==R.id.wall_of_fame)
            {
                Intent i=new Intent(head_navigation.this,Wall_of_fame_head.class);
                startActivity(i);
            }
            else
            if(id==R.id.You_tube)
            {
                Intent i=new Intent(head_navigation.this,You_Tube_links.class);
                startActivity(i);
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
                    LinearLayoutManager layoutManager = new LinearLayoutManager(head_navigation.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new Students_details_adapter(head_navigation.this);
                    recyclerView.setAdapter(adapter);
                    adapter.swapCursor(context, name,photoname);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Toast.makeText(head_navigation.this,"No Internet",Toast.LENGTH_SHORT).show();
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

    class Background_getting_students_info extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_info_of_student.php?username="+username_s;

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

            if (JSON_STRING != null) {


                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    int count = 0;


                    jsonArray = jsonObject.getJSONArray("server response");
                    int size = jsonArray.length();
                    if (size > 0) {
                        Toast.makeText(getApplicationContext(), "YES", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "No", Toast.LENGTH_LONG).show();
                        uploadImage();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else
            {
                Toast.makeText(head_navigation.this,"No Internet",Toast.LENGTH_SHORT).show();
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


    private void uploadImage()
    {



        StringRequest stringRequest =new StringRequest(Request.Method.POST,UploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String Response=jsonObject.getString("response");
                            Toast.makeText(head_navigation.this,Response,Toast.LENGTH_SHORT).show();
                            Background_getting_students background_getting_students=new Background_getting_students();
                            background_getting_students.execute();

                           /* Intent i=new Intent(teachers_profile.this,teachers_profile.class);
                            startActivity(i);*/



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(head_navigation.this,"Error",Toast.LENGTH_SHORT).show();
                Background_getting_students background_getting_students=new Background_getting_students();
                background_getting_students.execute();
            }
        })

        {
            @Override
            protected Map<String , String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<>();
                params.put("name",username_s);


                params.put("image",imageToSTring(bitmap));
                return params;


            }
        };
        MySingleton.getInstance(head_navigation.this).addToRequestQueue(stringRequest);
    }


    private String imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }



}
