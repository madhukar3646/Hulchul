package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.VideoModel;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */

public class HashtagsGridAdapter extends RecyclerView.Adapter<HashtagsGridAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<VideoModel> discoverhashtagvideosList;
    private OnHashtagItemClickListener onHashtagItemClickListener;
    private String videobasepath;

    public HashtagsGridAdapter(Context context, ArrayList<VideoModel> discoverhashtagvideosList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.discoverhashtagvideosList=discoverhashtagvideosList;
    }

    public void setVideobasepath(String path)
    {
        this.videobasepath=path;
    }

    @Override
    public HashtagsGridAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.videosgrid_model, parent, false);

        return new HashtagsGridAdapter.MyViewHolder(itemView);
    }

    public void setOnHashtagItemClickListener(OnHashtagItemClickListener onHashtagItemClickListener)
    {
        this.onHashtagItemClickListener=onHashtagItemClickListener;
    }

    @Override
    public void onBindViewHolder(final HashtagsGridAdapter.MyViewHolder holder, final int position)
    {
        final HashtagsGridAdapter.MyViewHolder myViewHolder=holder;
        VideoModel model=discoverhashtagvideosList.get(position);
        String gif=model.getVideo().substring(0,model.getVideo().length()-3);
        gif=videobasepath+gif+"gif";

        Glide.with(context).load(gif)
                .error(R.mipmap.placeholder)
                .into(holder.iv_catimage);

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
        public ImageView iv_catimage;
        public TextView tv_duration;
        public ImageView iv_playpauseimage;
        public LinearLayout layout_duration;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            itemView.getLayoutParams().height=(width/3)+((width/3)/3);
            iv_catimage=(ImageView)itemView.findViewById(R.id.iv_catimage);
            tv_duration=(TextView)itemView.findViewById(R.id.tv_duration);
            iv_playpauseimage=(ImageView)itemView.findViewById(R.id.iv_playpauseimage);
            layout_duration=(LinearLayout)itemView.findViewById(R.id.layout_duration);
            tv_duration.setVisibility(View.GONE);
            iv_playpauseimage.setVisibility(View.GONE);
            layout_duration.setVisibility(View.GONE);
        }
    }

    public interface OnHashtagItemClickListener
    {
        void onHashtagitemClick(ArrayList<VideoModel> discoverhashtagvideosList, int pos);
    }
}
