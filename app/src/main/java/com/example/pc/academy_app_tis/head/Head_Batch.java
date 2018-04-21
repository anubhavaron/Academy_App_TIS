package com.example.pc.academy_app_tis.head;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.example.pc.academy_app_tis.R;

public class Head_Batch extends AppCompatActivity implements Head_Batch_Adapter.Head_Batch_AdapterOnClickHandler {
    RecyclerView recyclerView;
    Head_Batch_Adapter adapter;
    String batch_subject[];
    String batch_class[];
    String batch_number[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_head__batch);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_3);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Head_Batch_Adapter(Head_Batch.this);
        batch_subject=new String[2];
        batch_class=new String[2];
        batch_number=new String[2];
        batch_subject[0]="Science;";
        batch_subject[1]="Math";
        batch_class[0]="11";
        batch_class[1]="11";
        batch_number[0]="1";
        batch_number[1]="1";

        recyclerView.setAdapter(adapter);
        adapter.swapCursor(this,batch_subject,batch_class,batch_number);
    }

    @Override
    public void onClick(int x) {
        Toast.makeText(Head_Batch.this,x+"",Toast.LENGTH_LONG).show();

    }
}
