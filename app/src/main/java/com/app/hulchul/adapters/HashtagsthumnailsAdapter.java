package com.app.hulchul.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.activities.PlayvideosCategorywise_Activity;
import com.app.hulchul.model.VideoModel;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */

public class HashtagsthumnailsAdapter extends RecyclerView.Adapter<HashtagsthumnailsAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<VideoModel> discoverhashtagvideosList;
    private OnHashtagItemClickListener onHashtagItemClickListener;
    private String videobasepath;

    public HashtagsthumnailsAdapter(Context context,ArrayList<VideoModel> discoverhashtagvideosList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.discoverhashtagvideosList=discoverhashtagvideosList;
    }

    public void setVideobasepath(String videobasepath)
    {
        this.videobasepath=videobasepath;
    }

    @Override
    public HashtagsthumnailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hashtagthumbnail_model, parent, false);

        return new HashtagsthumnailsAdapter.MyViewHolder(itemView);
    }

    public void setOnHashtagItemClickListener(OnHashtagItemClickListener onHashtagItemClickListener)
    {
        this.onHashtagItemClickListener=onHashtagItemClickListener;
    }

    @Override
    public void onBindViewHolder(final HashtagsthumnailsAdapter.MyViewHolder holder, final int position)
    {
        final HashtagsthumnailsAdapter.MyViewHolder myViewHolder=holder;

        VideoModel model=discoverhashtagvideosList.get(position);
        String gif=model.getVideo().substring(0,model.getVideo().length()-3);
        gif=videobasepath+gif+"gif";

        Glide.with(context).load(gif)
                .error(R.mipmap.placeholder)
                .into(holder.iv_hashtagthumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onHashtagItemClickListener!=null)
                    onHashtagItemClickListener.onHashtagitemClick(discoverhashtagvideosList,position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return discoverhashtagvideosList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_hashtagthumbnail;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=(width*20)/100;
            itemView.getLayoutParams().height=(width*30)/100;
            iv_hashtagthumbnail=(ImageView)itemView.findViewById(R.id.iv_hashtagthumbnail);
        }
    }

    public interface OnHashtagItemClickListener
    {
        void onHashtagitemClick(ArrayList<VideoModel> discoverhashtagvideosList,int pos);
    }
}
