package com.TIS.head;

import android.content.Intent;
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
import android.util.Base64;
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

public class Wall_of_fame_head extends AppCompatActivity implements  Wall_of_fame_Adapter.Wall_of_fame_AdapterOnClickHandler {
FloatingActionButton floatingActionButton;
TextView t_name;
TextView t_description;
ImageView t_imageView;
Button t_button;

String title[];
String description[];
ArrayList<ParseFile> file;

Wall_of_fame_Adapter adapter;
RecyclerView recyclerView;

    private final int IMG_REQUEST=1;
    String UploadUrl="https://tisabcd12.000webhostapp.com/head/adding_wall_fame.php";
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_of_fame_head);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();












        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_23);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_23);













        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(Wall_of_fame_head.this);
                View mView=getLayoutInflater().inflate(R.layout.dialog_wall_of_fame,null);
                t_name=(TextView)mView.findViewById(R.id.name_24);
                t_description=(TextView)mView.findViewById(R.id.description_24);
                t_imageView=(ImageView)mView.findViewById(R.id.imageView_24);
                t_button=(Button) mView.findViewById(R.id.button_24);
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

                        byte[] data = imageToString(bitmap);
                        ParseFile file = new ParseFile("image", data);

                        ParseObject gameScore = new ParseObject("wall_of_fame_table");
                        gameScore.put("image", file);
                        gameScore.put("description", t_description.getText().toString().trim());
                        gameScore.put("title", t_name.getText().toString().trim());
                       // gameScore.put("batch_number", n.getText().toString());
                        //gameScore.put("subject_class_number", sub.getText().toString()+"_"+);
                        gameScore.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Toast.makeText(Wall_of_fame_head.this, "Item saved!", Toast.LENGTH_SHORT).show();
                                Background_wall_of_fame_parse();
                               // Background_details_of_batch();
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

        LinearLayoutManager layoutManager=new LinearLayoutManager(Wall_of_fame_head.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Wall_of_fame_Adapter(Wall_of_fame_head.this);
        recyclerView.setAdapter(adapter);
       // Background_getting_wall_fame background_getting_wall_fame=new Background_getting_wall_fame();
        //background_getting_wall_fame.execute();
        Background_wall_of_fame_parse();


    }


    void Background_wall_of_fame_parse()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("wall_of_fame_table");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Wall_of_fame_head.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                    }
                    else {
                        int count = 0;


                      //  jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        title = new String[size];
                        description = new String[size];
                        file=new ArrayList<ParseFile>(size);
                        while (count < scoreList.size()) {
                            //JSONObject JO = jsonArray.getJSONObject(count);
                            title[count] = scoreList.get(count).getString("title");
                            description[count] = scoreList.get(count).getString("description");
                            file.add(count,scoreList.get(count).getParseFile("image"));
                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), title, description,file);

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(Wall_of_fame_head.this,"Connection_problem",Toast.LENGTH_LONG).show();
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




/*

    private void uploadImage()
    {



        StringRequest stringRequest =new StringRequest(Request.Method.POST,UploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String Response=jsonObject.getString("response");
                            Toast.makeText(Wall_of_fame_head.this,Response,Toast.LENGTH_SHORT).show();
                            Background_getting_wall_fame background_getting_wall_fame=new Background_getting_wall_fame();
                            background_getting_wall_fame.execute();

                            Intent i=new Intent(teachers_profile.this,teachers_profile.class);
                            startActivity(i);



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Wall_of_fame_head.this,"Error",Toast.LENGTH_SHORT).show();
            }
        })

        {
            Override
            protected Map<String , String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<>();
                params.put("name",t_name.getText().toString().trim());
                params.put("description",t_description.getText().toString().trim());

                params.put("image",imageToSTring(bitmap));
                return params;


            }
        };
        MySingleton.getInstance(Wall_of_fame_head.this).addToRequestQueue(stringRequest);
    }*/


    private String imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }
    private byte[] imageToString(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return imgBytes;
       // return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }

    @Override
    public void onClick(int x) {

    }

    class Background_getting_wall_fame extends AsyncTask<Void,Void,String>
    {   String json_url="https://tisabcd12.000webhostapp.com/head/getting_wall_fame.php";

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
                    title = new String[size];
                    description = new String[size];
                    while (count < jsonArray.length()) {
                        JSONObject JO = jsonArray.getJSONObject(count);
                        title[count] = JO.getString("title");
                        description[count] = JO.getString("description");

                        count++;


                    }

                  //  adapter.swapCursor(getApplicationContext(), title, description);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                super.onPostExecute(JSON_STRING);

            }
            else {
                Toast.makeText(Wall_of_fame_head.this,"No Internet",Toast.LENGTH_SHORT).show();
            }
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
