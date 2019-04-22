package com.app.hulchul.activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.app.hulchul.R;
/**
 * Created by admin on 4/20/2017.
 * */

public class SongListitemAdapter extends RecyclerView.Adapter<SongListitemAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;

    public SongListitemAdapter(Context context)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @Override
    public SongListitemAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songslistitemmodel, parent, false);

        return new SongListitemAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongListitemAdapter.MyViewHolder holder, final int position)
    {
        final SongListitemAdapter.MyViewHolder myViewHolder=holder;
    }

    @Override
    public int getItemCount()
    {
        return 6;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public MyViewHolder(View itemView)
        {
            super(itemView);
        }
    }
}
