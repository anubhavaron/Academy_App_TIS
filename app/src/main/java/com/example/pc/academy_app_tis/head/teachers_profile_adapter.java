package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.signature.StringSignature;
import com.example.pc.academy_app_tis.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by pc on 4/23/2018.
 */

public class teachers_profile_adapter extends RecyclerView.Adapter<teachers_profile_adapter.NUMBERVIEWHOLDER>{


    String username[];
    String description[];
    ArrayList<ParseFile> parseFiles;



    private teachers_profile_adapter.teachers_profile_adapterOnClickHandler mClickHandler;
    public interface teachers_profile_adapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public teachers_profile_adapter(teachers_profile_adapter.teachers_profile_adapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public teachers_profile_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_teachers_profile,parent,false);
        return new teachers_profile_adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(final teachers_profile_adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.name.setText(username[position]);
        holder.description.setText(description[position]);
        //String url="https://tisabcd12.000webhostapp.com/teacher/photos/"+photoname[position]+".jpg";
        /*Glide.with(context)
                .load(url) // image url
                .placeholder(R.drawable.ic_people_black_24dp) // any placeholder to load at start
                .error(R.drawable.ic_people_black_24dp)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.imageView);*/

        /*Glide.with(context).load(url).asBitmap().centerCrop().into(new BitmapImageViewTarget(holder.imageView) {
            @Override
            protected void setResource(Bitmap resource) {
                RoundedBitmapDrawable circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                circularBitmapDrawable.setCircular(true);
                holder.imageView.setImageDrawable(circularBitmapDrawable);
            }
        });*/
        parseFiles.get(position).getDataInBackground(new GetDataCallback() {

            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    if(data==null) {
                        // Toast.makeText(MainActivity.this, "HI", Toast.LENGTH_LONG).show();
                    }
                    else {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                      //  holder.imageView.setImageBitmap(bitmap);
                        holder.circleImageView.setImageBitmap(bitmap);

                    }

                } else {
                    //byteArray not retrieved from parseImage handle the exception

                }
            }
        });





    }

    @Override
    public int getItemCount() {

        if(username==null)
            return 0;
        else
            return username.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView name;
        TextView description;

        ImageView imageView;
        CircleImageView circleImageView;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            name=(TextView)view.findViewById(R.id.teacher_name_8);
            description=(TextView)view.findViewById(R.id.description_8);
            imageView=(ImageView) view.findViewById(R.id.image_8);
            circleImageView=(CircleImageView)view.findViewById(R.id.profile_image);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {


            mClickHandler.onClick(getAdapterPosition());
        }
    }
    public void swapCursor(Context context, String name[], String description[], ArrayList<ParseFile> parseFiles) {
        // Always close the previous mCursor first

        if (name != null) {
            // Force the RecyclerView to refresh
            this.username=name;
            this.description=description;
           // this.photoname=photoname;
            this.parseFiles=parseFiles;


            this.context=context;
            this.notifyDataSetChanged();
        }
    }
}