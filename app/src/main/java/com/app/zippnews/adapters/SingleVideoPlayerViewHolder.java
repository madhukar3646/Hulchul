package com.app.zippnews.adapters;

import android.net.Uri;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.ExoPlayerViewHelper;
import im.ene.toro.exoplayer.Playable;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

import static com.google.android.exoplayer2.util.RepeatModeUtil.REPEAT_TOGGLE_MODE_ALL;

public class SingleVideoPlayerViewHolder extends RecyclerView.ViewHolder implements ToroPlayer,ToroPlayer.EventListener{

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;
    ExoPlayerViewHelper helper;
    Uri mediaUri;
    OnVideoCompletedListener onVideoCompletedListener;
    public Handler handler=new Handler();
    @BindView(R.id.iv_thumb)
    ImageView iv_thumb;

    public SingleVideoPlayerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        playerView.setRepeatToggleModes(REPEAT_TOGGLE_MODE_ALL);
    }

    @Override public View getPlayerView() {
        return playerView;
    }

    @Override public PlaybackInfo getCurrentPlaybackInfo() {
        return helper != null ? helper.getLatestPlaybackInfo() : new PlaybackInfo();
    }

    @Override
    public void initialize(Container container, PlaybackInfo playbackInfo) {
        if (helper == null) {
            helper = new ExoPlayerViewHelper(container, this, mediaUri);
            iv_thumb.setVisibility(View.GONE);
            handler.post(thumbnaildisplay);
            helper.addEventListener(new Playable.EventListener() {
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
                    if(playerView.getPlayer()!=null)
                        playerView.getPlayer().setRepeatMode(Player.REPEAT_MODE_ALL);
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

                @Override
                public void onMetadata(Metadata metadata) {

                }

                @Override
                public void onCues(List<Cue> cues) {

                }

                @Override
                public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

                }

                @Override
                public void onRenderedFirstFrame() {

                }
            });
        }
        helper.initialize(container,playbackInfo);
        helper.addPlayerEventListener(this);
    }

    @Override public void play() {
        if (helper != null) helper.play();
    }

    @Override public void pause() {
        if (helper != null) helper.pause();
    }

    @Override public boolean isPlaying() {
        return helper != null && helper.isPlaying();
    }

    @Override public void release() {
        if (helper != null) {
            helper.release();
            helper = null;
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

    @Override
    public void onFirstFrameRendered() {

    }

    @Override
    public void onBuffering() {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onCompleted() {
        if(onVideoCompletedListener!=null)
            onVideoCompletedListener.onVideoCompleted();
        Log.e("on completed","on completed");
    }

    public void setOnVideoCompletedListener(OnVideoCompletedListener onVideoCompletedListener)
    {
        this.onVideoCompletedListener=onVideoCompletedListener;
    }

    public interface OnVideoCompletedListener
    {
        void onVideoCompleted();
    }

    Runnable thumbnaildisplay=new Runnable() {

        @Override
        public void run() {
            if(playerView!=null && playerView.getPlayer()!=null)
            {
                long duration=playerView.getPlayer().getCurrentPosition();
                if(duration>0)
                {
                    handler.removeCallbacks(thumbnaildisplay);
                    iv_thumb.setVisibility(View.GONE);
                }
                else {
                    iv_thumb.setVisibility(View.VISIBLE);
                    handler.postDelayed(thumbnaildisplay, 50);
                }
            }
            else {
                handler.postDelayed(thumbnaildisplay, 50);
            }
        }
    };
}
