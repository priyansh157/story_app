package com.story.storyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ChildAdapter extends RecyclerView.Adapter {

    private List<ChildItem> list;
    private String type;

    public ChildAdapter(List<ChildItem> list,String mtype) {
        this.list = list;
        this.type=mtype;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_story_design,parent,false);
        return new childviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String image=list.get(position).getImage();
        String id=list.get(position).getStoryId();

        ((childviewholder)holder).setDetails(image,id,position);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class childviewholder extends RecyclerView.ViewHolder {
        private ImageView imgview;
        public childviewholder(View view) {
            super(view);

            imgview=(ImageView) view.findViewById(R.id.horizontal_image);
        }

        public void setDetails(String image,String Id,int position){

            Glide
                    .with(itemView.getContext())
                    .load(image)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgview);
            imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(itemView.getContext(),ReadingActivity.class);
                    intent.putExtra("StoryId",Id);
                    intent.putExtra("StoryImage",image);
                    itemView.getContext().startActivity(intent);
                }
            });
            imgview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(type.equals("My Picks")){
                        AlertDialog.Builder builder=new AlertDialog.Builder(itemView.getContext())
                                .setTitle("Alert")
                                .setMessage("You wanna remove it from your History??");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                HistoryDatabase db=new HistoryDatabase(itemView.getContext());
                                db.removeFromHistory(Id);
                                list.remove(position);
                                StoryGrid.childAdapter.notifyDataSetChanged();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }
                    return true;
                }
            });

        }
    }
}
