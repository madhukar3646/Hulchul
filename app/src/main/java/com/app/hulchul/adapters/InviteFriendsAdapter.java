package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.ContactsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */

public class InviteFriendsAdapter extends RecyclerView.Adapter<InviteFriendsAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<ContactsModel> contactsModelArrayList;
    public InviteFriendsAdapter(Context context,ArrayList<ContactsModel> contactsModelArrayList)
    {
        this.context=context;
        this.contactsModelArrayList=contactsModelArrayList;
    }

    @Override
    public InviteFriendsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.invite_friendsmodel, parent, false);
        return new InviteFriendsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final InviteFriendsAdapter.MyViewHolder holder, final int position)
    {
        final InviteFriendsAdapter.MyViewHolder myViewHolder=holder;
        ContactsModel model=contactsModelArrayList.get(position);
        holder.tv_personname.setText(model.getName());
        holder.tv_personnumber.setText(model.getNumber());
    }

    @Override
    public int getItemCount()
    {
        return contactsModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_personname)
        TextView tv_personname;
        @BindView(R.id.tv_personnumber)
        TextView tv_personnumber;
        @BindView(R.id.layout_invite)
        RelativeLayout layout_invite;
        @BindView(R.id.layout_invited)
        RelativeLayout layout_invited;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
