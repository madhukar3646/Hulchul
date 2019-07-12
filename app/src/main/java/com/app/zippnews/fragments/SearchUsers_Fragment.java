package com.app.zippnews.fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.zippnews.R;
import com.app.zippnews.activities.LoginLandingActivity;
import com.app.zippnews.activities.SearchActivity;
import com.app.zippnews.activities.UserProfileActivity;
import com.app.zippnews.adapters.UsersSearchAdapter;
import com.app.zippnews.model.SearchUserResponse;
import com.app.zippnews.model.SignupResponse;
import com.app.zippnews.model.UserSearchdata;
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


public class SearchUsers_Fragment extends Fragment implements SearchActivity.OnSearchFragmentSelected,UsersSearchAdapter.OnUserSelectionListener{

    @BindView(R.id.rv_users)
    RecyclerView rv_users;
    @BindView(R.id.tv_nodata)
    TextView tv_nodata;
    private UsersSearchAdapter usersSearchAdapter;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private ArrayList<UserSearchdata> userSearchdataArrayList=new ArrayList<>();
    private String search_key="";

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
        connectionDetector=new ConnectionDetector(getActivity());
        sessionManagement=new SessionManagement(getActivity());
        usersSearchAdapter=new UsersSearchAdapter(getActivity(),userSearchdataArrayList);
        usersSearchAdapter.setOnUserSelectionListener(this);
        rv_users.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_users.setAdapter(usersSearchAdapter);
    }

    @Override
    public void onSearchFragmentSelected(String searchkey) {
        if(searchkey!=null && searchkey.trim().length()!=0)
        {
            if(!this.search_key.equalsIgnoreCase(searchkey))
            {
                if(connectionDetector.isConnectingToInternet())
                    onPerformSearch(searchkey);
                else
                    Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
            }
            else {
                if(userSearchdataArrayList.size()>0)
                    tv_nodata.setVisibility(View.GONE);
                else
                    tv_nodata.setVisibility(View.VISIBLE);
            }
        }
        Log.e("Users fragment","Users fragment");
    }

    @Override
    public void onPerformSearch(String key) {
        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN)) {
            this.search_key = key;
            userSearchdataArrayList.clear();
            Utils.showDialog(getActivity());
            Call<SearchUserResponse> call = RetrofitApis.Factory.createTemp(getActivity()).searchUser(key,sessionManagement.getValueFromPreference(SessionManagement.USERID));
            call.enqueue(new Callback<SearchUserResponse>() {
                @Override
                public void onResponse(Call<SearchUserResponse> call, Response<SearchUserResponse> response) {
                    Utils.dismissDialog();
                    SearchUserResponse body = response.body();
                    if (body != null) {
                        if (body.getStatus() == 0) {
                            if (body.getData() != null)
                                userSearchdataArrayList.addAll(body.getData());
                        } else {
                            //Utils.callToast(getActivity(), body.getMessage());
                            tv_nodata.setText(body.getMessage());
                        }
                    } else {
                        Utils.callToast(getActivity(), "Null data came");
                    }
                    if (userSearchdataArrayList.size() > 0)
                        tv_nodata.setVisibility(View.GONE);
                    else
                        tv_nodata.setVisibility(View.VISIBLE);
                    usersSearchAdapter.notifyDataSetChanged();
                }

                @Override
                public void onFailure(Call<SearchUserResponse> call, Throwable t) {
                    Utils.dismissDialog();
                    Log.e("searchuser onFailure", "" + t.getMessage());
                }
            });
        }
        else
            startActivity(new Intent(getActivity(),LoginLandingActivity.class));
    }

    @Override
    public void onUserSelected(UserSearchdata userSearchdata) {

        if(sessionManagement.getBooleanValueFromPreference(SessionManagement.ISLOGIN))
        {
            Log.e("others userid",""+userSearchdata.getUserId());
            Log.e("my userid",""+sessionManagement.getValueFromPreference(SessionManagement.USERID));
            Intent intent = new Intent(getActivity(), UserProfileActivity.class);
            intent.putExtra("othersuserid", userSearchdata.getUserId());
            startActivity(intent);
        }
        else {
            startActivity(new Intent(getActivity(), LoginLandingActivity.class));
        }
    }

    @Override
    public void onFollowClicked(UserSearchdata userSearchdata, int position) {
      if(connectionDetector.isConnectingToInternet())
      {
          String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
          if(!userid.equalsIgnoreCase(userSearchdata.getUserId()))
             setFollowUnFollow(userid,userSearchdata.getUserId(),position,true);
          else
              Utils.callToast(getActivity(),"This is your profile only");
      }
      else
          Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
    }

    @Override
    public void onFollowingClicked(UserSearchdata userSearchdata, int position) {
        if(connectionDetector.isConnectingToInternet())
        {
            String userid=sessionManagement.getValueFromPreference(SessionManagement.USERID);
            if(!userid.equalsIgnoreCase(userSearchdata.getUserId()))
                setFollowUnFollow(userid,userSearchdata.getUserId(),position,false);
            else
                Utils.callToast(getActivity(),"This is your profile only");
        }
        else
            Utils.callToast(getActivity(), getResources().getString(R.string.internet_toast));
    }

    private void setFollowUnFollow(String userid, String fromid,int position,boolean isFollow){
        Utils.showDialog(getActivity());
        Call<SignupResponse> call= RetrofitApis.Factory.createTemp(getActivity()).followUnfollowUserService(userid,fromid);
        call.enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(Call<SignupResponse> call, Response<SignupResponse> response) {
                Utils.dismissDialog();
                SignupResponse body=response.body();
                if(body.getStatus()==1){
                    if(isFollow)
                      userSearchdataArrayList.get(position).setFollwerstatus("1");
                    else
                        userSearchdataArrayList.get(position).setFollwerstatus("0");
                    usersSearchAdapter.notifyDataSetChanged();
                }
                else {
                    //Utils.callToast(getActivity(),body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<SignupResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("follow onFailure",""+t.getMessage());
            }
        });
    }
}
