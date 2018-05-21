package com.example.pc.academy_app_tis.teacher;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.Students_details_adapter;
import com.example.pc.academy_app_tis.head.head_navigation;

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

/**
 * Created by pc on 4/28/2018.
 */

public class Feed_Adapter extends RecyclerView.Adapter<Feed_Adapter.NUMBERVIEWHOLDER>{


    String title[];
    String message[];
    String fwion[];





    private Feed_Adapter.Feed_AdapterOnClickHandler mClickHandler;
    public interface Feed_AdapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public Feed_Adapter(Feed_Adapter.Feed_AdapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public Feed_Adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_feed_teacher,parent,false);
        return new Feed_Adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(Feed_Adapter.NUMBERVIEWHOLDER holder, int position) {


        position=title.length-position-1;
        if(position%2==0)
        {
           // holder.linearLayout.setBackgroundColor(Color.rgb(47,127,102));





        }
        else
        {

           // holder.linearLayout.setBackgroundColor(Color.rgb(47,127,102));


        }
        holder.title.setText(title[position]);
        holder.message.setText(message[position]);


        if(fwion[position].equals("YES"))
        {   holder.imageView.setVisibility(View.VISIBLE);
            String url="https://tisabcd12.000webhostapp.com/teacher/photos/"+title[position]+".jpg";
            Glide.with(context)
                    .load(url) // image url
                    .placeholder(R.drawable.ic_people_black_24dp) // any placeholder to load at start
                    .error(R.drawable.ic_people_black_24dp)  // any image in case of error
                    .override(200, 200) // resizing
                    .centerCrop()
                    .into(holder.imageView);


        }
        else
        {
         holder.imageView.setVisibility(View.GONE);
        }






    }

    @Override
    public int getItemCount() {

        if(title==null)
            return 0;
        else
            return title.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView title;
        TextView message;
        ImageView imageView;
        LinearLayout linearLayout;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            title=(TextView)view.findViewById(R.id.title_43);
            message=(TextView)view.findViewById(R.id.message_43);
            imageView=(ImageView)view.findViewById(R.id.imageView_43);
            linearLayout=(LinearLayout)view.findViewById(R.id.linear_layout_43);



            itemView.setOnClickListener(this);


        }
        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }
    public void swapCursor(Context context,String title[],String message[],String fwion[]) {
        // Always close the previous mCursor first
        if (title != null) {
            // Force the RecyclerView to refresh
            this.title=title;
            this.message=message;
            this.fwion=fwion;
            this.context=context;
            this.notifyDataSetChanged();
        }
    }
}