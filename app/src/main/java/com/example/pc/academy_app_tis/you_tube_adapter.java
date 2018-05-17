package com.example.pc.academy_app_tis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.academy_app_tis.teacher.Test_record_adapter;

import org.w3c.dom.Text;

/**
 * Created by pc on 5/15/2018.
 */

public class you_tube_adapter extends RecyclerView.Adapter<you_tube_adapter.NUMBERVIEWHOLDER>{


    String name[];



    private Context mcontext;

    private you_tube_adapter.you_tube_adapterOnClickHandler mClickHandler;
    public interface you_tube_adapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public you_tube_adapter(you_tube_adapter.you_tube_adapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public you_tube_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_links,parent,false);
        return new you_tube_adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(you_tube_adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.sub.setText(name[position]);
        holder.cl.setText((position+1)+".");







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
        TextView sub;
        TextView cl;
        TextView n;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            sub=(TextView)view.findViewById(R.id.name_111);
            cl=(TextView)view.findViewById(R.id.number_111);



            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context,String batch_subject[]) {
        // Always close the previous mCursor first

        if (batch_subject != null) {
            // Force the RecyclerView to refresh
            this.name=batch_subject;



            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}