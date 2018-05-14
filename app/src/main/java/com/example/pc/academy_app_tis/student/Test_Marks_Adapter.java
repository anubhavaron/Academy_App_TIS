package com.example.pc.academy_app_tis.student;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.pc.academy_app_tis.R;

/**
 * Created by pc on 5/15/2018.
 */

public class Test_Marks_Adapter extends RecyclerView.Adapter<Test_Marks_Adapter.NUMBERVIEWHOLDER>{


    String subject[];
    String name[];
    String marks_o[];
    String marks_t[];

    private Context mcontext;

    private Test_Marks_Adapter.Test_Marks_AdapterOnClickHandler mClickHandler;
    public interface Test_Marks_AdapterOnClickHandler
    {
        void onClick(int x);
    }
    private Context context;
    public Test_Marks_Adapter(Test_Marks_Adapter.Test_Marks_AdapterOnClickHandler clickHandler)
    {

        mClickHandler=clickHandler;

    }




    @Override
    public Test_Marks_Adapter.NUMBERVIEWHOLDER onCreateViewHolder(ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_test_marks_student,parent,false);
        return new Test_Marks_Adapter.NUMBERVIEWHOLDER(view);

    }

    @Override
    public void onBindViewHolder(Test_Marks_Adapter.NUMBERVIEWHOLDER holder, int position) {



        holder.sub.setText(subject[position]);
        holder.cl.setText(name[position]);
        holder.n.setText(marks_o[position]+"/"+marks_t[position]);





    }

    @Override
    public int getItemCount() {

        if(subject==null)
            return 0;
        else
            return subject.length;
    }



    public class NUMBERVIEWHOLDER extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView sub;
        TextView cl;
        TextView n;

        public NUMBERVIEWHOLDER(View view)

        {

            super(view);
            sub=(TextView)view.findViewById(R.id.subject_104);
            cl=(TextView)view.findViewById(R.id.name_104);
            n=(TextView)view.findViewById(R.id.marks_104);

            itemView.setOnClickListener(this);


        }


        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());

        }
    }
    public void swapCursor(Context context,String batch_subject[],String batch_class[], String batch_number[],String marks_t[]) {
        // Always close the previous mCursor first

        if (batch_subject != null) {
            // Force the RecyclerView to refresh
            this.subject=batch_subject;
            this.name=batch_class;
            this.marks_o=batch_number;
            this.marks_t=marks_t;

            this.context=context;
            this.notifyDataSetChanged();
        }
    }







}