package com.app.hulchul.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.CommonEmptyActivity;
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
        holder.bindMusic("http://testingmadesimple.org/training_app/uploads/songs/"+modelArrayList.get(position).getSongfile());
        holder.latest1_commentfrom.setText("@Satya");
        holder.latest2_commentfrom.setText("@Krishna");
        updateLikeAndFollows(holder,position);

        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followActions(holder,position);
            }
        });
        holder.iv_addfriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                followActions(holder,position);
            }
        });
        holder.layout_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.callToast(context,"Comments");
            }
        });
        holder.layout_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
                {
                    String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
                    int likescount=sessionManagement.getIntegerValueFromPreference("lc"+position);
                    boolean isliked=sessionManagement.getBooleanValueFromPreference(userid+"l"+position);
                    if(isliked)
                    {
                        sessionManagement.setBooleanValuetoPreference(userid+"l"+position,false);
                        sessionManagement.setIntegerValuetoPreference("lc"+position,likescount-1);
                        holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
                    }
                    else {
                        sessionManagement.setBooleanValuetoPreference(userid+"l"+position,true);
                        sessionManagement.setIntegerValuetoPreference("lc"+position,likescount+1);
                        holder.iv_addfriend.setImageResource(R.mipmap.follow_check);
                    }
                    updateLikeAndFollows(holder,position);
                }
                else {
                    context.startActivity(new Intent(context, LoginLandingActivity.class));
                }
            }
        });
        holder.layout_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.callToast(context,"share "+position);
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
                Intent abuse=new Intent(context, CommonEmptyActivity.class);
                abuse.putExtra("common","Abuse Reason selection screen");
                context.startActivity(abuse);
            }
        });
    }

    @Override public int getItemCount() {
        return modelArrayList.size();
    }

    private void updateLikeAndFollows(SimplePlayerViewHolder holder,int pos)
    {
        int likescount=sessionManagement.getIntegerValueFromPreference("lc"+pos);
        int followscount=sessionManagement.getIntegerValueFromPreference("fc"+pos);
        holder.tv_profilelikescount.setText(""+followscount);
        holder.tv_likescount.setText(""+likescount);
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
            boolean isliked=sessionManagement.getBooleanValueFromPreference(userid+"l"+pos);
            boolean isfollow=sessionManagement.getBooleanValueFromPreference(userid+"f"+pos);
            if(isfollow)
                holder.iv_addfriend.setImageResource(R.mipmap.follow_check);
            else
                holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
            if(isliked)
                holder.iv_like.setImageResource(R.mipmap.heart);
            else
                holder.iv_like.setImageResource(R.mipmap.heart_white);
        }
        else {
            holder.iv_addfriend.setImageResource(R.mipmap.add_friendicon);
            holder.iv_like.setImageResource(R.mipmap.heart_white);
        }
    }

    private void followActions(SimplePlayerViewHolder holder,int pos)
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
            updateLikeAndFollows(holder,pos);
        }
        else {
            context.startActivity(new Intent(context, LoginLandingActivity.class));
        }
    }
}