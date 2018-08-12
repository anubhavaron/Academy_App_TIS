package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.example.pc.academy_app_tis.R;
import com.parse.GetDataCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by pc on 4/22/2018.
 */

public class Students_details_adapter extends RecyclerView.Adapter<Students_details_adapter.NUMBERVIEWHOLDER>{


    String name[];
   // String photoname[];
ArrayList<ParseFile> parseFiles;



    private Students_details_adapter.Students_details_adapterOnClickHandler mClickHandler;
    public interface Students_details_adapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public Students_details_adapter(Students_details_adapter.Students_details_adapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public Students_details_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_head_students_details,parent,false);
        return new Students_details_adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(final Students_details_adapter.NUMBERVIEWHOLDER holder, int position) {

        String x=name[position].substring(0,name[position].length()-2);

        holder.textView.setText(x);
        holder.number.setText(position+1+"");

        parseFiles.get(position).getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    if(data==null) {
                        // Toast.makeText(MainActivity.this, "HI", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        holder.imageView.setImageBitmap(bitmap);
                    }

                } else {
                    //byteArray not retrieved from parseImage handle the exception

                }
            }
        });

    }

    @Override
    public int getItemCount() {

        if(name==null)
            return 0;
        else
            return name.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView textView;
        ImageView imageView;
        TextView number;

        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            textView=(TextView)view.findViewById(R.id.textView_7);
            number=(TextView)view.findViewById(R.id.number_7);
            imageView=(ImageView) view.findViewById(R.id.imageView_7);


            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context, String name[], ArrayList<ParseFile> parseFiles) {
        // Always close the previous mCursor first

        if (name != null) {
            // Force the RecyclerView to refresh
            this.name=name;
           // this.photoname=photoname;
            this.parseFiles=parseFiles;

            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}