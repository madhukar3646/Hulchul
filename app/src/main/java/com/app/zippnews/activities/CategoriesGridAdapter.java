package com.app.zippnews.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.model.CategoryModel;
import com.app.zippnews.utils.ApiUrls;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesGridAdapter extends RecyclerView.Adapter<CategoriesGridAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoriesModelArrayList;
    private OnCategorySelectionListener listener;
    private int screenWidth;
    public void setListener(OnCategorySelectionListener listener) {
        this.listener = listener;
    }

    public CategoriesGridAdapter(Context context, List<CategoryModel> categoriesModelArrayList) {
        this.context=context;
        this.categoriesModelArrayList = categoriesModelArrayList;
        this.screenWidth=context.getResources().getDisplayMetrics().widthPixels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_griditem, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {
        final CategoryModel model=categoriesModelArrayList.get(pos);
        viewHolder.tv_categoryname.setText(model.getName());
        viewHolder.tv_categoryname.setSelected(true);
        if(model.isLocation())
            viewHolder.iv_categoryicon.setImageResource(R.mipmap.location_category);
        else if(model.isHashtag())
            viewHolder.iv_categoryicon.setImageResource(R.mipmap.addhashcategory);
        else
          Picasso.with(context).load(ApiUrls.CATEGORYIMAGESBASEPATH+model.getImage()).placeholder(R.mipmap.placeholder).into(viewHolder.iv_categoryicon);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onCategorySelected(model);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(listener!=null){
                      listener.onDeleteCategory(model);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.tv_categoryname)
        TextView tv_categoryname;
        @BindView(R.id.layout_container)
        LinearLayout layout_container;
        @BindView(R.id.iv_categoryicon)
        ImageView iv_categoryicon;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);

            layout_container.getLayoutParams().width=screenWidth/3;
            layout_container.getLayoutParams().height=screenWidth/3;
            iv_categoryicon.getLayoutParams().height=screenWidth/6;
            iv_categoryicon.getLayoutParams().height=screenWidth/6;
        }
    }
   public interface OnCategorySelectionListener{
        void onCategorySelected(CategoryModel category);
        void onDeleteCategory(CategoryModel category);
   }
}
