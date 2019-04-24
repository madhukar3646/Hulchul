package com.app.hulchul.adapters;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
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
 * */

public class SongListitemAdapter extends RecyclerView.Adapter<SongListitemAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private ArrayList<ServerSong> songArrayList;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int last_index = -1,parentpos;
    private MyViewHolder myholder;
    private String songsBasepath;
    private OnSoundSelectionListener onSoundSelectionListener;

    public SongListitemAdapter(Context context,ArrayList<ServerSong> songArrayList,int parentpos)
    {
        this.context=context;
        this.songArrayList=songArrayList;
        this.parentpos=parentpos;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    public void setSongsBasepath(String basepath)
    {
        this.songsBasepath=basepath;
    }

    public void setSoundSelectionListener(OnSoundSelectionListener onSoundSelectionListener)
    {
        this.onSoundSelectionListener=onSoundSelectionListener;
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
        this.myholder=holder;
        setUpData(holder,position);
    }

    @Override
    public int getItemCount()
    {
        return songArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        private String songUri;
        @BindView(R.id.tv_songtitle)
        TextView tv_songtitle;
        @BindView(R.id.tv_artistname)
        TextView tv_artistname;
        @BindView(R.id.layout_songdetails)
        RelativeLayout layout_songdetails;
        @BindView(R.id.layout_checked)
        RelativeLayout layout_checked;
        @BindView(R.id.iv_favourite)
        ImageView iv_favourite;
        @BindView(R.id.iv_checked)
        ImageView iv_checked;
        @BindView(R.id.iv_songthumbnail)
        ImageView iv_songthumbnail;
        @BindView(R.id.iv_playpauseimage)
        ImageView iv_playpauseimage;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            layout_songdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songArrayList.get(position);

                    songUri = songsBasepath+songsModel.getFile();
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

            layout_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songArrayList.get(position);
                    if(onSoundSelectionListener!=null)
                        onSoundSelectionListener.onSoundSelected(songsModel);
                }
            });
        }

        private void markAllPaused() {
            for( int i=0; i < songArrayList.size(); i++ ){
                songArrayList.get(i).setPlaying(false);
                songArrayList.set(i,songArrayList.get(i));
            }
            notifyDataSetChanged();
        }

        private void markAllUnPaused() {
            for( int i=0; i < songArrayList.size(); i++ ){
                songArrayList.set(i,songArrayList.get(i));
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

    public void stopPlayerFromActivity()
    {
        if(myholder!=null)
            myholder.stopPlaying();
    }

    private void setUpData(MyViewHolder holder, int position)
    {
        ServerSong recording = songArrayList.get(position);
        holder.tv_songtitle.setText(recording.getName());
        holder.tv_artistname.setText(recording.getFile());
        holder.iv_songthumbnail.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

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

    public interface OnSoundSelectionListener
    {
        void onSoundSelected(ServerSong songsModel);
    }
}
