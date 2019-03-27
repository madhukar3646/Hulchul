package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.LocalVideo_Model;

import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */
public class LocalVideothumbnailsAdapter extends RecyclerView.Adapter<LocalVideothumbnailsAdapter.MyViewHolder>
{
    private Context context;
    private int width;
    private ArrayList<LocalVideo_Model> videoModelArrayList;
    private LocalVideo_Model model;

    public LocalVideothumbnailsAdapter(Context context,ArrayList<LocalVideo_Model> videoModelArrayList)
    {
        this.videoModelArrayList=videoModelArrayList;
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
    }

    @Override
    public LocalVideothumbnailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.localvideo_thumbnailmodel, parent, false);

        return new LocalVideothumbnailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LocalVideothumbnailsAdapter.MyViewHolder holder, final int position)
    {
        final LocalVideothumbnailsAdapter.MyViewHolder myViewHolder=holder;
         model=videoModelArrayList.get(position);
         holder.iv_catimage.setImageBitmap(model.getBitmap());
         holder.tv_duration.setText(model.getDuration());
        /*Picasso.with(context).load(R.mipmap.sampleimage)
                .error(R.mipmap.placeholder)
                .into(holder.iv_catimage);*/
    }

    @Override
    public int getItemCount()
    {
        return videoModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_catimage;
        public TextView tv_duration;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            itemView.getLayoutParams().height=(width/3)+((width/3)/3);
            iv_catimage=(ImageView)itemView.findViewById(R.id.iv_catimage);
            tv_duration=(TextView)itemView.findViewById(R.id.tv_duration);
        }
    }

}
