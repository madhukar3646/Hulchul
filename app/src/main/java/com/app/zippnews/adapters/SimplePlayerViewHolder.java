package com.app.zippnews.adapters;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.metadata.Metadata;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.text.Cue;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static com.google.android.exoplayer2.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL;

public class SimplePlayerViewHolder extends RecyclerView.ViewHolder implements ToroPlayer,ToroPlayer.EventListener{

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;
    @BindView(R.id.card)
    RelativeLayout card;
    @BindView(R.id.iv_pauseresume)
    ImageView iv_pauseresume;
    @BindView(R.id.layout_comments)
    LinearLayout layout_comments;
    @BindView(R.id.layout_share)
    LinearLayout layout_share;
    @BindView(R.id.tv_commentscount)
    TextView tv_commentscount;
    @BindView(R.id.tv_sharescount)
    TextView tv_sharescount;
    @BindView(R.id.layout_abuse)
    LinearLayout layout_abuse;
    @BindView(R.id.layout_favourites)
    LinearLayout layout_favourites;
    @BindView(R.id.iv_favourite)
    ImageView iv_favourite;
    @BindView(R.id.layout_doubletap)
    RelativeLayout layout_doubletap;
    @BindView(R.id.tv_musicscroll)
    TextView tv_musicscroll;
    @BindView(R.id.iv_likeanim)
    ImageView iv_likeanim;
    @BindView(R.id.iv_thumb)
    ImageView iv_thumb;
    @BindView(R.id.layout_dubvideo)
    LinearLayout layout_dubvideo;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;

    ExoPlayerViewHelper helper;
    Uri mediaUri;
    boolean isPlay=true;
    public MediaPlayer musicplayer;
    private String musicpath;
    public int musicposition=0;

    @BindView(R.id.rv_hashtagslist)
    RecyclerView rv_hashtagslist;
    public Handler handler=new Handler();

    public SimplePlayerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        iv_thumb.setVisibility(View.VISIBLE);
        tv_musicscroll.setSelected(true);
        playerView.setRepeatToggleModes(REPEAT_TOGGLE_MODE_ALL);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helper==null)
                    return;
                if(isPlay && isPlaying())
                {
                    isPlay=false;
                    helper.pause();
                    //iv_pauseresume.setVisibility(View.VISIBLE);
                    if(musicplayer!=null) {
                        musicplayer.pause();
                        musicposition = musicplayer.getCurrentPosition();
                    }
                }
                else {
                    isPlay=true;
                    helper.play();
                    //iv_pauseresume.setVisibility(View.GONE);
                    if(musicplayer!=null) {
                        musicplayer.seekTo(musicposition);
                        musicplayer.start();
                    }
                }
            }
        });
    }

    @Override public View getPlayerView() {
        return playerView;
    }

    @Override public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(Container container, PlaybackInfo playbackInfo) {
        Log.e("initialize","initialize");
        iv_thumb.setVisibility(View.VISIBLE);
        iv_pauseresume.setVisibility(View.GONE);
        if (helper == null) {
            helper = new ExoPlayerViewHelper(container, this, mediaUri);
            helper.addEventListener(new Playable.EventListener() {
                @Override
                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

                }

                @Override
                public void onRenderedFirstFrame() {

                }

                @Override
                public void onCues(List<Cue> cues) {

                }

                @Override
                public void onMetadata(Metadata metadata) {

                }

                @Override
                public void onTimelineChanged(Timeline timeline, @Nullable Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    Log.e("onPlayerStateChanged "+playWhenReady,""+playbackState);
                    if(playerView.getPlayer()!=null)
                        playerView.getPlayer().setRepeatMode(Player.REPEAT_MODE_ALL);
                       if(playWhenReady){

                                 }else {

                                 }
                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });
        }
        helper.initialize(container,playbackInfo);
        helper.addPlayerEventListener(this);
    }

    @Override public void play() {
        if (helper != null) helper.play();
        if(musicpath!=null) {
            if(musicplayer!=null)
            musicplayer.release();
            musicplayer=null;
            if(musicplayer==null) {
                musicplayer = new MediaPlayer();
                try {
                    musicplayer.setDataSource(musicpath);
                    musicplayer.prepare();
                    musicplayer.start();
                } catch (IOException e) {
                    Log.e("LOG_TAG", "prepare() failed");
                }
            }
        }
    }

    @Override public void pause() {
        if (helper != null) helper.pause();
        if(musicplayer!=null) {
            musicplayer.pause();
            musicposition = musicplayer.getCurrentPosition();
        }
    }

    @Override public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override public void release() {
        if (helper != null) {
            helper.release();
            helper = null;

            if(musicplayer!=null)
            musicplayer.release();
        }
    }

    @Override public boolean wantsToPlay() {
        return ToroUtil.visibleAreaOffset(this, itemView.getParent()) >= 0.85;
    }

    @Override public int getPlayerOrder() {
        return getAdapterPosition();
    }

    void bind(Uri media) {
        this.mediaUri = media;
    }

    void bindMusic(String url) {
        musicplayer=null;
        this.musicpath = url;
    }

    @Override
    public void onFirstFrameRendered() {
        iv_thumb.setVisibility(View.GONE);
     Log.e("onFirstFrameRendered","onFirstFrameRendered");
    }

    @Override
    public void onBuffering() {
        Log.e("onBuffering","onBuffering");
    }

    @Override
    public void onPlaying() {
        iv_pauseresume.setVisibility(View.GONE);
        Log.e("onPlaying","onPlaying");
    }

    @Override
    public void onPaused() {
        iv_pauseresume.setVisibility(View.VISIBLE);
        Log.e("onPaused","onPaused");
    }

    @Override
    public void onCompleted() {
        Log.e("on completed","on completed");
    }
}
