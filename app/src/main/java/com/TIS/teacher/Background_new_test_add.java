package com.TIS.teacher;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by pc on 4/27/2018.
 */

public class Background_new_test_add extends AsyncTask<String,Void,String> {
    Context context;
    String url_insert="https://tisabcd12.000webhostapp.com/teacher/adding_test_name.php";

    public Background_new_test_add(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String batch_subject=strings[0];
        String batch_class=strings[1];
        String batch_number=strings[2];
        String test_name=strings[3];
        String test_marks=strings[4];

        try {
            URL url=new URL(url_insert);
            HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream outputStream=httpURLConnection.getOutputStream();

            BufferedWriter bufferedWriter=new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));

            String data= URLEncoder.encode("batch_subject","UTF-8")+"="+URLEncoder.encode(batch_subject,"UTF-8")+"&"+
                    URLEncoder.encode("batch_class","UTF-8")+"="+URLEncoder.encode(batch_class,"UTF-8")+"&"+
                    URLEncoder.encode("batch_number","UTF-8")+"="+URLEncoder.encode(batch_number,"UTF-8")+"&"+
            URLEncoder.encode("test_name","UTF-8")+"="+URLEncoder.encode(test_name,"UTF-8")+"&"+
                    URLEncoder.encode("total_marks","UTF-8")+"="+URLEncoder.encode(test_marks,"UTF-8")
            ;
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream IS=httpURLConnection.getInputStream();
            IS.close();

            return "Added ";



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }
}