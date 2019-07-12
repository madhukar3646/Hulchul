package com.app.zippnews.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.activities.CategoriesGridAdapter;
import com.app.zippnews.activities.HashTagsListingActivity;
import com.app.zippnews.activities.LoginLandingActivity;
import com.app.zippnews.activities.MapsActivity;
import com.app.zippnews.activities.PlayvideosCategorywise_Activity;
import com.app.zippnews.activities.SettingsActivity;
import com.app.zippnews.model.CategoriesResponse;
import com.app.zippnews.model.CategoryModel;
import com.app.zippnews.model.UserAddedCategory;
import com.app.zippnews.model.VideoModel;
import com.app.zippnews.presenter.RetrofitApis;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class Discovernews_fragment extends Fragment implements CategoriesGridAdapter.OnCategorySelectionListener{
    // TODO: Rename parameter arguments, choose names that match

    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @BindView(R.id.iv_add)
    ImageView iv_add;
    @BindView(R.id.rv_categories)
    RecyclerView rv_categories;
    CategoriesGridAdapter adapter;
    private ArrayList<CategoryModel> categories_list=new ArrayList<>();
    private final String ADDCATEGORYLOCATION="location";
    private final String ADDCATEGORYHASHTAG="hashtag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_discovernews_fragment, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());

        rv_categories.setLayoutManager(new GridLayoutManager(getActivity(),3));
        adapter=new CategoriesGridAdapter(getActivity(),categories_list);
        adapter.setListener(this);
        rv_categories.setAdapter(adapter);

        if(connectionDetector.isConnectingToInternet())
        {
            setCagegoriesData();
        }
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
                  displayAddcategorypopup();
                else{
                  Intent intent=new Intent(getActivity(), LoginLandingActivity.class);
                  startActivity(intent);
                }
            }
        });
    }

    private void setCagegoriesData(){
        Utils.showDialog(getContext());
        String userid="";
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        Call<CategoriesResponse> call= RetrofitApis.Factory.createTemp(getContext()).getCategorylist(userid);
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Utils.dismissDialog();
                CategoriesResponse body=response.body();
                if(body!=null)
                {
                    if (body.getStatus() == 1) {
                        if(body.getData()!=null && body.getData().size()>0)
                        {
                            categories_list.clear();
                            CategoryModel model=new CategoryModel();
                            model.setId("");
                            model.setName("Near Me");
                            model.setLocation(true);
                            model.setCategory(false);
                            model.setHashtag(false);
                            model.setLattitude(sessionManagement.getCurrentlattitude());
                            model.setLongnitude(sessionManagement.getCurrentlognitude());
                            categories_list.add(model);
                            categories_list.addAll(body.getData());
                        }
                        if(body.getUsercategories()!=null && body.getUsercategories().size()>0)
                        {
                            ArrayList<UserAddedCategory> list=new ArrayList<>();
                            list.addAll(body.getUsercategories());
                            for(UserAddedCategory category:list)
                            {
                                if(category.getType().equalsIgnoreCase("location"))
                                {
                                    CategoryModel model=new CategoryModel();
                                    model.setId(category.getId());
                                    model.setName(category.getPlace());
                                    model.setLocation(true);
                                    model.setCategory(false);
                                    model.setHashtag(false);
                                    model.setLattitude(category.getLatitude());
                                    model.setLongnitude(category.getLongitude());
                                    categories_list.add(model);
                                }
                                else {
                                    CategoryModel model=new CategoryModel();
                                    model.setId(category.getId());
                                    model.setName(category.getPlace());
                                    model.setLocation(false);
                                    model.setCategory(false);
                                    model.setHashtag(true);
                                    model.setLattitude(category.getLatitude());
                                    model.setLongnitude(category.getLongitude());
                                    categories_list.add(model);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Utils.callToast(getContext(), "No categories found");
                    }
                }
                else {
                    Utils.callToast(getContext(), "Null response");
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("follow onFailure",""+t.getMessage());
            }
        });
    }

    private void addCategory(String latitude,String longitude,String place,String type){
        Utils.showDialog(getContext());
        String userid="";
        if(sessionManagement.getValueFromPreference(SessionManagement.USERID)!=null)
            userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
        Call<CategoriesResponse> call= RetrofitApis.Factory.createTemp(getContext()).addUserCategory(userid,latitude,longitude,place,type);
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Utils.dismissDialog();
                CategoriesResponse body=response.body();
                if(body!=null)
                {
                    if (body.getStatus() == 0) {
                        setCagegoriesData();
                    } else {
                        Utils.callToast(getContext(), ""+body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getContext(), "Null response");
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("follow onFailure",""+t.getMessage());
            }
        });
    }

    private void deleteCategory(String id){
        Utils.showDialog(getContext());
        Call<CategoriesResponse> call= RetrofitApis.Factory.createTemp(getContext()).deleteUserCategory(id);
        call.enqueue(new Callback<CategoriesResponse>() {
            @Override
            public void onResponse(Call<CategoriesResponse> call, Response<CategoriesResponse> response) {
                Utils.dismissDialog();
                CategoriesResponse body=response.body();
                if(body!=null)
                {
                    if (body.getStatus() == 0) {
                        setCagegoriesData();
                    } else {
                        Utils.callToast(getContext(), ""+body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getContext(), "Null response");
                }
            }

            @Override
            public void onFailure(Call<CategoriesResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("follow onFailure",""+t.getMessage());
            }
        });
    }

    @Override
    public void onCategorySelected(CategoryModel category) {

        Intent intent=new Intent(getActivity(), PlayvideosCategorywise_Activity.class);
        if(category.isCategory()) {
            intent.putExtra("isFrom", "category");
            intent.putExtra("categoryid",category.getId());
        }
        else if(category.isHashtag()) {
            intent.putExtra("isFrom","hashTag");
            intent.putExtra("hashTag",category.getName());
        }
        else if(category.isLocation()) {
            intent.putExtra("isFrom", "NearLocation");
            intent.putExtra("lattitude",category.getLattitude());
            intent.putExtra("longnitude",category.getLongnitude());
        }

        intent.putParcelableArrayListExtra("videos",new ArrayList<VideoModel>());
        intent.putExtra("position",0);
        intent.putExtra("videosbasepath","");
        intent.putExtra("musicbasepath","");
        startActivity(intent);
    }

    @Override
    public void onDeleteCategory(CategoryModel category) {
         if(category.getId().trim().length()>0)
         {
             if(category.isHashtag()||category.isLocation())
             {
                 displayDeleteCategoryDialog(category);
             }
         }
    }

    private void displayDeleteCategoryDialog(CategoryModel category)
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.exitdialog);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RelativeLayout layout_yes=(RelativeLayout) dialog.findViewById(R.id.layout_yes);
        RelativeLayout layout_no=(RelativeLayout)dialog.findViewById(R.id.layout_no);
        TextView tv_title=(TextView)dialog.findViewById(R.id.tv_title);
        tv_title.setText("Are you sure you want to delete "+category.getName()+" category?");
        TextView tv_no=(TextView)dialog.findViewById(R.id.tv_no);
        tv_no.setText("No");
        TextView tv_yes=(TextView)dialog.findViewById(R.id.tv_yes);
        tv_yes.setText("Yes");

        layout_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connectionDetector.isConnectingToInternet())
                {
                    deleteCategory(category.getId());
                }
                else
                    Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
                dialog.dismiss();
            }
        });

        layout_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private void displayAddcategorypopup()
    {
        final Dialog dialog=new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.addcategory_dialogview);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RelativeLayout layout_location=(RelativeLayout) dialog.findViewById(R.id.layout_location);
        RelativeLayout layout_hashtag=(RelativeLayout)dialog.findViewById(R.id.layout_hashtag);

        layout_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(), MapsActivity.class);
                startActivityForResult(intent,125);
                dialog.dismiss();
            }
        });

        layout_hashtag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), HashTagsListingActivity.class);
                startActivityForResult(intent,100);
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent intent)
    {
        if(responseCode==RESULT_OK){
            if(requestCode==125){
             Log.e("lat",intent.getStringExtra("lattitude"));
             Log.e("logni",intent.getStringExtra("longnitude"));
             Log.e("place",intent.getStringExtra("add"));

             String lat=intent.getStringExtra("lattitude");
             String longni=intent.getStringExtra("longnitude");
             String place=intent.getStringExtra("add");
                if(connectionDetector.isConnectingToInternet())
                {
                    addCategory(lat,longni,place,ADDCATEGORYLOCATION);
                }
                else
                    Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));

            }
            else if(requestCode==100)
            {
                Log.e("hashtag",intent.getStringExtra("hashtag"));
                Log.e("hashtagid",intent.getStringExtra("hashtagid"));
                String hashtag=intent.getStringExtra("hashtag");
                if(connectionDetector.isConnectingToInternet())
                {
                    addCategory("","",hashtag,ADDCATEGORYHASHTAG);
                }
                else
                    Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
            }
        }
    }
}
