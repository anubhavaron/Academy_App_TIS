package com.TIS.student;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TIS.R;

/**
 * Created by pc on 4/29/2018.
 */

public class Batch_Adapter extends RecyclerView.Adapter<Batch_Adapter.NUMBERVIEWHOLDER>{


    String batch_subject[];
    String batch_class[];
    String batch_number[];

    private Context mcontext;

    private Batch_Adapter.Batch_AdapterOnClickHandler mClickHandler;
    public interface Batch_AdapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public Batch_Adapter(Batch_Adapter.Batch_AdapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public Batch_Adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_student_batch,parent,false);
        return new Batch_Adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(Batch_Adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.sub.setText(batch_subject[position]);
        holder.cl.setText(batch_class[position]);
        holder.n.setText(batch_number[position]);





    }

    @Override
    public int getItemCount() {

        if(batch_subject==null)
            return 0;
        else
            return batch_subject.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView sub;
        TextView cl;
        TextView n;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            sub=(TextView)view.findViewById(R.id.subject_52);
            cl=(TextView)view.findViewById(R.id.class_52);
            n=(TextView)view.findViewById(R.id.number_52);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context,String batch_subject[],String batch_class[], String batch_number[]) {
        // Always close the previous mCursor first

        if (batch_subject != null) {
            // Force the RecyclerView to refresh
            this.batch_subject=batch_subject;
            this.batch_class=batch_class;
            this.batch_number=batch_number;

            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}