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

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */

public class HashtagSearchAdapter extends RecyclerView.Adapter<HashtagSearchAdapter.MyViewHolder>
{
    private Context context;
    public HashtagSearchAdapter(Context context)
    {
        this.context=context;
    }

    @Override
    public HashtagSearchAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hashtagsearch_model, parent, false);
        return new HashtagSearchAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HashtagSearchAdapter.MyViewHolder holder, final int position)
    {
        final HashtagSearchAdapter.MyViewHolder myViewHolder=holder;

        holder.layout_viewicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    @Override
    public int getItemCount()
    {
        return 20;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_hashtagname)
        TextView tv_hashtagname;
        @BindView(R.id.tv_videoscount)
        TextView tv_videoscount;
        @BindView(R.id.layout_viewicon)
        RelativeLayout layout_viewicon;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
