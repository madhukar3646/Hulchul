package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.zippnews.R;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 4/20/2017.
 */

public class VideothumbnailsAdapter extends RecyclerView.Adapter<VideothumbnailsAdapter.MyViewHolder>
{
    private Context context;
    private int width;
    private OnVideoSelectedListener onVideoSelectedListener;

    public VideothumbnailsAdapter(Context context)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
    }

    public void setOnVideoSelectedListener(OnVideoSelectedListener onVideoSelectedListener)
    {
        this.onVideoSelectedListener=onVideoSelectedListener;
    }

    @Override
    public VideothumbnailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videosgrid_model, parent, false);

        return new VideothumbnailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VideothumbnailsAdapter.MyViewHolder holder, final int position)
    {
        final VideothumbnailsAdapter.MyViewHolder myViewHolder=holder;

        Picasso.with(context).load(R.mipmap.sampleimage)
                .error(R.mipmap.placeholder)
                .into(holder.iv_catimage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onVideoSelectedListener!=null)
                    onVideoSelectedListener.onVideoSelected();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return 30;
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

    public interface OnVideoSelectedListener
    {
        void onVideoSelected();
    }

}
