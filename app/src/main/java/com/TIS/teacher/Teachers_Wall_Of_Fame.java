package com.TIS.teacher;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.TIS.R;
import com.TIS.head.Wall_of_fame_Adapter;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class Teachers_Wall_Of_Fame extends AppCompatActivity implements Wall_of_fame_Adapter.Wall_of_fame_AdapterOnClickHandler {
    String title[];
    String description[];

    Wall_of_fame_Adapter adapter;
    RecyclerView recyclerView;
    ArrayList<ParseFile> parseFiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachers__wall__of__fame);


        Parse.initialize(this);
        ParseInstallation.getCurrentInstallation().saveInBackground();


        recyclerView=(RecyclerView)findViewById(R.id.recycler_28);
        LinearLayoutManager layoutManager=new LinearLayoutManager(Teachers_Wall_Of_Fame.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter=new Wall_of_fame_Adapter(Teachers_Wall_Of_Fame.this);
        recyclerView.setAdapter(adapter);
        Background_wall_of_fame_parse();
       // Background_getting_wall_fame background_getting_wall_fame=new Background_getting_wall_fame();
       // background_getting_wall_fame.execute();

    }

    @Override
    public void onClick(int x) {

    }



    void Background_wall_of_fame_parse()
    {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("wall_of_fame_table");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> scoreList, ParseException e) {
                if (e == null) {

                    if(scoreList.size()==0)
                    {
                        Toast.makeText(Teachers_Wall_Of_Fame.this,"Nothing Found",Toast.LENGTH_SHORT).show();

                    }
                    else {
                        int count = 0;


                        //  jsonArray = jsonObject.getJSONArray("server response");
                        int size = scoreList.size();
                        title = new String[size];
                        description = new String[size];
                        parseFiles=new ArrayList<ParseFile>(size);
                        while (count < scoreList.size()) {
                            //JSONObject JO = jsonArray.getJSONObject(count);
                            title[count] = scoreList.get(count).getString("title");
                            description[count] = scoreList.get(count).getString("description");
                            parseFiles.add(count,scoreList.get(count).getParseFile("image"));
                            count++;


                        }

                        adapter.swapCursor(getApplicationContext(), title, description,parseFiles);

                        // Toast.makeText(login_signup.this,"Found",Toast.LENGTH_LONG).show();



                    }
                } else {
                    Toast.makeText(Teachers_Wall_Of_Fame.this,"Connection_problem",Toast.LENGTH_LONG).show();
                }
            }
        });


    }


}
