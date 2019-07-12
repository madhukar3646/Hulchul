package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.zippnews.R;
import com.app.zippnews.model.SoundSearchdata;

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
    private OnSoundSelectedListener onSoundSelectedListener;

    public SoundSearchAdapter(Context context,ArrayList<SoundSearchdata> searchdataList)
    {
        this.context=context;
        this.searchdataList=searchdataList;
    }

    public void setOnSoundSelectedListener(OnSoundSelectedListener onSoundSelectedListener)
    {
        this.onSoundSelectedListener=onSoundSelectedListener;
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
                if(onSoundSelectedListener!=null)
                    onSoundSelectedListener.onSoundSelected(searchdataList.get(position));
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

    public interface OnSoundSelectedListener
    {
        void onSoundSelected(SoundSearchdata soundSearchdata);
    }
}
