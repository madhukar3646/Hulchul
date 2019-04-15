package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.app.hulchul.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */

public class SoundSearchAdapter extends RecyclerView.Adapter<SoundSearchAdapter.MyViewHolder>
{
    private Context context;
    public SoundSearchAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public SoundSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.soundsearch_model, parent, false);
        return new SoundSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SoundSearchAdapter.MyViewHolder holder, final int position)
    {
        final SoundSearchAdapter.MyViewHolder myViewHolder=holder;
    }

    @Override
    public int getItemCount()
    {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.song_image)
        ImageView song_image;
        @BindView(R.id.tv_songtitle)
        TextView tv_songtitle;
        @BindView(R.id.tv_authorname)
        TextView tv_authorname;
        @BindView(R.id.tv_videoscount)
        TextView tv_videoscount;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
