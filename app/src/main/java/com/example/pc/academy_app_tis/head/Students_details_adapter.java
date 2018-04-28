package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.graphics.Bitmap;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

/**
 * Created by pc on 4/22/2018.
 */

public class Students_details_adapter extends RecyclerView.Adapter<Students_details_adapter.NUMBERVIEWHOLDER>{


    String name[];





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
        String url="https://tisabcd12.000webhostapp.com/student/photos/logo.PNG";
        Glide.with(context)
                .load(url) // image url
               .placeholder(R.drawable.ic_people_black_24dp) // any placeholder to load at start
                .error(R.drawable.ic_people_black_24dp)  // any image in case of error
                .override(100, 100) // resizing
        //.centerCrop()
        .into(holder.imageView);
        holder.number.setText((position+1)+".");

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
    public void swapCursor(Context context,String name[]) {
        // Always close the previous mCursor first

        if (name != null) {
            // Force the RecyclerView to refresh
            this.name=name;

            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}