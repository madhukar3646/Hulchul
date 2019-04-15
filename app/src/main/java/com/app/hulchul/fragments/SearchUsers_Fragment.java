package com.app.hulchul.fragments;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hulchul.R;
import com.app.hulchul.activities.SearchActivity;
import com.app.hulchul.adapters.UsersSearchAdapter;
import com.app.hulchul.model.SearchUserResponse;
import com.app.hulchul.model.UserSearchdata;
import com.app.hulchul.model.ViewProfileResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchUsers_Fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected{

    @BindView(R.id.rv_users)
    RecyclerView rv_users;
    private UsersSearchAdapter usersSearchAdapter;
    private ConnectionDetector connectionDetector;
    private ArrayList<UserSearchdata> userSearchdataArrayList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_search_users_, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    public void init(View view)
    {
        usersSearchAdapter=new UsersSearchAdapter(getActivity(),userSearchdataArrayList);
        rv_users.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_users.setAdapter(usersSearchAdapter);
    }

    @Override
    public void onSearchFragmentSelected() {
        Log.e("Users fragment","Users fragment");
    }

    public void onPerformSearch(String key) {
        userSearchdataArrayList.clear();
        Utils.showDialog(getActivity());
        Call<SearchUserResponse> call= RetrofitApis.Factory.createTemp(getActivity()).searchUser(key);
        call.enqueue(new Callback<SearchUserResponse>() {
            @Override
            public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                Utils.dismissDialog();
                SearchUserResponse body=response.body();
                if(body!=null) {
                    if (body.getStatus()==0) {
                        if(body.getData()!=null)
                            userSearchdataArrayList.addAll(body.getData());
                    } else {
                        Utils.callToast(getActivity(), body.getMessage());
                    }
                }
                else {
                    Utils.callToast(getActivity(), "Null data came");
                }
                usersSearchAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("searchuser onFailure",""+t.getMessage());
            }
        });
    }
}
