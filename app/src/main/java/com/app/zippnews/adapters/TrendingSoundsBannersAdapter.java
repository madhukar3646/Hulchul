package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.zippnews.R;
import com.app.zippnews.model.Soundbanners;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
/**
 * Created by admin on 4/20/2017.
 */

public class TrendingSoundsBannersAdapter extends RecyclerView.Adapter<TrendingSoundsBannersAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<Soundbanners> soundbannerslist;
    private OnBannerClickListener onBannerClickListener;

    public TrendingSoundsBannersAdapter(Context context,ArrayList<Soundbanners> soundbannerslist)
    {
        this.context=context;
        this.soundbannerslist=soundbannerslist;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    public void setOnBannerClickListener(OnBannerClickListener onBannerClickListener)
    {
        this.onBannerClickListener=onBannerClickListener;
    }

    @Override
    public TrendingSoundsBannersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trendingsoundsbanner_model, parent, false);

        return new TrendingSoundsBannersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrendingSoundsBannersAdapter.MyViewHolder holder, final int position)
    {
        final TrendingSoundsBannersAdapter.MyViewHolder myViewHolder=holder;

        Picasso.with(context).load(R.mipmap.sampleimage)
                .error(R.mipmap.placeholder)
                .into(holder.iv_hashtagthumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onBannerClickListener!=null)
                    onBannerClickListener.onBannerClick(soundbannerslist.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return soundbannerslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_hashtagthumbnail;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=(width*75)/100;
            itemView.getLayoutParams().height=(width*50)/100;
            iv_hashtagthumbnail=(ImageView)itemView.findViewById(R.id.iv_hashtagthumbnail);
        }
    }

    public interface OnBannerClickListener
    {
        void onBannerClick(Soundbanners model);
    }
}
