package com.app.hulchul.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hulchul.R;

import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.ViewHolder> {

    Context context;
    List<String> categoriesModelArrayList;
    private OnTagSelectionListener listener;
    int selected=-1;


    public void setListener(OnTagSelectionListener listener) {
        this.listener = listener;
    }

    public TagsAdapter(Context context, List<String> reviewList) {
        this.context=context;
        this.categoriesModelArrayList = reviewList;
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
        if(selected==pos)
        {
           viewHolder.tag.setTextColor(Color.parseColor("#ffffff"));
           viewHolder.itemView.setSelected(true);
        }else {
            viewHolder.tag.setTextColor(Color.parseColor("#000000"));
            viewHolder.itemView.setSelected(false);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onCategorySelected(model);
                }
                selected=pos;
                Log.e("position",""+pos);
                notifyDataSetChanged();
            }
        });
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getSelected() {
        return selected;
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
public interface OnTagSelectionListener{

        void onCategorySelected(String category);
}

}
