package com.example.pc.academy_app_tis;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.pc.academy_app_tis.student.Student_Navigation;
import com.example.pc.academy_app_tis.teacher.Background_new_test_add;
import com.example.pc.academy_app_tis.teacher.Teacher_batch;
import com.example.pc.academy_app_tis.teacher.Teacher_navigation;
import com.example.pc.academy_app_tis.teacher.Teacher_new_test;

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
import java.util.List;
import java.util.concurrent.ExecutionException;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.ParseInstallation;
import com.parse.SignUpCallback;

public class login_signup extends Activity {

    int status;

    String username;

    String password;

    String head_teacher_parent_student;

    RadioGroup radioGroup;

    RadioButton radioButton;

    EditText userName;

    EditText passWord;

    ImageView i;

    Button f;

    Button d;

    Context context;

    String radio_click;

    ActionBar actionBar;

    public login_signup() throws ExecutionException, InterruptedException {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);

        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        i=(ImageView)findViewById(R.id.i_d);
        i.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder mBuilder=new AlertDialog.Builder(login_signup.this);
                View mView=getLayoutInflater().inflate(R.layout.developers_profile,null);
                final Button sub=(Button) mView.findViewById(R.id.fb_d);
                final Button cl=(Button) mView.findViewById(R.id.gitb_d);

                sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Toast.makeText(login_signup.this,"f",Toast.LENGTH_SHORT).show();
                        String url = "http://www.facebook.com/anubhav.aron.7";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                cl.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Toast.makeText(login_signup.this,"g",Toast.LENGTH_SHORT).show();

