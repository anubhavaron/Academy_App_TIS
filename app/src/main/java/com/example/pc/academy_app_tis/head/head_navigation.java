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
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

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
import java.util.List;
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
    ArrayList<ParseFile> parseFiles;
    private String UploadUrl="https://tisabcd12.000webhostapp.com/head/adding_login_students.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head_navigation);


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


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

        recyclerView=(RecyclerView)findViewById(R.id.recycler_6);



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                        byte[] data = imageToSTring(bitmap);
                        ParseFile file = new ParseFile("image", data);

                        ParseObject gameScore = new ParseObject("login_table");
                        gameScore.put("image", file);
                        gameScore.put("username", username_s);
                        gameScore.put("password", username_s);
                        gameScore.put("h_t_p_s","Student");
                        gameScore.put("batch_subject",head_navigation.batch_subject);
                        gameScore.put("batch_class",head_navigation.batch_class);
                        gameScore.put("batch_number",head_navigation.batch_number);
                        // gameScore.put("batch_number", n.getText().toString());
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(head_navigation.this, "Item saved!", Toast.LENGTH_SHORT).show();
                               // Background_wall_of_fame_parse();
                                // Background_details_of_batch();
                                Background_students_details();
                            }
                        });


                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();



            }
        });




        LinearLayoutManager layoutManager=new LinearLayoutManager(head_navigation.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Students_details_adapter(head_navigation.this);
        recyclerView.setAdapter(adapter);
        Background_students_details();



    }


    void Background_students_details()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("login_table");
        query.whereEqualTo("batch_subject",head_navigation.batch_subject);
        query.whereEqualTo("batch_class",head_navigation.batch_class);
        query.whereEqualTo("batch_number",head_navigation.batch_number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(head_navigation.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count = 0;


                        //  jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        name = new String[size];
                        parseFiles = new ArrayList<ParseFile>(size);
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
                    Toast.makeText(head_navigation.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
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











    private byte[] imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        //return Base64.encodeToString(imgBytes,Base64.DEFAULT);
        return imgBytes;
    }



}
