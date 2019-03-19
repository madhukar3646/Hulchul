package com.app.hulchul.adapters;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by admin on 4/20/2017.
 */
public class HashtagsCategoriesAdapter extends RecyclerView.Adapter<HashtagsCategoriesAdapter.MyViewHolder>
{
    private Context context;
    private int width,height;
    private HashtagsthumnailsAdapter hashtagsthumnailsAdapter;

    public HashtagsCategoriesAdapter(Context context)
    {
        this.context=context;
        DisplayMetrics metrics=context.getResources().getDisplayMetrics();
        width=metrics.widthPixels;
        height=metrics.heightPixels;
    }

    @Override
    public HashtagsCategoriesAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hashtags_containermodel, parent, false);

        return new HashtagsCategoriesAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final HashtagsCategoriesAdapter.MyViewHolder holder, final int position)
    {
        final HashtagsCategoriesAdapter.MyViewHolder myViewHolder=holder;

        holder.rv_hashtagscontainer.setLayoutManager(new LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false));
        hashtagsthumnailsAdapter=new HashtagsthumnailsAdapter(context);
        holder.rv_hashtagscontainer.setAdapter(hashtagsthumnailsAdapter);
    }

    @Override
    public int getItemCount()
    {
        return 5;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.tv_hashtagname)
        TextView tv_hashtagname;
        @BindView(R.id.tv_hashtagsviews)
        TextView tv_hashtagsviews;
        @BindView(R.id.layout_viewall)
        RelativeLayout layout_viewall;
        @BindView(R.id.rv_hashtagscontainer)
        RecyclerView rv_hashtagscontainer;

        public MyViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
