package com.story.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.net.MalformedURLException;
import java.util.Objects;

public class ReadingActivity extends AppCompatActivity {
    private RelativeLayout floatinglayout;
    private Animation fade_out;
    private FloatingActionButton audioPlay;
    private FloatingActionButton zoomIn;
    private FloatingActionButton zoomOut;
    private MediaPlayer mediaPlayer;
    private RelativeLayout mainlayout;
    private TextView story;
    int textsize;
    private String storyIMage;
    private String storyid;
    private String storyText;
    private ProgressBar progressBar;
    private HistoryDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);


        floatinglayout=(RelativeLayout) findViewById(R.id.story_floating_layout);
        mainlayout=(RelativeLayout) findViewById(R.id.story_main_layout);
        fade_out= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_out);
        audioPlay=(FloatingActionButton) findViewById(R.id.story_audio_play);
        zoomIn=(FloatingActionButton) findViewById(R.id.story_zoom_in);
        zoomOut=(FloatingActionButton) findViewById(R.id.story_zoom_out);
        story=(TextView) findViewById(R.id.reader_story);
        textsize=30;
        story.setTextSize(textsize);
        storyIMage=getIntent().getStringExtra("StoryImage");
        storyid=getIntent().getStringExtra("StoryId");
        ImageView readerImage=(ImageView)findViewById(R.id.reader_image);
        TextView storytitle=(TextView)findViewById(R.id.story_title);
        progressBar=(ProgressBar)findViewById(R.id.reader_progress_bar);
        database=new HistoryDatabase(this);

        storytitle.setText(storyid);

        Glide
                .with(getApplicationContext())
                .load(storyIMage)
                .placeholder(R.drawable.ic_launcher_background)
                .into(readerImage);

        loadstory();

        audioPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterAudio();
            }
        });

        mainlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatinglayoutshow();
            }
        });

        if(mediaPlayer!=null){
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        }

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textsize<80) {
                    textsize+=10;
                    story.setTextSize(textsize);
                }
            }
        });
        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textsize>10) {
                    textsize-=10;
                    story.setTextSize(textsize);
                }
            }
        });
        database.addToHistory(storyid);

    }

    private void loadstory() {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("Stories").document(storyid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    if(task.getResult().get("Story")!=null)
                     storyText = Objects.requireNonNull(task.getResult().get("Story")).toString();
                    story.setText(storyText);
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }

    private void alterAudio() {
        if(mediaPlayer==null) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.audio);
            mediaPlayer.start();
            audioPlay.setImageResource(R.drawable.arrow_pause);
        }
        else if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            audioPlay.setImageResource(R.drawable.play_arrow);
        }
        else{
            mediaPlayer.start();
            audioPlay.setImageResource(R.drawable.arrow_pause);
        }
    }

    private void floatinglayoutshow() {
        mainlayout.setEnabled(false);
        floatinglayout.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                floatinglayout.startAnimation(fade_out);
                floatinglayout.setVisibility(View.INVISIBLE);
                mainlayout.setEnabled(true);
            }
        },3000);
    }

    @Override
    protected void onStart() {
        super.onStart();
        floatinglayoutshow();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mediaPlayer!=null)
            mediaPlayer.release();
    }
}