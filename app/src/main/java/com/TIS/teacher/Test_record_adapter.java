package com.TIS.teacher;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TIS.R;

/**
 * Created by pc on 4/27/2018.
 */

public class Test_record_adapter extends RecyclerView.Adapter<Test_record_adapter.NUMBERVIEWHOLDER>{


    String name[];
    String marks[];


    private Context mcontext;

    private Test_record_adapter.Test_record_adapterOnClickHandler mClickHandler;
    public interface Test_record_adapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public Test_record_adapter(Test_record_adapter.Test_record_adapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public Test_record_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_test_record,parent,false);
        return new Test_record_adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(Test_record_adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.sub.setText(name[position]);
        holder.cl.setText(marks[position]);
        holder.n.setText((position+1)+".");





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
            sub=(TextView)view.findViewById(R.id.name_34);
            cl=(TextView)view.findViewById(R.id.marks_34);
            n=(TextView)view.findViewById(R.id.number_34);


            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context,String batch_subject[],String batch_class[]) {
        // Always close the previous mCursor first

        if (batch_subject != null) {
            // Force the RecyclerView to refresh
            this.name=batch_subject;
            this.marks=batch_class;


            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}