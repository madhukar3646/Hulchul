package com.app.hulchul.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.app.hulchul.R;
import com.app.hulchul.activities.HomeScreenHashtagsAdapter;
import com.app.hulchul.model.HomescreenCommentModel;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.utils.ApiUrls;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import java.util.ArrayList;
import java.util.StringTokenizer;

import static com.app.hulchul.utils.ApiUrls.VIDEOSHAREBASEPATH;

public class SimpleAdapter extends RecyclerView.Adapter<SimplePlayerViewHolder> implements HomeScreenHashtagsAdapter.OnHashtagClickListener {

    private ArrayList<VideoModel> modelArrayList;
    private Context context;
    private SessionManagement sessionManagement;
    private VideoActionsListener videoActionsListener;
    private String videobasepath;
    private String audiobasepath;

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
         holder.bind(Uri.parse(videobasepath+modelArrayList.get(position).getVideo()) /* FIXME use real data */);
        if(modelArrayList.get(position).getSongfile()==null || modelArrayList.get(position).getSongfile().equalsIgnoreCase("null"))
            holder.bindMusic(null);
        else
            holder.bindMusic(audiobasepath+ modelArrayList.get(position).getSongfile());

        ArrayList<String> tagslist=getHashtagslist(modelArrayList.get(position).getHashTag());
        HomeScreenHashtagsAdapter hashtagsAdapter=new HomeScreenHashtagsAdapter(context,tagslist);
        hashtagsAdapter.setOnHashtagClickListener(this);
        holder.rv_hashtagslist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rv_hashtagslist.setAdapter(hashtagsAdapter);

        holder.tv_profilename.setText("@User"+modelArrayList.get(position).getUserId().substring(modelArrayList.get(position).getUserId().length()-4));

        if(modelArrayList.get(position).getComments()!=null && modelArrayList.get(position).getComments().size()>0)
        {
            HomescreenCommentModel model=modelArrayList.get(position).getComments().get(0);
            String com_userid=model.getUserId();
            holder.latest1_commentfrom.setText("@user"+com_userid.substring(com_userid.length()-4));
            holder.latest1_comment.setText(model.getComment());
        }
        else {
            holder.latest1_commentfrom.setVisibility(View.GONE);
            holder.latest1_comment.setVisibility(View.GONE);
        }
        if(modelArrayList.get(position).getComments()!=null && modelArrayList.get(position).getComments().size()>1)
        {
            HomescreenCommentModel model=modelArrayList.get(position).getComments().get(1);
            String com_userid=model.getUserId();
            holder.latest2_commentfrom.setText("@user"+com_userid.substring(com_userid.length()-4));
            holder.latest2_comment.setText(model.getComment());
        }
        else {
            holder.latest2_commentfrom.setVisibility(View.GONE);
            holder.latest2_comment.setVisibility(View.GONE);
        }

        holder.tv_likescount.setText(modelArrayList.get(position).getLikes());
        if(modelArrayList.get(position).getLikestatus()!=null)
        {
            if (modelArrayList.get(position).getLikestatus().equalsIgnoreCase("0"))
                holder.iv_like.setImageResource(R.mipmap.heart_white);
            else
                holder.iv_like.setImageResource(R.mipmap.heart);
        }

        if((modelArrayList.get(position).getFollowersCount()==null || modelArrayList.get(position).getFollowersCount().equalsIgnoreCase("null")))
          holder.tv_profilelikescount.setText("0");
        else
          holder.tv_profilelikescount.setText(modelArrayList.get(position).getFollowersCount());

        if(modelArrayList.get(position).getFollwerstatus()!=null)
        {
            if (modelArrayList.get(position).getFollwerstatus().equalsIgnoreCase("0")) {
                holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
                holder.iv_heart.setImageResource(R.mipmap.heart_white);
            } else {
                holder.iv_addfriend.setImageResource(R.mipmap.follow_check);
                holder.iv_heart.setImageResource(R.mipmap.heart);
            }
        }

        if(modelArrayList.get(position).getShareCount()==null || modelArrayList.get(position).getShareCount().equalsIgnoreCase("null"))
            holder.tv_sharescount.setText("0");
        else
            holder.tv_sharescount.setText(modelArrayList.get(position).getShareCount());

