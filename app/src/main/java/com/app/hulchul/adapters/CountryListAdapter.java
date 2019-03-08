package com.app.hulchul.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hulchul.R;
import com.app.hulchul.model.Country_model;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


/**
 * Created by admin on 4/20/2017.
 */

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.MyViewHolder>
{
    private Activity context;
    private ArrayList<Country_model> channelslist;
    private OnCountryselectedListener onCountryselectedListener;

    public CountryListAdapter(Activity context, ArrayList<Country_model> channelslist)
    {
        this.context=context;
        this.channelslist=channelslist;
    }

    public void setClicklistener(OnCountryselectedListener onCountryselectedListener)
    {
        this.onCountryselectedListener=onCountryselectedListener;
    }
    @Override
    public CountryListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.countryitem, parent, false);

        return new CountryListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CountryListAdapter.MyViewHolder holder, final int position)
    {
        final CountryListAdapter.MyViewHolder myViewHolder=holder;
        Country_model model=channelslist.get(position);
        holder.tv_countrycode.setText(model.getCountry_code());
        holder.tv_countryname.setText(model.getCountry_name());

        Picasso.with(context)
                .load(model.getFlagpath())
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(holder.iv_flag);


        holder.itemgrid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(onCountryselectedListener!=null)
                   onCountryselectedListener.onCountrySelected(channelslist.get(position));
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return channelslist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView tv_countryname,tv_countrycode;
        public ImageView iv_flag;
        public RelativeLayout itemgrid;

        public MyViewHolder(View itemView)
        {
            super(itemView);

            tv_countryname=(TextView) itemView.findViewById(R.id.tv_countryname);
            tv_countrycode=(TextView) itemView.findViewById(R.id.tv_countrycode);
            iv_flag=(ImageView)itemView.findViewById(R.id.iv_flag);
            itemgrid=(RelativeLayout)itemView.findViewById(R.id.itemgrid);
        }
    }

    public void filterList(ArrayList<Country_model> filterdNames) {
        this.channelslist = filterdNames;
        notifyDataSetChanged();
    }

    public interface OnCountryselectedListener
    {
        void onCountrySelected(Country_model model);
    }
}
