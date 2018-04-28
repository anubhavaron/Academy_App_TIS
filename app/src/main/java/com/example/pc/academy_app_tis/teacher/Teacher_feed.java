package com.example.pc.academy_app_tis.teacher;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.example.pc.academy_app_tis.head.teachers_profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Teacher_feed extends AppCompatActivity {
    Switch aSwitch;
    EditText title;
    EditText message;
    Button button;
    ImageView imageView;
    Bitmap bitmap;
    FloatingActionButton floatingActionButton;
    private final int IMG_REQUEST=1;
    String UploadUrl="https://tisabcd12.000webhostapp.com/teacher/adding_image_feed.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_feed);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_41);
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
                    if(flag[0]==false)
                    {
                        Background_feed_not_image background_feed_not_image=new Background_feed_not_image(Teacher_feed.this);
                        background_feed_not_image.execute(Teacher_navigation.batch_subject,Teacher_navigation.batch_class,Teacher_navigation.batch_number,title.getText().toString(),message.getText().toString());

                    }
                    else
                    {
                            uploadImage();
                    }

                   /*     Background_add_batch background_task_add_batches=new Background_add_batch(Head_Batch.this);

                        background_task_add_batches.execute(sub.getText().toString(),cl.getText().toString(),n.getText().toString());*/


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
                imageView.setImageBitmap(bitmap);

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
                            Toast.makeText(Teacher_feed.this,Response,Toast.LENGTH_SHORT).show();

                           /* Intent i=new Intent(teachers_profile.this,teachers_profile.class);
                            startActivity(i);*/



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(Teacher_feed.this,"Error",Toast.LENGTH_SHORT).show();
            }
        })

        {
            @Override
            protected Map<String , String> getParams() throws AuthFailureError
            {
                Map<String,String> params=new HashMap<>();
                params.put("batch_subject",Teacher_navigation.batch_subject);
                params.put("batch_class",Teacher_navigation.batch_class);
                params.put("batch_number",Teacher_navigation.batch_number);
                params.put("title",title.getText().toString());
                params.put("message",message.getText().toString());

                params.put("image",imageToSTring(bitmap));
                return params;


            }
        };
        MySingleton.getInstance(Teacher_feed.this).addToRequestQueue(stringRequest);
    }


    private String imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }

}
