package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.app.hulchul.R;
import com.squareup.picasso.Picasso;

/**
 * Created by admin on 4/20/2017.
 */

public class HashtagsthumnailsAdapter extends RecyclerView.Adapter<HashtagsthumnailsAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;

    public HashtagsthumnailsAdapter(Context context)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @Override
    public HashtagsthumnailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hashtagthumbnail_model, parent, false);

        return new HashtagsthumnailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HashtagsthumnailsAdapter.MyViewHolder holder, final int position)
    {
        final HashtagsthumnailsAdapter.MyViewHolder myViewHolder=holder;

        Picasso.with(context).load(R.mipmap.sampleimage)
                .error(R.mipmap.placeholder)
                .into(holder.iv_hashtagthumbnail);
    }

    @Override
    public int getItemCount()
    {
        return 30;
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
}
