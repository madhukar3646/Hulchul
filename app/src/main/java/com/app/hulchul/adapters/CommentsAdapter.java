package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.app.hulchul.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 4/20/2017.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MyViewHolder>
{
    private Context context;
    private OnCommentsActionsListener onCommentsActionsListener;
    public CommentsAdapter(Context context)
    {
        this.context=context;
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
        if(position%10==0)
            holder.layout_repliedcommentlayout.setVisibility(View.VISIBLE);
        else
           holder.layout_repliedcommentlayout.setVisibility(View.GONE);

        holder.iv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onCommentsActionsListener!=null)
                    onCommentsActionsListener.onReplyClicked("reply to user"+position);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return 30;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.iv_reply)
        ImageView iv_reply;
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.tv_replycomment)
        TextView tv_replycomment;
        @BindView(R.id.iv_like)
        ImageView iv_like;
        @BindView(R.id.tv_likescount)
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
        void onReplyClicked(String username);
    }
}