                        String url = "http://www.github.com/anubhavaron";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });

                mBuilder.setView(mView);
                AlertDialog dialog=mBuilder.create();
                dialog.show();
            }
        });

        radioGroup=(RadioGroup)findViewById(R.id.radiogroup);
        userName=(EditText)findViewById(R.id.username_2);
        passWord=(EditText)findViewById(R.id.password_2);
        context=getApplicationContext();
    }

    public void LOGIN(View view) {

        if (radioGroup.getCheckedRadioButtonId() == -1)
        {   Toast.makeText(login_signup.this,"Choose (Head/Teacher/Student)",Toast.LENGTH_SHORT).show();
            // no radio buttons are checked
        }
        else
        {   int id=radioGroup.getCheckedRadioButtonId();

        radioButton=(RadioButton)findViewById(id);
            username=userName.getText().toString();
            password=passWord.getText().toString();

            if(radioButton.isChecked()==true) {
            head_teacher_parent_student=radioButton.getText().toString();
            if (head_teacher_parent_student.equals("Head")) {
                status = 1;
                radio_click="Head";
            }
            if (head_teacher_parent_student.equals("Teacher")) {
                status = 2;
                radio_click="Teacher";
            }
            if (head_teacher_parent_student.equals("Student/Parent")) {
                head_teacher_parent_student = "Student";
                radio_click="Student";
                status = 3;
            }
       //     Toast.makeText(login_signup.this,"Login",Toast.LENGTH_SHORT).show();
           // new Background_login().execute();
                check_for_login();
            }

        }







    }



    void check_for_login()
    {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("login_table");
        query.whereEqualTo("username", username);
       query.whereEqualTo("password", password);
       query.whereEqualTo("h_t_p_s",radio_click);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {
                    //Toast.makeText(login_signup.this,scoreList.size()+"",Toast.LENGTH_LONG).show();
                        if(scoreList.size()==0)
                        {
                            Toast.makeText(login_signup.this,"Nothing_Found",Toast.LENGTH_LONG).show();

                        }
                        else {

                           // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();
                            if(radio_click.equals("Head"))
                            {
                                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();

                                editor.putBoolean("is", true);
                                editor.putString("h_t_s", "Head");
                                editor.apply();
                                Toast.makeText(login_signup.this,"Succeessful Login",Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(login_signup.this, com.example.pc.academy_app_tis.head.Head_Batch.class);
                                startActivity(intent);
                                finish();
                            }
                            else
                                if(radio_click.equals("Student"))
                                {
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();

                                    editor.putBoolean("is", true);
                                    editor.putString("h_t_s", "Student");
                                    editor.putString("username", username);
                                    editor.apply();
                                    //Toast.makeText(login_signup.this,"bhbhjbh",Toast.LENGTH_LONG).show();
                                    Toast.makeText(login_signup.this,"Succeessful Login",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(login_signup.this, Student_Navigation.class);
                                    startActivity(intent);
                                    finish();

                                }
                                else
                                {
                                    SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = pref.edit();

                                    editor.putBoolean("is", true);
                                    editor.putString("h_t_s", "Teacher");
                                    editor.putString("username", username);
                                    editor.apply();
                                    //Toast.makeText(login_signup.this,flag+"",Toast.LENGTH_LONG).show();
                                    Toast.makeText(login_signup.this,"Succeessful Login",Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(login_signup.this, Teacher_batch.class);
                                    startActivity(intent);
                                    finish();
                                }


                        }
                } else {
                    Toast.makeText(login_signup.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });






    }
    /*
    class Background_login extends AsyncTask<Void,Void,String>
    {   String json_url="http://tisabcd12.000webhostapp.com/login_json.php";
        boolean flag=false;

        @Override
        protected void onPostExecute(String JSON_STRING) {
        //Toast.makeText(getApplicationContext(),JSON_STRING,Toast.LENGTH_LONG).show();
                if(JSON_STRING!=null) {
                    JSONObject jsonObject;
                    JSONArray jsonArray;

                    try {
                        jsonObject = new JSONObject(JSON_STRING);
                        int count = 0;
                        String u_name;
                        String p_word;
                        String h_t_s_p;
                        jsonArray = jsonObject.getJSONArray("server response");
                        while (count < jsonArray.length()) {
                            JSONObject JO = jsonArray.getJSONObject(count);
                            u_name = JO.getString("username");
                            p_word = JO.getString("password");
                            h_t_s_p = JO.getString("head_teacher_student_parent");

                            if ((u_name.equals(username)) && (p_word.equals(password)) && (h_t_s_p.equals(head_teacher_parent_student))) {
                                flag = true;
                            }
                            count++;


                        }

                        if ((flag) && (status == 1)) {
                            //
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean("is", true);
                            editor.putString("h_t_s", "Head");
                            editor.apply();
                             Toast.makeText(login_signup.this,"Succeessful Login",Toast.LENGTH_LONG).show();

                            Intent intent = new Intent(login_signup.this, com.example.pc.academy_app_tis.head.Head_Batch.class);
                            startActivity(intent);
                            finish();

                        } else if ((flag) && (status == 2)) {   //Toast.makeText(login_signup.this,"teacher_login",Toast.LENGTH_SHORT).show();
                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean("is", true);
                            editor.putString("h_t_s", "Teacher");
                            editor.putString("username", username);
                            editor.apply();
                            //Toast.makeText(login_signup.this,flag+"",Toast.LENGTH_LONG).show();
                            Toast.makeText(login_signup.this,"Succeessful Login",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(login_signup.this, Teacher_batch.class);
                            startActivity(intent);
                            finish();

                        } else if ((flag) && (status == 3)) {

                            SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();

                            editor.putBoolean("is", true);
                            editor.putString("h_t_s", "Student");
                            editor.putString("username", username);
                            editor.apply();
                            //Toast.makeText(login_signup.this,"bhbhjbh",Toast.LENGTH_LONG).show();
                            Toast.makeText(login_signup.this,"Succeessful Login",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(login_signup.this, Student_Navigation.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(login_signup.this, "Not Found", Toast.LENGTH_SHORT).show();
                            //ERROR
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                else
                {
                    Toast.makeText(login_signup.this,"No Internet Connectivity",Toast.LENGTH_SHORT).show();
                }


            super.onPostExecute(JSON_STRING);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
        public boolean isNetworkAvailable(Context context)
        {
            return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        }

        @Override
        protected String doInBackground(Void... voids) {
            String json_string;
            try {

                URL url=new URL(json_url);

                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                   // httpURLConnection.setConnectTimeout(7000);
                try {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((json_string = bufferedReader.readLine()) != null) {
                        stringBuilder.append(json_string + "\n");

                    }
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();
                }finally {
                    //end connection
                    httpURLConnection.disconnect();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }*/


}
