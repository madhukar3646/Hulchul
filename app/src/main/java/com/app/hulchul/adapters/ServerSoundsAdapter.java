package com.app.hulchul.adapters;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.widget.RecyclerView;
import android.transition.TransitionManager;
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
 * Created by Manish on 10/3/2017.
 */

public class ServerSoundsAdapter extends RecyclerView.Adapter<ServerSoundsAdapter.ViewHolder>{

    private Activity context;
    private ArrayList<ServerSong> songsModelArrayList;
    private MediaPlayer mPlayer;
    private boolean isPlaying = false;
    private int last_index = -1;
    private int width;
    private ViewHolder myholder;
    private OnSoundSelectionListener onSoundSelectionListener;
    private String songsBasepath;

    public ServerSoundsAdapter(Activity context, ArrayList<ServerSong> songsModelArrayList){
        this.context = context;
        this.songsModelArrayList = songsModelArrayList;
        this.width=context.getResources().getDisplayMetrics().widthPixels;
    }

    public void setSoundSelectionListener(OnSoundSelectionListener onSoundSelectionListener)
    {
        this.onSoundSelectionListener=onSoundSelectionListener;
    }

    public void setSongsBasepath(String basepath)
    {
        this.songsBasepath=basepath;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.mysounds_model, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        this.myholder=holder;
        setUpData(holder,position);

    }

    public void stopPlayerFromActivity()
    {
        if(myholder!=null)
            myholder.stopPlaying();
    }

    private void setUpData(ViewHolder holder, int position) {

        ServerSong recording = songsModelArrayList.get(position);
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

    @Override
    public int getItemCount() {
        return songsModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private String songUri;
        @BindView(R.id.layout_checked)
        RelativeLayout layout_checked;
        @BindView(R.id.iv_checked)
        ImageView iv_checked;
        @BindView(R.id.iv_favourite)
        ImageView iv_favourite;
        @BindView(R.id.layout_songdetails)
        RelativeLayout layout_songdetails;
        @BindView(R.id.iv_songthumbnail)
        ImageView iv_songthumbnail;
        @BindView(R.id.iv_playpauseimage)
        ImageView iv_playpauseimage;
        @BindView(R.id.tv_songtitle)
        TextView tv_songtitle;
        @BindView(R.id.tv_artistname)
        TextView tv_artistname;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            iv_songthumbnail.getLayoutParams().width=width/5;
            iv_songthumbnail.getLayoutParams().height=width/5;

            layout_songdetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songsModelArrayList.get(position);

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

            iv_checked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songsModelArrayList.get(position);
                    if(onSoundSelectionListener!=null)
                        onSoundSelectionListener.onSoundSelected(songsModel);
                }
            });

            iv_favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    ServerSong songsModel = songsModelArrayList.get(position);
                  if(onSoundSelectionListener!=null)
                      onSoundSelectionListener.onFavouriteClick(songsModel,position);
                }
            });
        }

        private void markAllPaused() {
            for( int i=0; i < songsModelArrayList.size(); i++ ){
                songsModelArrayList.get(i).setPlaying(false);
                songsModelArrayList.set(i,songsModelArrayList.get(i));
            }
            notifyDataSetChanged();
        }

        private void markAllUnPaused() {
            for( int i=0; i < songsModelArrayList.size(); i++ ){
                songsModelArrayList.set(i,songsModelArrayList.get(i));
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
                    /*isPlaying = false;
                    audio.setPlaying(false);
                    notifyItemChanged(position);*/
                    startPlaying(audio,position);
                }
            });
        }
    }

    public void updateFavourite(int pos)
    {

    }

    public interface OnSoundSelectionListener
    {
        void onSoundSelected(ServerSong songsModel);
        void onFavouriteClick(ServerSong song,int pos);
    }
}
