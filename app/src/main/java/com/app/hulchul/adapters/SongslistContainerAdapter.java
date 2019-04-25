package com.app.hulchul.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.ServerSong;

import java.io.IOException;
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
    private ArrayList<ServerSong> songsArraylist;
    private OnviewAllsongsClickListener onviewAllsongsClickListener;
    private String musicbasepath;

    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int last_index = -1;
    private SongslistContainerAdapter.MyViewHolder myholder;

    public SongslistContainerAdapter(Context context, ArrayList<ServerSong> songsArraylist)
    {
        this.context=context;
        this.songsArraylist=songsArraylist;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    public void setOnviewAllsongsClickListener(OnviewAllsongsClickListener onviewAllsongsClickListener)
    {
        this.onviewAllsongsClickListener=onviewAllsongsClickListener;
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
        this.myholder=holder;
        ServerSong songlistmodel=songsArraylist.get(position);
        if(songlistmodel.isHeader())
        {
           holder.layout_header.setVisibility(View.VISIBLE);
           holder.layout_child.setVisibility(View.GONE);
        }
        else{
            holder.layout_header.setVisibility(View.GONE);
            holder.layout_child.setVisibility(View.VISIBLE);
        }

        holder.tv_songplaylist.setText(songlistmodel.getName());
        holder.tv_songtitle.setText(songlistmodel.getName());
        holder.tv_artistname.setText(songlistmodel.getFile());
        holder.iv_songthumbnail.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

        holder.layout_viewall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(onviewAllsongsClickListener!=null)
                  onviewAllsongsClickListener.onViewAllSongs(songsArraylist.get(position));
            }
        });

        setUpData(holder,position);
    }

    @Override
    public int getItemCount()
    {
        return songsArraylist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private String songUri;
        @BindView(R.id.layout_header)
        RelativeLayout layout_header;
        @BindView(R.id.layout_child)
        CardView layout_child;
        @BindView(R.id.tv_songplaylist)
        TextView tv_songplaylist;
        @BindView(R.id.tv_songtitle)
        TextView tv_songtitle;
        @BindView(R.id.layout_viewall)
        RelativeLayout layout_viewall;
        @BindView(R.id.layout_checked)
        RelativeLayout layout_checked;
        @BindView(R.id.iv_favourite)
        ImageView iv_favourite;
        @BindView(R.id.iv_checked)
        ImageView iv_checked;
        @BindView(R.id.layout_songdetails)
        RelativeLayout layout_songdetails;
        @BindView(R.id.iv_songthumbnail)
        ImageView iv_songthumbnail;
        @BindView(R.id.iv_playpauseimage)
        ImageView iv_playpauseimage;
        @BindView(R.id.tv_artistname)
        TextView tv_artistname;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            layout_songdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songsArraylist.get(position);

                    songUri = musicbasepath+songsModel.getFile();
                    if( isPlaying ){
                        stopPlaying();
                        if( position == last_index ){
                            songsModel.setPlaying(false);
                            stopPlaying();
                            notifyItemChanged(position);
                        }else{
                            markAllPaused();
                            songsModel.setPlaying(true);
                            notifyItemChanged(position);
                            startPlaying(songsModel,position);
                            last_index = position;
                        }
                    }else {
                        if( songsModel.isPlaying() ){
                            songsModel.setPlaying(false);
                            stopPlaying();
                            Log.d("isPlayin","True");
                        }else {
                            startPlaying(songsModel,position);
                            songsModel.setPlaying(true);
                        }
                        notifyItemChanged(position);
                        last_index = position;
                    }
                }
            });

            iv_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songsArraylist.get(position);
                    if(onviewAllsongsClickListener!=null)
                        onviewAllsongsClickListener.onSoundSelected(songsModel);
                }
            });

            iv_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songsArraylist.get(position);
                  if(onviewAllsongsClickListener!=null)
                      onviewAllsongsClickListener.onFavouriteClick(songsModel,position);
                }
            });
        }

        private void markAllPaused() {
            for( int i=0; i < songsArraylist.size(); i++ ){
                songsArraylist.get(i).setPlaying(false);
                songsArraylist.set(i,songsArraylist.get(i));
            }
            notifyDataSetChanged();
        }

        private void markAllUnPaused() {
            for( int i=0; i < songsArraylist.size(); i++ ){
                songsArraylist.set(i,songsArraylist.get(i));
            }
            notifyDataSetChanged();
        }

        public void stopPlaying() {
            try{
                mPlayer.release();
            }catch (Exception e){
                e.printStackTrace();
            }
            mPlayer = null;
            isPlaying = false;
        }

        private void startPlaying(final ServerSong audio, final int position) {
            mPlayer = new MediaPlayer();
            try {
                Log.e("song url",songUri);
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(songUri);
                mPlayer.prepare();
                mPlayer.start();
            } catch (IOException e) {
                Log.e("LOG_TAG", "prepare() failed");
            }
            //showing the pause button
            isPlaying = true;

            mPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    startPlaying(audio,position);
                }
            });
        }
    }

    private void setUpData(SongslistContainerAdapter.MyViewHolder holder, int position)
    {
        ServerSong recording = songsArraylist.get(position);
        if( recording.isPlaying()){
            holder.iv_playpauseimage.setImageResource(R.drawable.ic_pause);
            holder.layout_checked.setVisibility(View.VISIBLE);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
        }else{
            holder.iv_playpauseimage.setImageResource(R.mipmap.play_video);
            holder.layout_checked.setVisibility(View.GONE);
            TransitionManager.beginDelayedTransition((ViewGroup) holder.itemView);
        }
    }

    public void stopPlayerFromActivity()
    {
        if(myholder!=null)
            myholder.stopPlaying();
    }

    public void updateFavourite(int pos)
    {

    }

    public interface OnviewAllsongsClickListener
    {
        void onViewAllSongs(ServerSong model);
        void onSoundSelected(ServerSong songsModel);
        void onFavouriteClick(ServerSong song,int pos);
    }
}
