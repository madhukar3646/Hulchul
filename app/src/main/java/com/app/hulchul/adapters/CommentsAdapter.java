package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.hulchul.R;
import com.app.hulchul.model.CommentslistModel;
import com.app.hulchul.utils.SessionManagement;

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
        if(model.getParentId()!=null) {
            holder.layout_repliedcommentlayout.setVisibility(View.VISIBLE);
            holder.tv_replycomment.setText(model.getParentId().getCommentId().getComment());
        }
        else
           holder.layout_repliedcommentlayout.setVisibility(View.GONE);

        holder.tv_likescount.setText(""+model.getLikeCount());
        String id=model.getUserId().getId();
        if(userid!=null)
        {
            if(userid.equalsIgnoreCase(id))
                holder.iv_reply.setVisibility(View.GONE);
            else
                holder.iv_reply.setVisibility(View.VISIBLE);
        }

        holder.tv_username.setText("@User"+id.substring(id.length()-4));
        holder.tv_usercomment.setText(model.getComment());
        holder.tv_postedtime.setVisibility(View.GONE);

        holder.iv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCommentsActionsListener!=null)
                    onCommentsActionsListener.onReplyClicked(commentslistModelArrayList.get(position));
            }
        });
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
    }
}
