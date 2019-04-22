package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.activities.SongListitemAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by admin on 4/20/2017.
 */
public class SongslistContainerAdapter extends RecyclerView.Adapter<SongslistContainerAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;

    public SongslistContainerAdapter(Context context)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @Override
    public SongslistContainerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.songslistcontainermodel, parent, false);

        return new SongslistContainerAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongslistContainerAdapter.MyViewHolder holder, final int position)
    {
        final SongslistContainerAdapter.MyViewHolder myViewHolder=holder;
        SongListitemAdapter songListitemAdapter=new SongListitemAdapter(context);
        holder.rv_songslist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        holder.rv_songslist.setAdapter(songListitemAdapter);
    }

    @Override
    public int getItemCount()
    {
        return 6;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rv_songslist)
        RecyclerView rv_songslist;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
