package com.example.pc.academy_app_tis.head;

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
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.pc.academy_app_tis.student.student_test_records;

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
import java.util.HashMap;
import java.util.Map;

public class teachers_profile extends AppCompatActivity implements teachers_profile_adapter.teachers_profile_adapterOnClickHandler {
    String name[];
    String description[];
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
    private String UploadUrl="https://tisabcd12.000webhostapp.com/head/Adding_teacher.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers_profile);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_9);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_9);
        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        Toast.makeText(teachers_profile.this,pref.getString("h_t_s", null),Toast.LENGTH_SHORT).show();

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
                        uploadImage();
                    }
                });


                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();


            }
        });
        context=getApplicationContext();

        Background_getting_teachers background_getting_teachers=new Background_getting_teachers();
        background_getting_teachers.execute();
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






    private void uploadImage()
    {



        StringRequest stringRequest =new StringRequest(Request.Method.POST,UploadUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            String Response=jsonObject.getString("response");
                            Toast.makeText(teachers_profile.this,Response,Toast.LENGTH_SHORT).show();
                            Background_getting_teachers background_getting_teachers=new Background_getting_teachers();
                            background_getting_teachers.execute();
                           /* Intent i=new Intent(teachers_profile.this,teachers_profile.class);
                            startActivity(i);*/



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(teachers_profile.this,"Error",Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String , String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<>();
                params.put("name",t_name.getText().toString().trim());
                params.put("description",t_description.getText().toString().trim());

                params.put("image",imageToSTring(bitmap));
                return params;


            }
        };
        MySingleton.getInstance(teachers_profile.this).addToRequestQueue(stringRequest);
    }


    private String imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

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

           // Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();
            try {
                jsonObject=new JSONObject(JSON_STRING);
                int count=0;


                jsonArray=jsonObject.getJSONArray("server response");
                int size=jsonArray.length();
                name=new String[size];
                description=new String[size];
                while(count<jsonArray.length())
                {
                    JSONObject JO=jsonArray.getJSONObject(count);
                    name[count]=JO.getString("name");
                    description[count]=JO.getString("description");

                    count++;


                }
                LinearLayoutManager layoutManager=new LinearLayoutManager(teachers_profile.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setHasFixedSize(true);
                adapter=new teachers_profile_adapter(teachers_profile.this);
                recyclerView.setAdapter(adapter);
               adapter.swapCursor(context,name,description);
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

