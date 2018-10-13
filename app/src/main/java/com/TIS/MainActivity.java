package com.TIS;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.TIS.head.Head_Batch;
import com.TIS.student.Student_Navigation;
import com.TIS.teacher.Teacher_batch;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();


        boolean flag = pref.getBoolean("is", false);  // getting boolean
     //   Toast.makeText(MainActivity.this, flag + "", Toast.LENGTH_LONG).show();
        if (flag == true) {

            String h_t_s = pref.getString("h_t_s", null);
            if (h_t_s.equals("Head")) {
                Intent i = new Intent(MainActivity.this, Head_Batch.class);
                startActivity(i);
                finish();
            } else if (h_t_s.equals("Teacher")) {
                Intent i = new Intent(MainActivity.this, Teacher_batch.class);
                startActivity(i);
                finish();
            } else if (h_t_s.equals("Student")) {
                Intent i = new Intent(MainActivity.this, Student_Navigation.class);
                startActivity(i);
                finish();
            }
        }else {


                Thread myThread = new Thread() {
                    @Override
                    public void run() {
                        try {

                            sleep(2500);

                            Intent intent = new Intent(MainActivity.this, login_signup.class);

                            startActivity(intent);
                            finish();

                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }

                    }
                };
                myThread.start();
            }
        }
    }

