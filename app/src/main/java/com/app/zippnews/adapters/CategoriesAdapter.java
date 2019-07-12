package com.app.zippnews.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.model.CategoryModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.ViewHolder> {

    Context context;
    List<CategoryModel> categoriesModelArrayList;
    private OnCategorySelectionListener listener;
    private int clickedpos=0;

    public void setListener(OnCategorySelectionListener listener) {
        this.listener = listener;
    }

    public CategoriesAdapter(Context context, List<CategoryModel> categoriesModelArrayList) {
        this.context=context;
        this.categoriesModelArrayList = categoriesModelArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int type) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.categories_model, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int pos) {
        final CategoryModel model=categoriesModelArrayList.get(pos);
        viewHolder.tv_categoryname.setText(model.getName());
        viewHolder.tv_categorynamebold.setText(model.getName());
        if(pos==clickedpos) {
            viewHolder.tv_categorynamebold.setVisibility(View.VISIBLE);
            viewHolder.tv_categoryname.setVisibility(View.GONE);
        }
        else {
            viewHolder.tv_categorynamebold.setVisibility(View.GONE);
            viewHolder.tv_categoryname.setVisibility(View.VISIBLE);
        }
        if(pos==1)
            viewHolder.viewline.setVisibility(View.GONE);
        else
            viewHolder.viewline.setVisibility(View.VISIBLE);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    listener.onCategorySelected(model);
                    clickedpos=pos;
                    notifyDataSetChanged();
                }
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
        @BindView(R.id.tv_categorynamebold)
        TextView tv_categorynamebold;
        @BindView(R.id.viewline)
        View viewline;

        public ViewHolder(View itemView)
        {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
   public interface OnCategorySelectionListener{
        void onCategorySelected(CategoryModel category);
   }
}