        if(modelArrayList.get(position).getCommentCount()==null || modelArrayList.get(position).getCommentCount().equalsIgnoreCase("null"))
            holder.tv_commentscount.setText("0");
        else
            holder.tv_commentscount.setText(modelArrayList.get(position).getCommentCount());

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                    videoActionsListener.onProfileClicked(modelArrayList.get(position));
            }
        });
        holder.iv_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null && modelArrayList.get(position).getFollwerstatus()!=null)
                    videoActionsListener.onFollowClicked(holder,modelArrayList.get(position).getUserId(),position);
            }
        });
        holder.layout_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(videoActionsListener!=null)
                   videoActionsListener.onCommentsClicked(holder,position,modelArrayList.get(position).getId(),modelArrayList.get(position).getCommentCount());
            }
        });

        holder.layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null && modelArrayList.get(position).getLikestatus()!=null)
                {
                  videoActionsListener.onLikeClicked(holder,modelArrayList.get(position).getId(),position);
                }
            }
        });

        holder.layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*"http://testingmadesimple.org/training_app/uploads/userVideos/"*/
                if(videoActionsListener!=null)
                    videoActionsListener.onShareClicked(VIDEOSHAREBASEPATH + modelArrayList.get(position).getVideo(), holder, modelArrayList.get(position).getId(), position);
            }
        });

        holder.layout_sendcomment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.callToast(context,"send ");
            }
        });
        holder.layout_abuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(videoActionsListener!=null)
                   videoActionsListener.onAbuseClicked();
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
        holder.tv_likescount.setText(""+likescount);
        if(modelArrayList.get(pos).getLikestatus().equalsIgnoreCase("0")) {
            modelArrayList.get(pos).setLikes(""+(likescount+1));
            modelArrayList.get(pos).setLikestatus("1");
            holder.iv_like.setImageResource(R.mipmap.heart);
            holder.tv_likescount.setText(""+(likescount+1));
        }
        else {
            modelArrayList.get(pos).setLikes(""+(likescount-1));
            modelArrayList.get(pos).setLikestatus("0");
            holder.iv_like.setImageResource(R.mipmap.heart_white);
            holder.tv_likescount.setText(""+(likescount-1));
        }
    }

    public void updateFollows(SimplePlayerViewHolder holder,int pos)
    {
        int followscount;
        if(modelArrayList.get(pos).getFollowersCount()==null || modelArrayList.get(pos).getFollowersCount().equalsIgnoreCase("null"))
            followscount=0;
        else
           followscount=Integer.valueOf(modelArrayList.get(pos).getFollowersCount());

        holder.tv_profilelikescount.setText(""+followscount);
        if(modelArrayList.get(pos).getFollwerstatus()==null || modelArrayList.get(pos).getFollwerstatus().equalsIgnoreCase("null"))
            modelArrayList.get(pos).setFollwerstatus("0");

        if(modelArrayList.get(pos).getFollwerstatus().equalsIgnoreCase("0")) {
            modelArrayList.get(pos).setFollowersCount(""+(followscount+1));
            modelArrayList.get(pos).setFollwerstatus("1");
            holder.iv_addfriend.setImageResource(R.mipmap.follow_check);
            holder.iv_heart.setImageResource(R.mipmap.heart);
            holder.tv_profilelikescount.setText(""+(followscount+1));
            setFollowCountForAllVideostoThisUser(modelArrayList.get(pos).getUserId(),"1",""+(followscount+1));
        }
        else {
            modelArrayList.get(pos).setFollowersCount(""+(followscount-1));
            modelArrayList.get(pos).setFollwerstatus("0");
            holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
            holder.iv_heart.setImageResource(R.mipmap.heart_white);
            holder.tv_profilelikescount.setText(""+(followscount-1));
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

    public interface VideoActionsListener
    {
        void onLikeClicked(SimplePlayerViewHolder holder,String videoid,int pos);
        void onFollowClicked(SimplePlayerViewHolder holder,String videoid,int pos);
        void onProfileClicked(VideoModel model);
        void onCommentsClicked(SimplePlayerViewHolder holder,int pos,String videoid,String commentscount);
        void onShareClicked(String videourl,SimplePlayerViewHolder holder,String videoid,int pos);
        void onAbuseClicked();
        void onHashtagclicked(String hashtag);
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
}