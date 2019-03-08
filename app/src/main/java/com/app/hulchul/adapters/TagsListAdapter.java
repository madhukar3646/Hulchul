package com.app.hulchul.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.Tagmodel;

import java.util.ArrayList;

/**
 * Created by admin on 4/20/2017.
 */

public class TagsListAdapter extends RecyclerView.Adapter<TagsListAdapter.MyViewHolder>
{
    private Activity context;
    private ArrayList<Tagmodel> tagslist;
    private ArrayList<String> selected_tags=new ArrayList<>();
    private OnTagclicklistener onTagclicklistener;

    public TagsListAdapter(Activity context, ArrayList<Tagmodel> tagslist)
    {
        this.context=context;
        this.tagslist=tagslist;
    }

    public void setOnTagclicklistener(OnTagclicklistener onTagclicklistener)
    {
        this.onTagclicklistener=onTagclicklistener;
    }

    @Override
    public TagsListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tag_item, parent, false);

        return new TagsListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final TagsListAdapter.MyViewHolder holder, final int position)
    {
        final TagsListAdapter.MyViewHolder myViewHolder=holder;
        Tagmodel model=tagslist.get(position);
        holder.tv_tagname.setText(model.getTagname());
        if(tagslist.get(position).isSelected())
            holder.layout_tag.setBackgroundResource(R.drawable.rounded_whiteborderwithredbg);
        else
            holder.layout_tag.setBackgroundResource(R.drawable.rounded_whiteborderwith_transparentbg);

        holder.layout_tag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              if(tagslist.get(position).isSelected())
              {
                  tagslist.get(position).setSelected(false);
                  holder.layout_tag.setBackgroundResource(R.drawable.rounded_whiteborderwith_transparentbg);
                  if(selected_tags.contains(tagslist.get(position).getTagname()))
                      selected_tags.remove(tagslist.get(position).getTagname());
              }
              else {
                  tagslist.get(position).setSelected(true);
                  holder.layout_tag.setBackgroundResource(R.drawable.rounded_whiteborderwithredbg);
                  selected_tags.add(tagslist.get(position).getTagname());
              }

              if(onTagclicklistener!=null)
                  onTagclicklistener.onTagsSelected(selected_tags);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return tagslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_tagname;
        public RelativeLayout layout_tag;
        public MyViewHolder(View itemView)
        {
            super(itemView);
            tv_tagname=(TextView) itemView.findViewById(R.id.tv_tagname);
            layout_tag=(RelativeLayout)itemView.findViewById(R.id.layout_tag);
        }
    }

    public interface OnTagclicklistener
    {
       void onTagsSelected(ArrayList<String> selectedTags);
    }
}
