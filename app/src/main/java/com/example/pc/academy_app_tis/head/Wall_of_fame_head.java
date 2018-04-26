package com.example.pc.academy_app_tis.head;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Wall_of_fame_head extends AppCompatActivity {
FloatingActionButton floatingActionButton;
TextView t_name;
TextView t_description;
ImageView t_imageView;
Button t_button;
    private final int IMG_REQUEST=1;
    String UploadUrl="https://tisabcd12.000webhostapp.com/head/adding_wall_fame.php";
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall_of_fame_head);


        floatingActionButton=(FloatingActionButton)findViewById(R.id.Floating_23);
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
                        uploadImage();
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

                           /* Intent i=new Intent(teachers_profile.this,teachers_profile.class);
                            startActivity(i);*/



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
        MySingleton.getInstance(Wall_of_fame_head.this).addToRequestQueue(stringRequest);
    }


    private String imageToSTring(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] imgBytes=byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imgBytes,Base64.DEFAULT);

    }
}
