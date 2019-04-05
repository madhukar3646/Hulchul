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
import com.app.hulchul.adapters.NotificationsAdapter;
import com.app.hulchul.adapters.SimplePlayerViewHolder;
import com.app.hulchul.model.NotificationModel;
import com.app.hulchul.model.NotificationsResponse;
import com.app.hulchul.model.SignupResponse;
import com.app.hulchul.presenter.RetrofitApis;
import com.app.hulchul.utils.ConnectionDetector;
import com.app.hulchul.utils.Utils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Notification_fragment extends Fragment {


    @BindView(R.id.rv_notifications)
    RecyclerView rv_notifications;
    private NotificationsAdapter notificationsAdapter;
    private ArrayList<NotificationModel> notificationModelArrayList=new ArrayList<>();
    private ConnectionDetector connectionDetector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_notification_fragment, container, false);
        ButterKnife.bind(this,view);
        init(view);
        return view;
    }

    private void init(View view)
    {
        connectionDetector=new ConnectionDetector(getActivity());
        notificationsAdapter=new NotificationsAdapter(getActivity(),notificationModelArrayList);
        rv_notifications.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        rv_notifications.setAdapter(notificationsAdapter);

        if(connectionDetector.isConnectingToInternet())
            setNotificationsData();
        else
            Utils.callToast(getActivity(),getResources().getString(R.string.internet_toast));
    }

    private void setNotificationsData(){
        Utils.showDialog(getContext());
        Call<NotificationsResponse> call= RetrofitApis.Factory.createTemp(getContext()).notificationsService();
        call.enqueue(new Callback<NotificationsResponse>() {
            @Override
            public void onResponse(Call<NotificationsResponse> call, Response<NotificationsResponse> response) {
                Utils.dismissDialog();
                NotificationsResponse body=response.body();
                if(body.getStatus()==1){
                    if(body.getData()!=null && body.getData().size()>0)
                        notificationModelArrayList.addAll(body.getData());
                    notificationsAdapter.notifyDataSetChanged();
                }
                else {
                    Utils.callToast(getActivity(),body.getMessage());
                }
            }

            @Override
            public void onFailure(Call<NotificationsResponse> call, Throwable t) {
                Utils.dismissDialog();
                Log.e("videoslist onFailure",""+t.getMessage());
            }
        });
    }
}
