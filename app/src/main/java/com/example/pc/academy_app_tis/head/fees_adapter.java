package com.example.pc.academy_app_tis.head;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.academy_app_tis.R;

/**
 * Created by pc on 4/25/2018.
 */

public class fees_adapter extends RecyclerView.Adapter<fees_adapter.NUMBERVIEWHOLDER>{


    String start_date[];
    String end_date[];
    String amount[];



    private fees_adapter.fees_adapterOnClickHandler mClickHandler;
    public interface fees_adapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public fees_adapter(fees_adapter.fees_adapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public fees_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_previous_fees,parent,false);
        return new fees_adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(fees_adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.sub.setText(start_date[position]);
        holder.cl.setText(end_date[position]);
        holder.n.setText(amount[position]);





    }

    @Override
    public int getItemCount() {

        if(start_date==null)
            return 0;
        else
            return start_date.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView sub;
        TextView cl;
        TextView n;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            sub=(TextView)view.findViewById(R.id.start_date_13);
            cl=(TextView)view.findViewById(R.id.end_date_13);
            n=(TextView)view.findViewById(R.id.amount_13);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context,String batch_subject[],String batch_class[], String batch_number[]) {
        // Always close the previous mCursor first


            // Force the RecyclerView to refresh
            this.start_date=batch_subject;
            this.end_date=batch_class;
            this.amount=batch_number;

            this.context=context;
            this.notifyDataSetChanged();

    }




}