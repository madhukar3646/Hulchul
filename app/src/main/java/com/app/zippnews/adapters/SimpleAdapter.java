package com.app.zippnews.adapters;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.app.zippnews.R;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.utils.ApiUrls;
import com.app.zippnews.utils.RecyclerviewTapListeners;
import com.app.zippnews.utils.SessionManagement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.app.zippnews.utils.ApiUrls.VIDEOSHAREBASEPATH;

public class SimpleAdapter extends RecyclerView.Adapter<SimplePlayerViewHolder> implements HomeScreenHashtagsAdapter.OnHashtagClickListener, RecyclerviewTapListeners {

    private ArrayList<VideoModel> modelArrayList;
    private Context context;
    private SessionManagement sessionManagement;
    private VideoActionsListener videoActionsListener;
    private String videobasepath;
    private String audiobasepath;
    public int position;

    public SimpleAdapter(Context context, ArrayList<VideoModel> modelArrayList)
    {
        this.modelArrayList=modelArrayList;
        this.context=context;
        sessionManagement=new SessionManagement(context);
    }

    public void setBasepaths(String videobasepath,String audiobasepath)
    {
        this.videobasepath=videobasepath;
        this.audiobasepath=audiobasepath;
    }

    @Override public SimplePlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_exoplayer_basic, parent, false);
        return new SimplePlayerViewHolder(view);
    }

    @Override public void onBindViewHolder(final SimplePlayerViewHolder holder, final int position) {
        this.position=position;
        VideoModel videoModel=modelArrayList.get(position);
        Log.e("video id",""+videoModel.getId());
        holder.iv_thumb.setVisibility(View.VISIBLE);

        Picasso.with(context).load(ApiUrls.PROFILEBASEPATH+videoModel.getPhoto()).placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.profile_image);

        Picasso.with(context).load(ApiUrls.VIDEOTHUMBNAIL+getThumbnailpath(videoModel.getVideo())).error(R.drawable.zippy_screen).placeholder(R.drawable.zippy_screen).into(holder.iv_thumb);
        holder.iv_likeanim.setVisibility(View.GONE);
        Log.e("video url is",videoModel.getVideo());
        /*if(position==0)
            holder.bind(Uri.parse("http://d38ck6wnuh16mt.cloudfront.net/508206f6-7bbc-4951-a92c-57f98bf8cb71/hls/335121756618315141210682.m3u8") *//* FIXME use real data *//*);
        else*/
          holder.bind(Uri.parse(videobasepath+videoModel.getVideo()) /* FIXME use real data */);
        if(videoModel.getSongfile()==null || videoModel.getSongfile().equalsIgnoreCase("null"))
            holder.bindMusic(null);
        else
            holder.bindMusic(audiobasepath+ videoModel.getSongfile());

        ArrayList<String> tagslist=getHashtagslist(videoModel.getHashTag());
        HomeScreenHashtagsAdapter hashtagsAdapter=new HomeScreenHashtagsAdapter(context,tagslist);
        hashtagsAdapter.setOnHashtagClickListener(this);
        holder.rv_hashtagslist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rv_hashtagslist.setAdapter(hashtagsAdapter);
        holder.tv_musicscroll.setSelected(true);
        holder.tv_musicscroll.setText(videoModel.getTitle());

       /* RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(4000);
        rotate.setRepeatCount(Animation.INFINITE);
        holder.iv_musicprofile.startAnimation(rotate);*/

        if(videoModel.getFavouritestatus()!=null)
        {
            if (videoModel.getFavouritestatus().equalsIgnoreCase("0"))
                holder.iv_favourite.setImageResource(R.mipmap.fav_a_w);
            else
                holder.iv_favourite.setImageResource(R.mipmap.fav_a_r);
        }

        if(videoModel.getShareCount()==null || videoModel.getShareCount().equalsIgnoreCase("null"))
            holder.tv_sharescount.setText("0");
        else
            holder.tv_sharescount.setText(videoModel.getShareCount());

        if(videoModel.getCommentCount()==null || videoModel.getCommentCount().equalsIgnoreCase("null"))
            holder.tv_commentscount.setText("0");
        else
            holder.tv_commentscount.setText(modelArrayList.get(position).getCommentCount());

        holder.layout_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(videoActionsListener!=null)
                   videoActionsListener.onCommentsClicked(holder,position,modelArrayList.get(position).getId(),modelArrayList.get(position).getCommentCount());
            }
        });

        holder.layout_favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                {
                    videoActionsListener.onFavouriteClicked(holder,modelArrayList.get(position).getId(),position);
                }
            }
        });

       holder.layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //*"http://testingmadesimple.org/training_app/uploads/userVideos/"*//*
                if(videoActionsListener!=null)
                    videoActionsListener.onShareClicked(VIDEOSHAREBASEPATH + modelArrayList.get(position).getVideo(), holder, modelArrayList.get(position).getId(), position);
            }
        });

        holder.layout_abuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(videoActionsListener!=null)
                   videoActionsListener.onAbuseClicked(modelArrayList.get(position));
            }
        });

        holder.layout_dubvideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                    videoActionsListener.onProfileClicked(modelArrayList.get(position));
            }
        });
    }

    @Override public int getItemCount() {
        return modelArrayList.size();
    }

    public void setVideoActionsListener(VideoActionsListener videoActionsListener)
    {
      this.videoActionsListener=videoActionsListener;
    }

    public void updateLike(SimplePlayerViewHolder holder,int pos)
    {
        int likescount=Integer.valueOf(modelArrayList.get(pos).getLikes());
        if(modelArrayList.get(pos).getLikestatus().equalsIgnoreCase("0")) {
            modelArrayList.get(pos).setLikes(""+(likescount+1));
            modelArrayList.get(pos).setLikestatus("1");
        }
        else {
            modelArrayList.get(pos).setLikes(""+(likescount-1));
            modelArrayList.get(pos).setLikestatus("0");
        }
    }

    public void updateFavourite(SimplePlayerViewHolder holder,int pos)
    {
        if(modelArrayList.get(pos).getFavouritestatus()==null || modelArrayList.get(pos).getFavouritestatus().equalsIgnoreCase("null"))
            modelArrayList.get(pos).setFavouritestatus("0");
        if(modelArrayList.get(pos).getFavouritestatus().equalsIgnoreCase("0")) {
            modelArrayList.get(pos).setFavouritestatus("1");
            holder.iv_favourite.setImageResource(R.mipmap.fav_a_r);
        }
        else {
            modelArrayList.get(pos).setFavouritestatus("0");
            holder.iv_favourite.setImageResource(R.mipmap.fav_a_w);
        }
    }

    public void updateFollows(SimplePlayerViewHolder holder,int pos)
    {
        int followscount;
        if(modelArrayList.get(pos).getFollowersCount()==null || modelArrayList.get(pos).getFollowersCount().equalsIgnoreCase("null"))
            followscount=0;
        else
           followscount=Integer.valueOf(modelArrayList.get(pos).getFollowersCount());

        if(modelArrayList.get(pos).getFollwerstatus()==null || modelArrayList.get(pos).getFollwerstatus().equalsIgnoreCase("null"))
            modelArrayList.get(pos).setFollwerstatus("0");

        if(modelArrayList.get(pos).getFollwerstatus().equalsIgnoreCase("0")) {
            modelArrayList.get(pos).setFollowersCount(""+(followscount+1));
            modelArrayList.get(pos).setFollwerstatus("1");
            setFollowCountForAllVideostoThisUser(modelArrayList.get(pos).getUserId(),"1",""+(followscount+1));
        }
        else {
            modelArrayList.get(pos).setFollowersCount(""+(followscount-1));
            modelArrayList.get(pos).setFollwerstatus("0");
            setFollowCountForAllVideostoThisUser(modelArrayList.get(pos).getUserId(),"0",""+(followscount-1));
        }
    }

    private void setFollowCountForAllVideostoThisUser(String userid,String followstatus,String followcount)
    {
        for(int i=0;i<modelArrayList.size();i++)
        {
            if(modelArrayList.get(i).getUserId().equalsIgnoreCase(userid))
            {
                modelArrayList.get(i).setFollwerstatus(followstatus);
                modelArrayList.get(i).setFollowersCount(followcount);
            }
        }
    }

    public void updateShares(SimplePlayerViewHolder holder,int pos)
    {
        int sharescount;
        if(modelArrayList.get(pos).getShareCount()==null || modelArrayList.get(pos).getShareCount().equalsIgnoreCase("null"))
            sharescount=0;
        else
            sharescount=Integer.valueOf(modelArrayList.get(pos).getShareCount());

        Log.e("shares count","setting "+(sharescount+1));
        holder.tv_sharescount.setText(""+(sharescount+1));
        modelArrayList.get(pos).setShareCount(""+(sharescount+1));
    }

    public void updateCommentsCount(SimplePlayerViewHolder holder,int pos,String commentscount)
    {
        holder.tv_commentscount.setText(commentscount);
        modelArrayList.get(pos).setCommentCount(commentscount);
    }

    @Override
    public void onHashtagclicked(String hashtag) {
       if(videoActionsListener!=null)
           videoActionsListener.onHashtagclicked(hashtag);
    }

    @Override
    public void onDoubletap(SimplePlayerViewHolder holder, int position) {
        /*if(videoActionsListener!=null && modelArrayList.get(position).getLikestatus()!=null)
        {
            holder.iv_likeanim.setVisibility(View.VISIBLE);
            moveUp(holder.iv_likeanim);
            videoActionsListener.onLikeClicked(holder,modelArrayList.get(position).getId(),position);
        }*/
    }

    @Override
    public void onLongClick(SimplePlayerViewHolder holder, int position) {
        displayLongclickDialog(holder,position);
    }

    @Override
    public void onClick(SimplePlayerViewHolder holder, int position) {

    }

    public interface VideoActionsListener
    {
        void onLikeClicked(SimplePlayerViewHolder holder,String videoid,int pos);
        void onFavouriteClicked(SimplePlayerViewHolder holder,String videoid,int pos);
        void onFollowClicked(SimplePlayerViewHolder holder,String videoid,int pos);
        void onProfileClicked(VideoModel model);
        void onCommentsClicked(SimplePlayerViewHolder holder,int pos,String videoid,String commentscount);
        void onShareClicked(String videourl,SimplePlayerViewHolder holder,String videoid,int pos);
        void onAbuseClicked(VideoModel model);
        void onHashtagclicked(String hashtag);
        void onDubsmashClicked(VideoModel model);
    }

    private ArrayList<String> getHashtagslist(String tagsdata)
    {
        ArrayList<String> tagslist=new ArrayList<>();
        if(tagsdata!=null && !tagsdata.equalsIgnoreCase("null"))
        {
            StringTokenizer stringTokenizer=new StringTokenizer(tagsdata,",# ");
            while (stringTokenizer.hasMoreTokens())
                tagslist.add("#"+stringTokenizer.nextToken());
        }

        return tagslist;
    }

    private void displayLongclickDialog(SimplePlayerViewHolder holder,int position)
    {
        final Dialog dialog=new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.transparenthomescreendialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RelativeLayout layout_save=(RelativeLayout)dialog.findViewById(R.id.layout_save);
        RelativeLayout layout_addtofavourites=(RelativeLayout)dialog.findViewById(R.id.layout_addtofavourites);
        RelativeLayout layout_notinterested=(RelativeLayout)dialog.findViewById(R.id.layout_notinterested);

        layout_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null) {
                    dialog.dismiss();
                    videoActionsListener.onShareClicked(VIDEOSHAREBASEPATH + modelArrayList.get(position).getVideo(), holder, modelArrayList.get(position).getId(), position);
                }
            }
        });

        layout_addtofavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                {
                    dialog.dismiss();
                    videoActionsListener.onFavouriteClicked(holder,modelArrayList.get(position).getId(),position);
                }
            }
        });

        layout_notinterested.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null) {
                    dialog.dismiss();
                    videoActionsListener.onAbuseClicked(modelArrayList.get(position));
                }
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    private void fadeOutAndHideImage(final ImageView img)
    {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(1000);

        fadeOut.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation animation)
            {
                img.setVisibility(View.GONE);
            }
            public void onAnimationRepeat(Animation animation) {}
            public void onAnimationStart(Animation animation) {}
        });

        img.startAnimation(fadeOut);
    }

    private String getThumbnailpath(String videopath)
    {
        String jpeg=null;
        if(videopath!=null && videopath.trim().length()>4)
        {
            jpeg=videopath.substring(0,videopath.length()-4);
            jpeg=jpeg+"-image.jpeg";
        }
        return jpeg;
    }

    public void moveUp(final ImageView img){
        Animation animation1 =
                AnimationUtils.loadAnimation(context, R.anim.move_top);
        img.startAnimation(animation1);
        animation1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                img.clearAnimation();
                fadeOutAndHideImage(img);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}