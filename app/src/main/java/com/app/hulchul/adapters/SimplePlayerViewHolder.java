package com.app.hulchul.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import im.ene.toro.ToroPlayer;
import im.ene.toro.ToroUtil;
import im.ene.toro.exoplayer.SimpleExoPlayerViewHelper;
import im.ene.toro.media.PlaybackInfo;
import im.ene.toro.widget.Container;

public class SimplePlayerViewHolder extends RecyclerView.ViewHolder implements ToroPlayer,ToroPlayer.EventListener{

    @BindView(R.id.player)
    SimpleExoPlayerView playerView;
    @BindView(R.id.card)
    RelativeLayout card;
    @BindView(R.id.iv_pauseresume)
    ImageView iv_pauseresume;

    @BindView(R.id.latest1_commentfrom)
    TextView latest1_commentfrom;
    @BindView(R.id.latest2_commentfrom)
    TextView latest2_commentfrom;
    @BindView(R.id.latest1_comment)
    TextView latest1_comment;
    @BindView(R.id.latest2_comment)
    TextView latest2_comment;
    @BindView(R.id.profile_image)
    CircleImageView profile_image;
    @BindView(R.id.iv_addfriend)
    ImageView iv_addfriend;
    @BindView(R.id.layout_like)
    LinearLayout layout_like;
    @BindView(R.id.layout_comments)
    LinearLayout layout_comments;
    @BindView(R.id.layout_share)
    LinearLayout layout_share;
    @BindView(R.id.layout_sendcomment)
    RelativeLayout layout_sendcomment;
    @BindView(R.id.tv_profilename)
    TextView tv_profilename;
    @BindView(R.id.tv_profilelikescount)
    TextView tv_profilelikescount;
    @BindView(R.id.tv_hashtag2)
    TextView tv_hashtag2;
    @BindView(R.id.tv_hashtag1)
    TextView tv_hashtag1;
    @BindView(R.id.iv_like)
    ImageView iv_like;
    @BindView(R.id.tv_likescount)
    TextView tv_likescount;
    @BindView(R.id.tv_commentscount)
    TextView tv_commentscount;
    @BindView(R.id.tv_sharescount)
    TextView tv_sharescount;
    @BindView(R.id.layout_abuse)
    LinearLayout layout_abuse;

    SimpleExoPlayerViewHelper helper;
    Uri mediaUri;
    boolean isPlay=true;

    public SimplePlayerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(helper==null)
                    return;
                if(isPlay && isPlaying())
                {
                    isPlay=false;
                    helper.pause();
                    iv_pauseresume.setVisibility(View.VISIBLE);
                }
                else {
                    isPlay=true;
                    helper.play();
                    iv_pauseresume.setVisibility(View.GONE);
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
