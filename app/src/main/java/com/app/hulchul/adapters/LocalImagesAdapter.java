package com.app.hulchul.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.app.hulchul.R;
import com.app.hulchul.model.Local_imagesmodel;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */

public class LocalImagesAdapter extends RecyclerView.Adapter<LocalImagesAdapter.MyViewHolder>
{
    private Context context;
    private int width;
    private ArrayList<Local_imagesmodel> listOfAllImages;
    private onImagesSelectionListener onImagesSelectionListener;

    public LocalImagesAdapter(Context context,ArrayList<Local_imagesmodel> listOfAllImages)
    {
        this.listOfAllImages=listOfAllImages;
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
    }

    public void setOnImagesSelectionListener(onImagesSelectionListener onImagesSelectionListener)
    {
       this.onImagesSelectionListener=onImagesSelectionListener;
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
        final Local_imagesmodel model=listOfAllImages.get(position);
        Glide.with(context).load(new File(model.getPath()))
                .error(R.mipmap.placeholder)
                .into(holder.iv_catimage);

        if(model.isChecked())
            holder.iv_selectimage.setVisibility(View.VISIBLE);
        else
            holder.iv_selectimage.setVisibility(View.GONE);

        holder.iv_catimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(model.isChecked())
                listOfAllImages.get(position).setChecked(false);
              else
                  listOfAllImages.get(position).setChecked(true);

              if(onImagesSelectionListener!=null)
                  onImagesSelectionListener.onImageSelected(listOfAllImages.get(position).getPath());
              notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return listOfAllImages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public ImageView iv_catimage,iv_selectimage;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            itemView.getLayoutParams().width=width/3;
            itemView.getLayoutParams().height=(width/3)+((width/3)/3);
            iv_catimage=(ImageView)itemView.findViewById(R.id.iv_catimage);
            iv_selectimage=(ImageView)itemView.findViewById(R.id.iv_selectimage);
        }
    }

    public interface onImagesSelectionListener
    {
        public void onImageSelected(String path);
    }
}
