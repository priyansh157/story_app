package com.story.storyapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter {
    List<ParentItem> list;
    public MainAdapter(List<ParentItem> list) {
        this.list=list;
    }

    @NonNull
    @Override

    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.main_activity_recycler_design,parent,false);
        return new mainviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String title= list.get(position).getTitle();
        List<ChildItem> childItemList=list.get(position).getChildItems();

        ((mainviewholder)holder).setDetails(title,childItemList);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class mainviewholder extends RecyclerView.ViewHolder {

        private TextView heading;
        private TextView seeall;
        private RecyclerView recyclerView;
        public mainviewholder(View view) {
            super(view);

            heading=(TextView) view.findViewById(R.id.heading_main);
            seeall=(TextView) view.findViewById(R.id.see_all);
            recyclerView=(RecyclerView) view.findViewById(R.id.horizontalRecycler);

        }
        public void setDetails(final String title, List<ChildItem> childItems){

            heading.setText(title);
            ChildAdapter childAdapter=new ChildAdapter(childItems,"");
            LinearLayoutManager layoutManager= new LinearLayoutManager(itemView.getContext());
            layoutManager.setOrientation(RecyclerView.HORIZONTAL);
            recyclerView.setAdapter(childAdapter);
            recyclerView.setLayoutManager(layoutManager);

            seeall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent= new Intent(itemView.getContext(),StoryGrid.class);
                    intent.putExtra("Title",title);
                    StoryGrid.gridlist.clear();
                    StoryGrid.gridlist.addAll(childItems);
                    itemView.getContext().startActivity(intent);
                }
            });


        }
    }


}
