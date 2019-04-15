package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hulchul.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 4/20/2017.
 */

public class UsersSearchAdapter extends RecyclerView.Adapter<UsersSearchAdapter.MyViewHolder>
{
    private Context context;

    public UsersSearchAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public UsersSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usersearch_model, parent, false);
        return new UsersSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final UsersSearchAdapter.MyViewHolder holder, final int position)
    {
        final UsersSearchAdapter.MyViewHolder myViewHolder=holder;
    }

    @Override
    public int getItemCount()
    {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;
        @BindView(R.id.tv_username)
        TextView tv_username;
        @BindView(R.id.tv_usertag)
        TextView tv_usertag;
        @BindView(R.id.tv_biodata)
        TextView tv_biodata;
        @BindView(R.id.tv_following)
        TextView tv_following;
        @BindView(R.id.tv_follow)
        TextView tv_follow;
        @BindView(R.id.tv_friend)
        TextView tv_friend;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
