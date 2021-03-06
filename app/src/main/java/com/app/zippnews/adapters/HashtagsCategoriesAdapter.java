package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.model.Discoverhashtags;
import com.app.zippnews.model.VideoModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */
public class HashtagsCategoriesAdapter extends RecyclerView.Adapter<HashtagsCategoriesAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<Discoverhashtags> discoverhashtagsList;
    private Discoverhashtags discoverhashtags;
    private OnHashtagViewAllListener onHashtagViewAllListener;
    private HashtagsthumnailsAdapter.OnHashtagItemClickListener onHashtagItemClickListener;
    private String videobasepath;

    public HashtagsCategoriesAdapter(Context context,ArrayList<Discoverhashtags> discoverhashtagsList)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.discoverhashtagsList=discoverhashtagsList;
    }

    public void setVideobasepath(String videobasepath)
    {
        this.videobasepath=videobasepath;
    }

    public void setOnHashtagViewAllListener(OnHashtagViewAllListener onHashtagViewAllListener)
    {
        this.onHashtagViewAllListener=onHashtagViewAllListener;
    }
    public void setOnHashtagItemClickListener(HashtagsthumnailsAdapter.OnHashtagItemClickListener onHashtagItemClickListener)
    {
        this.onHashtagItemClickListener=onHashtagItemClickListener;
    }


    @Override
    public HashtagsCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hashtags_containermodel, parent, false);

        return new HashtagsCategoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HashtagsCategoriesAdapter.MyViewHolder holder, final int position)
    {
        final HashtagsCategoriesAdapter.MyViewHolder myViewHolder=holder;
        discoverhashtags=discoverhashtagsList.get(position);
        ArrayList<VideoModel> discoverhashtagvideosList=new ArrayList<>();
        if(discoverhashtags.getVideos()!=null && discoverhashtags.getVideos().size()>0)
          discoverhashtagvideosList.addAll(discoverhashtags.getVideos());
        holder.rv_hashtagscontainer.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        HashtagsthumnailsAdapter hashtagsthumnailsAdapter=new HashtagsthumnailsAdapter(context,discoverhashtagvideosList);
        hashtagsthumnailsAdapter.setVideobasepath(videobasepath);
        hashtagsthumnailsAdapter.setOnHashtagItemClickListener(onHashtagItemClickListener);
        holder.rv_hashtagscontainer.setAdapter(hashtagsthumnailsAdapter);

        holder.tv_hashtagname.setText(discoverhashtags.getHashTag());
        holder.tv_hashtagsviews.setText("124.1m");

        holder.layout_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onHashtagViewAllListener!=null)
                  onHashtagViewAllListener.onHashtagViewAll(discoverhashtagsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return discoverhashtagsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_hashtagname)
        TextView tv_hashtagname;
        @BindView(R.id.tv_hashtagsviews)
        TextView tv_hashtagsviews;
        @BindView(R.id.layout_viewall)
        RelativeLayout layout_viewall;
        @BindView(R.id.rv_hashtagscontainer)
        RecyclerView rv_hashtagscontainer;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnHashtagViewAllListener
    {
        void onHashtagViewAll(Discoverhashtags discoverhashtags);
    }

}
