package com.app.zippnews.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.zippnews.R;
import com.app.zippnews.model.CommentslistModel;
import com.app.zippnews.utils.ApiUrls;
import com.app.zippnews.utils.SessionManagement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 4/20/2017.
 */
public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>
{
    private ArrayList<CommentslistModel> commentslistModelArrayList;
    private Context context;
    private OnCommentsActionsListener onCommentsActionsListener;
    private SessionManagement sessionManagement;
    private String userid;
    public CommentsAdapter(Context context,ArrayList<CommentslistModel> commentslistModelArrayList)
    {
        this.context=context;
        this.commentslistModelArrayList=commentslistModelArrayList;
        sessionManagement=new SessionManagement(context);
        userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
    }

    public void setOnCommentsActionsListener(OnCommentsActionsListener onCommentsActionsListener)
    {
        this.onCommentsActionsListener=onCommentsActionsListener;
    }
    @Override
    public CommentsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_model, parent, false);

        return new CommentsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CommentsAdapter.MyViewHolder holder, final int position)
    {
        final CommentsAdapter.MyViewHolder myViewHolder=holder;
        CommentslistModel model=commentslistModelArrayList.get(position);
        if(model.getParentId()!=null && !model.getParentId().equalsIgnoreCase("0") && model.getParentId().trim().length()!=0) {
            holder.layout_repliedcommentlayout.setVisibility(View.VISIBLE);
            holder.tv_replycomment.setText(model.getParentcoment());
            String parentuserid,parentusername;
            parentuserid=model.getParentuserId();
            parentusername=model.getParentfullName();
            if(parentusername!=null && !parentusername.equalsIgnoreCase("null") && parentusername.trim().length()!=0)
                holder.tv_parentusername.setText("@"+parentusername);
            else
                holder.tv_parentusername.setText("@User"+parentuserid);
        }
        else
           holder.layout_repliedcommentlayout.setVisibility(View.GONE);

        holder.tv_likescount.setText(""+model.getLikeCount());
        String id=model.getUserId();
        String fullname=model.getFullName();
        if(userid!=null)
        {
            if(userid.equalsIgnoreCase(id))
                holder.iv_reply.setVisibility(View.GONE);
            else
                holder.iv_reply.setVisibility(View.VISIBLE);
        }

        if(fullname!=null && !fullname.equalsIgnoreCase("null") && fullname.trim().length()!=0)
            holder.tv_username.setText("@"+fullname);
        else
          holder.tv_username.setText("@User"+id);

        holder.tv_usercomment.setText(model.getComment());
        holder.tv_postedtime.setVisibility(View.GONE);

        holder.iv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(onCommentsActionsListener!=null)
                    onCommentsActionsListener.onReplyClicked(commentslistModelArrayList.get(position));
            }
        });

        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCommentsActionsListener!=null)
                    onCommentsActionsListener.onCommentLikeClicked(commentslistModelArrayList.get(position));
            }
        });

        Picasso.with(context).load(ApiUrls.PROFILEBASEPATH+model.getPhoto()).placeholder(R.mipmap.profile)
                .error(R.mipmap.profile)
                .into(holder.profile_image);
    }

    @Override
    public int getItemCount()
    {
        return commentslistModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.iv_reply)
        ImageView iv_reply;
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.tv_usercomment)
        TextView tv_usercomment;
        @BindView(R.id.tv_replycomment)
        TextView tv_replycomment;
        @BindView(R.id.iv_like)
        ImageView iv_like;
        @BindView(R.id.tv_likescounts)
        TextView tv_likescount;
        @BindView(R.id.tv_postedtime)
        TextView tv_postedtime;
        @BindView(R.id.tv_parentusername)
        TextView tv_parentusername;
        @BindView(R.id.layout_repliedcommentlayout)
        RelativeLayout layout_repliedcommentlayout;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public interface OnCommentsActionsListener
    {
        void onReplyClicked(CommentslistModel model);
        void onCommentLikeClicked(CommentslistModel model);
    }
}
