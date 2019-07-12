package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.model.VideoModel;
import com.bumptech.glide.Glide;

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
    private OnItemLongClickListener onItemLongClickListener;
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
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener)
    {
        this.onItemLongClickListener=onItemLongClickListener;
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

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onItemLongClickListener!=null)
                    onItemLongClickListener.onItemLongClick(discoverhashtagvideosList.get(position),position);
                return false;
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

    public interface OnItemLongClickListener
    {
        void onItemLongClick(VideoModel videoModel,int position);
    }
}
