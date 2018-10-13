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
 * Created by pc on 4/28/2018.
 */

public class Background_feed_not_image extends AsyncTask<String,Void,String> {
    Context context;
    String url_insert="https://tisabcd12.000webhostapp.com/teacher/adding_test_fees_without_image.php";

    public Background_feed_not_image(Context context) {
        this.context=context;
    }

    @Override
    protected String doInBackground(String... strings) {
        String batch_subject=strings[0];
        String batch_class=strings[1];
        String batch_number=strings[2];
        String title=strings[3];
        String message=strings[4];

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
                    URLEncoder.encode("title","UTF-8")+"="+URLEncoder.encode(title,"UTF-8")+"&"+
                    URLEncoder.encode("message","UTF-8")+"="+URLEncoder.encode(message,"UTF-8")
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