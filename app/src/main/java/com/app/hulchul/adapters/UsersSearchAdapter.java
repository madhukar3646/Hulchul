package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.app.hulchul.R;
import com.app.hulchul.model.UserSearchdata;
import com.app.hulchul.utils.ApiUrls;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by admin on 4/20/2017.
 */

public class UsersSearchAdapter extends RecyclerView.Adapter<UsersSearchAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<UserSearchdata> userSearchdataArrayList;

    public UsersSearchAdapter(Context context,ArrayList<UserSearchdata> userSearchdataArrayList)
    {
        this.context=context;
        this.userSearchdataArrayList=userSearchdataArrayList;
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
        UserSearchdata model=userSearchdataArrayList.get(position);
        Picasso.with(context).load(ApiUrls.PROFILEBASEPATH+model.getPhoto()).placeholder(R.mipmap.placeholder)
                .error(R.mipmap.placeholder)
                .into(holder.profile_image);

        String name="",followers="0";
        if(model.getFollowers()!=null && !model.getFollowers().equalsIgnoreCase("null"))
            followers=model.getFollowers();
        if(model.getFullName()!=null && !model.getFullName().equalsIgnoreCase("null"))
            name=model.getFullName();
        else
            name="@User"+model.getUserId().substring(model.getUserId().length()-4);
        holder.tv_username.setText(name+", "+followers+" Followers");
        holder.tv_usertag.setText("@"+name);
        if(model.getBioData()!=null && !model.getBioData().equalsIgnoreCase("null"))
          holder.tv_biodata.setText(model.getBioData());
        else
            holder.tv_biodata.setText("No bio data yet");

    }

    @Override
    public int getItemCount()
    {
        return userSearchdataArrayList.size();
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
