package com.app.hulchul.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.model.DraftModel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */

public class DraftsthumbnailsAdapter extends RecyclerView.Adapter<DraftsthumbnailsAdapter.MyViewHolder>
{
    private Context context;
    private int width;
    private ArrayList<DraftModel> draftModelArrayList;
    private VideoSelectedListener videoSelectedListener;
    private boolean selection=false;

    public DraftsthumbnailsAdapter(Context context,ArrayList<DraftModel> draftModelArrayList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        this.draftModelArrayList=draftModelArrayList;
    }

    public void setOnVideoSelectedListener(VideoSelectedListener videoSelectedListener)
    {
        this.videoSelectedListener=videoSelectedListener;
    }

    public void setVideosSelection(boolean selection)
    {
        this.selection=selection;
        if(!selection)
            setUnselect();
    }

    public void setUnselect()
    {
        for (int i=0;i<draftModelArrayList.size();i++)
        {
            if(draftModelArrayList.get(i).isSelected())
                draftModelArrayList.get(i).setSelected(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public DraftsthumbnailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.draftmodel, parent, false);

        return new DraftsthumbnailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DraftsthumbnailsAdapter.MyViewHolder holder, final int position)
    {
        final DraftsthumbnailsAdapter.MyViewHolder myViewHolder=holder;
        DraftModel model=draftModelArrayList.get(position);

        Glide.with(context)
                .load(Uri.fromFile(new File(model.getVideopath())))
                .into(holder.iv_catimage);

        if(selection)
            holder.checkbox.setVisibility(View.VISIBLE);
        else
            holder.checkbox.setVisibility(View.GONE);

        holder.checkbox.setChecked(model.isSelected());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(selection)
               {
                   if(draftModelArrayList.get(position).isSelected())
                   {
                       draftModelArrayList.get(position).setSelected(false);
                       holder.checkbox.setChecked(false);
                   }
                   else {
                       draftModelArrayList.get(position).setSelected(true);
                       holder.checkbox.setChecked(true);
                   }
                   if(videoSelectedListener!=null)
                       videoSelectedListener.onVideoSelected(draftModelArrayList.get(position).getVideopath());
               }
               else {
                 if(videoSelectedListener!=null)
                     videoSelectedListener.onGoToPostClicked(draftModelArrayList.get(position).getVideopath());
               }
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return draftModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_catimage;
        public CheckBox checkbox;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            itemView.getLayoutParams().height=(width/3)+((width/3)/3);
            iv_catimage=(ImageView)itemView.findViewById(R.id.iv_catimage);
            checkbox=(CheckBox)itemView.findViewById(R.id.checkbox);
        }
    }

    public interface VideoSelectedListener
    {
        void onVideoSelected(String path);
        void onGoToPostClicked(String path);
    }
}
