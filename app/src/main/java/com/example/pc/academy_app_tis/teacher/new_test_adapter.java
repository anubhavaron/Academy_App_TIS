package com.example.pc.academy_app_tis.teacher;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.academy_app_tis.R;
import com.example.pc.academy_app_tis.head.fees_adapter;

import org.w3c.dom.Text;

/**
 * Created by pc on 4/27/2018.
 */

public class new_test_adapter extends RecyclerView.Adapter<new_test_adapter.NUMBERVIEWHOLDER>{


    String test_name[];
    String marks[];




    private new_test_adapter.new_test_adapterOnClickHandler mClickHandler;
    public interface new_test_adapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public new_test_adapter(new_test_adapter.new_test_adapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public new_test_adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_new_test,parent,false);
        return new new_test_adapter.NUMBERVIEWHOLDER(view);

    }


    @Override
    public void onBindViewHolder(new_test_adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.sub.setText(test_name[position]);
        holder.cl.setText(marks[position]);
        holder.n.setText((position+1)+".");





    }

    @Override
    public int getItemCount() {

        if(test_name==null)
            return 0;
        else
            return test_name.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView sub;
        TextView cl;
        TextView n;
        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            sub=(TextView)view.findViewById(R.id.test_name_30);
            cl=(TextView)view.findViewById(R.id.marks_30);
            n=(TextView)view.findViewById(R.id.s_no_30);



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
            this.test_name=batch_subject;
            this.marks=batch_class;


            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}