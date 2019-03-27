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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */

public class LocalImagesAdapter extends RecyclerView.Adapter<LocalImagesAdapter.MyViewHolder>
{
    private Context context;
    private int width;
    private ArrayList<String> listOfAllImages;

    public LocalImagesAdapter(Context context,ArrayList<String> listOfAllImages)
    {
        this.listOfAllImages=listOfAllImages;
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
    }

    @Override
    public LocalImagesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.localimages_model, parent, false);

        return new LocalImagesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final LocalImagesAdapter.MyViewHolder holder, final int position)
    {
        final LocalImagesAdapter.MyViewHolder myViewHolder=holder;

        Picasso.with(context).load(listOfAllImages.get(position))
                .error(R.mipmap.placeholder)
                .into(holder.iv_catimage);
    }

    @Override
    public int getItemCount()
    {
        return listOfAllImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_catimage;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            itemView.getLayoutParams().height=(width/3)+((width/3)/3);
            iv_catimage=(ImageView)itemView.findViewById(R.id.iv_catimage);
        }
    }
}
