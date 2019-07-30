package com.app.zippnews.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.zippnews.R;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    Context context;
    List<String> categoriesModelArrayList;
    ArrayList<Boolean> booleanArrayList=new ArrayList<>();

    public TagsAdapter(Context context, List<String> reviewList) {
        this.context=context;
        this.categoriesModelArrayList = reviewList;
        booleanArrayList.clear();
        for (int i=0;i<reviewList.size();i++)
            booleanArrayList.add(false);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tags, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {
        final String model=categoriesModelArrayList.get(pos);
        viewHolder.tag.setText(model);
        if(booleanArrayList.get(pos))
        {
           viewHolder.tag.setTextColor(Color.parseColor("#ffffff"));
           viewHolder.tag.setSelected(true);
        }else {
            viewHolder.tag.setTextColor(Color.parseColor("#000000"));
            viewHolder.tag.setSelected(false);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(booleanArrayList.get(pos))
                    booleanArrayList.set(pos,false);
                else
                    booleanArrayList.set(pos,true);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {

        return categoriesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tag;
        public ViewHolder(View itemView)
        {
            super(itemView);
            tag=(TextView) itemView.findViewById(R.id.tag);
        }
    }

    public String getSelectedTags()
    {
        String selectedtags="";
        for(int i=0;i<booleanArrayList.size();i++)
        {
            if(booleanArrayList.get(i))
                selectedtags=selectedtags+" "+categoriesModelArrayList.get(i)+",";
        }
        if(selectedtags.length()>1)
        selectedtags=selectedtags.substring(0,selectedtags.length()-1);
        return selectedtags;
    }
}
