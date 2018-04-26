package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pc.academy_app_tis.R;

/**
 * Created by pc on 4/27/2018.
 */

public class Wall_of_fame_Adapter extends RecyclerView.Adapter<Wall_of_fame_Adapter.NUMBERVIEWHOLDER>{


    String title[];
    String description[];




    private Wall_of_fame_Adapter.Wall_of_fame_AdapterOnClickHandler mClickHandler;
    public interface Wall_of_fame_AdapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public Wall_of_fame_Adapter(Wall_of_fame_Adapter.Wall_of_fame_AdapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public Wall_of_fame_Adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_wall_of_fame,parent,false);
        return new Wall_of_fame_Adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(Wall_of_fame_Adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.name.setText(title[position]);
        holder.description.setText(description[position]);
        String url="https://tisabcd12.000webhostapp.com/photos/"+title[position]+".jpg";
        Glide.with(context)
                .load(url) // image url
                .placeholder(R.drawable.ic_people_black_24dp) // any placeholder to load at start
                .error(R.drawable.ic_people_black_24dp)  // any image in case of error
                .override(200, 200) // resizing
                .centerCrop()
                .into(holder.imageView);






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
        TextView name;
        TextView description;
        ImageView imageView;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            name=(TextView)view.findViewById(R.id.title_26);
            description=(TextView)view.findViewById(R.id.description_26);
            imageView=(ImageView) view.findViewById(R.id.image_view_26);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {


            mClickHandler.onClick(getAdapterPosition());
        }
    }
    public void swapCursor(Context context,String name[],String description[]) {
        // Always close the previous mCursor first

        if (name != null) {
            // Force the RecyclerView to refresh
            this.title=name;
            this.description=description;


            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}