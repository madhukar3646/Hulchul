package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.ServerSong;
import com.app.hulchul.model.Songlistmodel;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by admin on 4/20/2017.
 */
public class SongslistContainerAdapter extends RecyclerView.Adapter<SongslistContainerAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    ArrayList<Songlistmodel> songsContainerlist;
    private OnviewAllsongsClickListener onviewAllsongsClickListener;
    private SongListitemAdapter.OnSoundSelectionListener soundSelectionListener;
    private String musicbasepath;

    public SongslistContainerAdapter(Context context, ArrayList<Songlistmodel> songsContainerlist)
    {
        this.context=context;
        this.songsContainerlist=songsContainerlist;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    public void setOnviewAllsongsClickListener(OnviewAllsongsClickListener onviewAllsongsClickListener)
    {
        this.onviewAllsongsClickListener=onviewAllsongsClickListener;
    }

    public void setSoundSelectionListener(SongListitemAdapter.OnSoundSelectionListener soundSelectionListener)
    {
        this.soundSelectionListener=soundSelectionListener;
    }

    public void setMusicBasepath(String musicBasepath)
    {
      this.musicbasepath=musicBasepath;
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
        Songlistmodel songlistmodel=songsContainerlist.get(position);
        ArrayList<ServerSong> songslist=new ArrayList<>();
        if(songlistmodel.getSongs()!=null && songlistmodel.getSongs().size()>0)
            songslist.addAll(songlistmodel.getSongs());
        SongListitemAdapter songListitemAdapter=new SongListitemAdapter(context,songslist,position);
        songListitemAdapter.setSoundSelectionListener(soundSelectionListener);
        songListitemAdapter.setSongsBasepath(musicbasepath);
        holder.rv_songslist.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false));
        holder.rv_songslist.setAdapter(songListitemAdapter);
        holder.tv_songtitle.setText(songlistmodel.getName());

        holder.layout_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(onviewAllsongsClickListener!=null)
                  onviewAllsongsClickListener.onViewAllSongs(songsContainerlist.get(position));
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return songsContainerlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.rv_songslist)
        RecyclerView rv_songslist;
        @BindView(R.id.tv_songtitle)
        TextView tv_songtitle;
        @BindView(R.id.layout_viewall)
        RelativeLayout layout_viewall;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnviewAllsongsClickListener
    {
        void onViewAllSongs(Songlistmodel model);
    }
}
