package com.example.pc.academy_app_tis;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {

                    sleep(2500);

                    Intent intent=new Intent(MainActivity.this,login_signup.class);

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
