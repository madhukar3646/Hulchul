package com.app.hulchul.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.app.hulchul.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.SimpleExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class SingleVideoPlayerViewHolder extends RecyclerView.ViewHolder implements ToroPlayer,ToroPlayer.EventListener{

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;
    SimpleExoPlayerViewHelper helper;
    Uri mediaUri;

    public SingleVideoPlayerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
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
            helper = new SimpleExoPlayerViewHelper(container, this, mediaUri);
        }
        helper.initialize(playbackInfo);
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
    public void onBuffering() {

    }

    @Override
    public void onPlaying() {

    }

    @Override
    public void onPaused() {

    }

    @Override
    public void onCompleted(Container container, ToroPlayer player) {
      play();
      Log.e("on completed","on completed");
    }
}
