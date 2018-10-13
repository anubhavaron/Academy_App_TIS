package com.TIS.head;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.TIS.R;
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
import java.util.List;

public class teachers_profile extends AppCompatActivity implements teachers_profile_adapter.teachers_profile_adapterOnClickHandler {
    String name[];
    String description[];
    String photoname[];
    RecyclerView recyclerView;
    teachers_profile_adapter adapter;
    Context context;
    FloatingActionButton floatingActionButton;
    private final int IMG_REQUEST=1;
    TextView t_name;
    TextView t_description;
    ImageView t_imageView;
    Button t_button;
    Bitmap bitmap;
    ArrayList<ParseFile> file;
    private String UploadUrl="https://tisabcd12.000webhostapp.com/head/Adding_teacher.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_profile);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_9);
      //  Glide.get(teachers_profile.this).clearMemory();
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_9);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        //Toast.makeText(teachers_profile.this,pref.getString("h_t_s", null),Toast.LENGTH_SHORT).show();

        if(pref.getString("h_t_s", null).equals("Head"))
        {
            floatingActionButton.setVisibility(View.VISIBLE);
        }
        else
            if(pref.getString("h_t_s", null).equals("Student"))
            {
                floatingActionButton.setVisibility(View.GONE);
            }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(teachers_profile.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_add_teacher,null);
                t_name=(TextView)mView.findViewById(R.id.name_10);
                t_description=(TextView)mView.findViewById(R.id.description_10);
                t_imageView=(ImageView)mView.findViewById(R.id.imageView_10);
                t_button=(Button) mView.findViewById(R.id.button_10);
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

                        byte[] data = imageToSTring(bitmap);
                        ParseFile file = new ParseFile("image", data);

                        ParseObject gameScore = new ParseObject("login_table");
                        gameScore.put("image", file);
                        gameScore.put("password",t_name.getText().toString().trim());
                        gameScore.put("username", t_name.getText().toString().trim());
                        gameScore.put("description",t_description.getText().toString().trim());
                        gameScore.put("h_t_p_s", "Teacher");
                        // gameScore.put("batch_number", n.getText().toString());
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(teachers_profile.this, "Item saved!", Toast.LENGTH_SHORT).show();
                              //  Background_wall_of_fame_parse();
                                // Background_details_of_batch();
                                Background_teachers();
                            }
                        });


                        //uploadImage();
                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();


            }
        });
        context=getApplicationContext();

       // Background_getting_teachers background_getting_teachers=new Background_getting_teachers();
        //background_getting_teachers.execute();
        Background_teachers();
    }


    void Background_teachers()
    {


        ParseQuery<ParseObject> query = ParseQuery.getQuery("login_table");
        query.whereEqualTo("h_t_p_s","Teacher");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(teachers_profile.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {

                        int count = 0;

                        int size = scoreList.size();
                        name = new String[size];
                        description = new String[size];
                        file=new ArrayList<ParseFile>(size);
                        //photoname=new String[size];
                        while (count < size) {

                            name[count] = scoreList.get(count).getString("username");
                            description[count] = scoreList.get(count).getString("description");
                            file.add(count,scoreList.get(count).getParseFile("image"));
                            count++;


                        }
                        LinearLayoutManager layoutManager = new LinearLayoutManager(teachers_profile.this);
                        recyclerView.setLayoutManager(layoutManager);
                        recyclerView.setHasFixedSize(true);
                        adapter = new teachers_profile_adapter(teachers_profile.this);
                        recyclerView.setAdapter(adapter);
                        adapter.swapCursor(context, name, description,file);

                        //  jsonArray = jsonObject.getJSONArray("server response");







                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(teachers_profile.this,"Connection_problem",Toast.LENGTH_LONG).show();
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









    private byte[] imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return imgBytes;

    }




    @Override
    public void onClick(int x) {

    }


    class Background_getting_teachers extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_teachers.php";
        @Override
        protected void onPreExecute() {
            //   Toast.makeText(login_signup.this,"Hey",Toast.LENGTH_SHORT).show();
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(String JSON_STRING) {
            JSONObject jsonObject;
            JSONArray jsonArray;
            if(JSON_STRING!=null) {
                // Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();
                try {
                    jsonObject = new JSONObject(JSON_STRING);
                    int count = 0;


                    jsonArray = jsonObject.getJSONArray("server response");
                    int size = jsonArray.length();
                    name = new String[size];
                    description = new String[size];
                    photoname=new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        name[count] = JO.getString("name");
                        description[count] = JO.getString("description");
                        photoname[count]=JO.getString("photoname");
                        count++;


                    }
                    LinearLayoutManager layoutManager = new LinearLayoutManager(teachers_profile.this);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setHasFixedSize(true);
                    adapter = new teachers_profile_adapter(teachers_profile.this);
                    recyclerView.setAdapter(adapter);
                    adapter.swapCursor(context, name, description,file);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                Toast.makeText(teachers_profile.this,"No Internet",Toast.LENGTH_SHORT).show();
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

