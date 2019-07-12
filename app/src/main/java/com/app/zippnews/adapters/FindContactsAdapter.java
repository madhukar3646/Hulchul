package com.app.zippnews.adapters;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.model.ContactsModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */

public class FindContactsAdapter extends RecyclerView.Adapter<FindContactsAdapter.MyViewHolder>
{
    private Context context;
    private ArrayList<ContactsModel> contactsModelArrayList;
    public FindContactsAdapter(Context context, ArrayList<ContactsModel> contactsModelArrayList)
    {
        this.context=context;
        this.contactsModelArrayList=contactsModelArrayList;
    }

    @Override
    public FindContactsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.find_contactsmodel, parent, false);
        return new FindContactsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FindContactsAdapter.MyViewHolder holder, final int position)
    {
        final FindContactsAdapter.MyViewHolder myViewHolder=holder;
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
        @BindView(R.id.layout_follow)
        RelativeLayout layout_follow;
        @BindView(R.id.layout_following)
        RelativeLayout layout_following;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
