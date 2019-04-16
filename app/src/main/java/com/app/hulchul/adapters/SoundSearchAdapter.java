package com.app.hulchul.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.hulchul.R;
import com.app.hulchul.activities.SoundsSearchresultsActivity;
import com.app.hulchul.model.SoundSearchdata;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */

public class SoundSearchAdapter extends RecyclerView.Adapter<SoundSearchAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<SoundSearchdata> searchdataList;
    public SoundSearchAdapter(Context context,ArrayList<SoundSearchdata> searchdataList)
    {
        this.context=context;
        this.searchdataList=searchdataList;
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
        SoundSearchdata model=searchdataList.get(position);
        holder.tv_songtitle.setText(model.getName());
        holder.tv_authorname.setText(model.getFile());
        holder.layout_viewicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             context.startActivity(new Intent(context, SoundsSearchresultsActivity.class));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return searchdataList.size();
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
        @BindView(R.id.layout_viewicon)
        RelativeLayout layout_viewicon;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}