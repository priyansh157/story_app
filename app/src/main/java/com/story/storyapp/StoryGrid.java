package com.story.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class StoryGrid extends AppCompatActivity {
    private RecyclerView gridview;
    public static ChildAdapter childAdapter;
    public static List<ChildItem> gridlist;
    private GridLayoutManager gridLayoutManager;
    private TextView heading;
    private ProgressBar progressBar;
    private HistoryDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_grid);
        Intent intent=getIntent();
        String title=intent.getStringExtra("Title");
        progressBar=(ProgressBar)findViewById(R.id.grid_progress_bar);
        TextView testtext=(TextView)findViewById(R.id.grid_text);
        db=new HistoryDatabase(this);


        gridview=(RecyclerView) findViewById(R.id.grid_view);
        heading=(TextView) findViewById(R.id.grid_title);
        heading.setText(title);
        gridLayoutManager= new GridLayoutManager(getApplicationContext(),2, LinearLayoutManager.VERTICAL,false);
        gridview.setLayoutManager(gridLayoutManager);
        childAdapter= new ChildAdapter(gridlist,title);
        gridview.setAdapter(childAdapter);


        if(title.equals("My Picks")){
            fillHistoryList();
        }


    }

    private void fillHistoryList() {
        List<String> list=db.getHistory();
        fillchildlist(list);

    }


    private void fillchildlist(List<String> storytemp) {
        if(storytemp.size()!=0)
                {progressBar.setVisibility(View.VISIBLE);
                List<ChildItem> childItemList=new ArrayList<>();
                for(int i=0;i<storytemp.size();i++){
                    FirebaseFirestore.getInstance().collection("Stories").document(storytemp.get(i).trim()).get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isSuccessful()){
                                        gridlist.add(new ChildItem((String) task.getResult().get("Image"),task.getResult().getId()));
                                        progressBar.setVisibility(View.GONE);

                                        childAdapter.notifyDataSetChanged();

                                    }
                                }
                            });
                }}
    }
}