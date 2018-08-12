package com.example.pc.academy_app_tis.teacher;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.pc.academy_app_tis.MySingleton;
import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.Students_details_adapter;
import com.example.pc.academy_app_tis.head.Wall_of_fame_head;
import com.example.pc.academy_app_tis.head.head_navigation;
import com.example.pc.academy_app_tis.head.teachers_profile;
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

public class Teacher_feed extends AppCompatActivity implements  Feed_Adapter.Feed_AdapterOnClickHandler {
    Switch aSwitch;
    EditText title;
    EditText message;
    Button button;
    ImageView imageView;
    Bitmap bitmap;
    String title_array[];
    String message_Array[];
    String fwion_array[];
    Feed_Adapter adapter;
    FloatingActionButton floatingActionButton;
    RecyclerView recyclerView;
    private final int IMG_REQUEST=1;
    String UploadUrl="https://tisabcd12.000webhostapp.com/teacher/adding_image_feed.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feed);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_41);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_41);


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();



        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Teacher_feed.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_teacher_feed,null);
                 title=(EditText)mView.findViewById(R.id.title_42);
                message=(EditText)mView.findViewById(R.id.message_42);
                imageView=(ImageView)mView.findViewById(R.id.imageview_42);
                aSwitch=(Switch)mView.findViewById(R.id.switch42);
                button=(Button)mView.findViewById(R.id.add_42);
                final boolean[] flag = {false};
                aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked==true)
                        {
                            imageView.setVisibility(View.VISIBLE);
                            flag[0] =true;

                        }
                        else
                        {
                            imageView.setVisibility(View.GONE);
                            flag[0]=false;


                        }
                        // do something, the isChecked will be
                        // true if the switch is in the On position
                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectImage();
                    }
                });

                final Button add=(Button)mView.findViewById(R.id.add_42);
                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        add.setEnabled(false);
                        add.setVisibility(View.GONE);




                        ParseObject gameScore = new ParseObject("feed_table");
                        if(flag[0]==false)          //withouot image
                        {   Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.p6);
                            byte[] data = imageToSTring(bm);
                            ParseFile file = new ParseFile("image", data);
                            gameScore.put("image", file);
                                gameScore.put("fwion","NO");

                        }
                        else            //with image
                        {
                            byte[] data = imageToSTring(bitmap);
                            ParseFile file = new ParseFile("image", data);
                            gameScore.put("image", file);
                                gameScore.put("fwion","YES");

                        }

                        gameScore.put("message", message.getText().toString().trim());
                        gameScore.put("title", title.getText().toString().trim());
                         gameScore.put("batch_number", Teacher_navigation.batch_number);
                        gameScore.put("batch_subject", Teacher_navigation.batch_subject);
                        gameScore.put("batch_class",Teacher_navigation.batch_class);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(Teacher_feed.this, "Item saved!", Toast.LENGTH_SHORT).show();
                                        getting_feed();
                            }
                        });



                    }
                });
                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(Teacher_feed.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new Feed_Adapter(Teacher_feed.this);
        recyclerView.setAdapter(adapter);


            getting_feed();

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
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }



    private byte[] imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
       // return Base64.encodeToString(imgBytes,Base64.DEFAULT);
        return imgBytes;
    }

    @Override
    public void onClick(int x) {

    }



    void getting_feed()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("feed_table");
        query.whereEqualTo("batch_subject",Teacher_navigation.batch_subject);
        query.whereEqualTo("batch_class",Teacher_navigation.batch_class);
        query.whereEqualTo("batch_number",Teacher_navigation.batch_number);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Teacher_feed.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count = 0;


                        int size=scoreList.size();
                        title_array = new String[size];
                        message_Array=new String[size];
                        fwion_array=new String[size];
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
                    Toast.makeText(Teacher_feed.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });





    }






}
