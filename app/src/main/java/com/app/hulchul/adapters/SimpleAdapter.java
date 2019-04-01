package com.app.hulchul.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.activities.LoginLandingActivity;
import com.app.hulchul.model.VideoModel;
import com.app.hulchul.utils.SessionManagement;
import com.app.hulchul.utils.Utils;
import java.util.ArrayList;

public class SimpleAdapter extends RecyclerView.Adapter<SimplePlayerViewHolder>{

    private ArrayList<VideoModel> modelArrayList;
    private Context context;
    private SessionManagement sessionManagement;
    private VideoActionsListener videoActionsListener;

    public SimpleAdapter(Context context, ArrayList<VideoModel> modelArrayList)
    {
        this.modelArrayList=modelArrayList;
        this.context=context;
        sessionManagement=new SessionManagement(context);
    }

    @Override public SimplePlayerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_holder_exoplayer_basic, parent, false);
        return new SimplePlayerViewHolder(view);
    }

    @Override public void onBindViewHolder(final SimplePlayerViewHolder holder, final int position) {
        holder.bind(Uri.parse("http://testingmadesimple.org/training_app/uploads/userVideos/"+modelArrayList.get(position).getVideo()) /* FIXME use real data */);
        if(modelArrayList.get(position).getSongfile()==null || modelArrayList.get(position).getSongfile().equalsIgnoreCase("null"))
            holder.bindMusic(null);
        else
          holder.bindMusic("http://testingmadesimple.org/training_app/uploads/songs/"+modelArrayList.get(position).getSongfile());
        holder.latest1_commentfrom.setText("@Satya");
        holder.latest2_commentfrom.setText("@Krishna");

        holder.tv_likescount.setText(modelArrayList.get(position).getLikes());
        if(modelArrayList.get(position).getLikestatus().equalsIgnoreCase("0"))
            holder.iv_like.setImageResource(R.mipmap.heart_white);
        else
            holder.iv_like.setImageResource(R.mipmap.heart);

        updateFollows(holder,position);

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                    videoActionsListener.onFollowClicked(holder,position);
                //followActions(holder,position);
            }
        });
        holder.iv_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                    videoActionsListener.onFollowClicked(holder,position);
                //followActions(holder,position);
            }
        });
        holder.layout_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(videoActionsListener!=null)
                   videoActionsListener.onCommentsClicked(holder,position);
            }
        });

        holder.layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                {
                  videoActionsListener.onLikeClicked(holder,modelArrayList.get(position).getId(),position);
                }
            }
        });

        holder.layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoActionsListener!=null)
                    videoActionsListener.onShareClicked();
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
        int followscount=sessionManagement.getIntegerValueFromPreference("fc"+pos);
        holder.tv_profilelikescount.setText(""+followscount);
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
            boolean isfollow=sessionManagement.getBooleanValueFromPreference(userid+"f"+pos);
            if(isfollow)
                holder.iv_addfriend.setImageResource(R.mipmap.follow_check);
            else
                holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
        }
        else {
            holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
        }
    }

    public void followActions(SimplePlayerViewHolder holder,int pos)
    {
        String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        int followcount=sessionManagement.getIntegerValueFromPreference("fc"+pos);
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            boolean isfollow=sessionManagement.getBooleanValueFromPreference(userid+"f"+pos);
            if(isfollow)
            {
                sessionManagement.setBooleanValuetoPreference(userid+"f"+pos,false);
                sessionManagement.setIntegerValuetoPreference("fc"+pos,followcount-1);
                holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
            }
            else {
                sessionManagement.setBooleanValuetoPreference(userid+"f"+pos,true);
                sessionManagement.setIntegerValuetoPreference("fc"+pos,followcount+1);
                holder.iv_addfriend.setImageResource(R.mipmap.follow_check);
            }
            updateFollows(holder,pos);
        }
        else {
            context.startActivity(new Intent(context, LoginLandingActivity.class));
        }
    }

    public interface VideoActionsListener
    {
        void onLikeClicked(SimplePlayerViewHolder holder,String videoid,int pos);
        void onFollowClicked(SimplePlayerViewHolder holder,int pos);
        void onCommentsClicked(SimplePlayerViewHolder holder,int pos);
        void onShareClicked();
        void onAbuseClicked();
    }
}