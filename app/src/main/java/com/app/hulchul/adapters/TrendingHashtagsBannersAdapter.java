package com.app.hulchul.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.app.hulchul.activities.HashtagSearchresultsActivity;
import com.app.hulchul.model.Hashtagbanner;
import com.app.hulchul.utils.ApiUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 4/20/2017.
 */

public class TrendingHashtagsBannersAdapter extends RecyclerView.Adapter<TrendingHashtagsBannersAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    List<Hashtagbanner> list=new ArrayList<Hashtagbanner>();

    public TrendingHashtagsBannersAdapter(Context context,List<Hashtagbanner> list)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
        this.list=list;
    }

    @Override
    public TrendingHashtagsBannersAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trendinghashdiscovermodel, parent, false);

        return new TrendingHashtagsBannersAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TrendingHashtagsBannersAdapter.MyViewHolder holder, final int position)
    {
        final TrendingHashtagsBannersAdapter.MyViewHolder myViewHolder=holder;
        Hashtagbanner hashtagbanner=list.get(position);
        Picasso.with(context).load(ApiUrls.IMAGEBASEPATH+hashtagbanner.getImage())
                .error(R.mipmap.placeholder)
                .into(holder.iv_hashtagthumbnail);

        holder.iv_hashtagthumbnail.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, HashtagSearchresultsActivity.class);
                intent.putExtra("hashtag",hashtagbanner.getHashTag());
                intent.putExtra("image",ApiUrls.IMAGEBASEPATH+hashtagbanner.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        if(list==null)
            return 0;

        return list.size();
    }



    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_hashtagthumbnail;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=(width*35)/100;
            itemView.getLayoutParams().height=(width*50)/100;
            iv_hashtagthumbnail=(ImageView)itemView.findViewById(R.id.iv_hashtagthumbnail);
        }
    }

}
