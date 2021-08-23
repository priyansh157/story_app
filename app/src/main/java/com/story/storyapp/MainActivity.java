package com.story.storyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    public static List<SlideModel> carouselList=new ArrayList<SlideModel>();
    private ImageSlider carousel;
    private static MainAdapter adapter;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    public static List<ParentItem> storylist=new ArrayList<>();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView sidebarbutton;
    private List<String> carouseltemp;
    private static List<String> storytemp;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carousel=(ImageSlider) findViewById(R.id.carousel);
        layoutManager= new LinearLayoutManager(getApplicationContext()){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        recyclerView=(RecyclerView) findViewById(R.id.main_parent_recycler);
        recyclerView.setLayoutManager(layoutManager);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer);
        navigationView=(NavigationView) findViewById(R.id.nav_view);
        sidebarbutton=(ImageView) findViewById(R.id.sideBarButton);
        final Animation rotateanim= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_clockwise);
        progressBar=(ProgressBar)findViewById(R.id.main_progress_bar);
        adapter=new MainAdapter(storylist);
        recyclerView.setAdapter(adapter);
        carousel.setImageList(carouselList);

        sidebarbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sidebarbutton.startAnimation(rotateanim);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            drawerLayout.openDrawer(GravityCompat.START);
                        }
                    },300);

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.history:
//                        Toast.makeText(getApplicationContext(),"History clicked",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(getApplicationContext(),StoryGrid.class);
                        intent.putExtra("Title","My Picks");
                        startActivity(intent);
                        break;
                    case R.id.share:
                        Toast.makeText(getApplicationContext(),"share clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.aboutus:
                        Toast.makeText(getApplicationContext(),"about us clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.contactus:
                        Toast.makeText(getApplicationContext(),"contact us clicked",Toast.LENGTH_SHORT).show();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    public static void fillstorylist(){
        FirebaseFirestore.getInstance().collection("Categories").orderBy("index").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot categorysnap: Objects.requireNonNull(task.getResult())){
                                storytemp= (List<String>) categorysnap.get("list");
                                storylist.add(new ParentItem(categorysnap.getId(),fillchildlist(storytemp)));
//                                adapter.notifyDataSetChanged();
                                storytemp.clear();
                            }

//                            progressBar.setVisibility(View.INVISIBLE);
//                            recyclerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

    }

    public static List<ChildItem> fillchildlist(List<String> storytemp) {
        List<ChildItem> childItemList=new ArrayList<>();
        for(int i=0;i<storytemp.size();i++){
            FirebaseFirestore.getInstance().collection("Stories").document(storytemp.get(i).trim()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if(task.isSuccessful()){
                                childItemList.add(new ChildItem((String) task.getResult().get("Image"),task.getResult().getId()));
                            }
                        }
                    });
        }

        return childItemList;
    }

    private void fillcarousel() {
        FirebaseFirestore.getInstance().collection("AppData").document("Carousel").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            carouseltemp =(List<String>)task.getResult().get("carousel");
                            int i=0;
                            while(i<carouseltemp.size()){
                                carouselList.add(new SlideModel(carouseltemp.get(i),ScaleTypes.FIT));
                                i++;
                            }
                            carousel.setImageList(carouselList);
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
          if(drawerLayout.isDrawerOpen(GravityCompat.START)){
              drawerLayout.closeDrawer(GravityCompat.START);
          }
          else
            super.onBackPressed();

    }

    @Override
    protected void onStart() {
        super.onStart();
        StoryGrid.gridlist= new ArrayList<>();
        if(carouselList.isEmpty())
            fillcarousel();
    }

}