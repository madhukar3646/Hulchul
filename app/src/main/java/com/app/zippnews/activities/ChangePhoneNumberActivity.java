package com.app.zippnews.activities;

import android.app.Dialog;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.zippnews.BuildConfig;
import com.app.zippnews.R;
import com.app.zippnews.adapters.CountryListAdapter;
import com.app.zippnews.model.Country_model;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChangePhoneNumberActivity extends AppCompatActivity implements View.OnClickListener,CountryListAdapter.OnCountryselectedListener{

    @BindView(R.id.iv_backbtn)
    ImageView iv_backbtn;
    @BindView(R.id.iv_continue)
    ImageView iv_continue;
    @BindView(R.id.et_mobile)
    EditText et_mobile;
    @BindView(R.id.layout_continue)
    RelativeLayout layout_continue;
    @BindView(R.id.layout_continuedumny)
    RelativeLayout layout_continuedumny;
    @BindView(R.id.layout_mobilenumber)
    LinearLayout layout_mobilenumber;

    @BindView(R.id.country_layout)
    RelativeLayout country_layout;
    @BindView(R.id.tv_countrydata)
    TextView tv_countrydata;

    private ArrayList<Country_model> list;
    private String country_zipcode="+91";
    private String country_namecode="IN";
    private String country_name="India";
    private String flagpath= Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/mipmap/in").toString();
    private LinearLayoutManager layoutManager;
    private CountryListAdapter adapter;
    private Dialog dialog;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(ChangePhoneNumberActivity.this);
        sessionManagement=new SessionManagement(ChangePhoneNumberActivity.this);
        iv_backbtn.setOnClickListener(this);
        layout_continue.setOnClickListener(this);
        //country_layout.setOnClickListener(this);
        layout_continuedumny.setVisibility(View.VISIBLE);
        layout_continue.setVisibility(View.GONE);

        parseCountriesData();
        /*country_zipcode="+"+getCountryZipCode(getResources().getConfiguration().locale.getCountry());
        if(country_zipcode==null)
            country_zipcode="+91";
        tv_countrydata.setText(country_zipcode);*/

        et_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString();
                validateMobileLogin(et_mobile.getText().toString());
            }
        });
    }

    private void validateMobileLogin(String mobile)
    {
        if(mobile.trim().length()>3)
        {
            layout_continue.setVisibility(View.VISIBLE);
            layout_continuedumny.setVisibility(View.GONE);
        }
        else {
            layout_continue.setVisibility(View.GONE);
            layout_continuedumny.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.iv_backbtn:
                finish();
                break;
            case R.id.layout_continue:
                 Utils.callToast(ChangePhoneNumberActivity.this,"Continue");
                break;

            case R.id.country_layout:
                displayContrylistDialog();
                break;
        }
    }

    private void parseCountriesData()
    {
        list=new ArrayList<>();
        Country_model model;
        InputStream inputStream = getResources().openRawResource(R.raw.country);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int ctr;
        try {
            ctr = inputStream.read();
            while (ctr != -1) {
                byteArrayOutputStream.write(ctr);
                ctr = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            // Parse the data into jsonobject to get original data in form of json.
            JSONObject jObject = new JSONObject(
                    byteArrayOutputStream.toString());
            Log.e("parsed data","is "+jObject.toString());

            JSONArray countryCodes=jObject.getJSONArray("countryCodes");
            for(int i=0;i<countryCodes.length();i++)
            {
                model=new Country_model();
                JSONObject item=countryCodes.getJSONObject(i);
                model.setCountry_code(item.optString("dialling_code"));
                String country_code=item.optString("country_code");
                model.setNamecode(country_code);
                model.setCountry_name(item.optString("country_name"));

                Uri otherPath=null;
                if(country_code.equalsIgnoreCase("do"))
                    otherPath = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/mipmap/doi");
                else
                    otherPath = Uri.parse("android.resource://"+ BuildConfig.APPLICATION_ID+"/mipmap/"+(country_code.toLowerCase()));

                model.setFlagpath(otherPath.toString());
                list.add(model);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayContrylistDialog()
    {
        dialog=new Dialog(ChangePhoneNumberActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_countrylist);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RecyclerView recyclerView=(RecyclerView) dialog.findViewById(R.id.recycler_view);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setReverseLayout(false);
        recyclerView.setLayoutManager(layoutManager);
        adapter=new CountryListAdapter(ChangePhoneNumberActivity.this,list);
        adapter.setClicklistener(this);
        recyclerView.setAdapter(adapter);

        EditText et_search=(EditText)dialog.findViewById(R.id.et_search);
        final TextView tv_noresults=(TextView)dialog.findViewById(R.id.tv_noresults);
        tv_noresults.setVisibility(View.GONE);

        et_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString(),tv_noresults);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
    }

    private void filter(String text,TextView tv_noresults) {
        //new array list that will hold the filtered data
        ArrayList<Country_model> filterdNames = new ArrayList<>();

        //looping through existing elements
        for(int i=0;i<list.size();i++)
        {
            if(list.get(i).getCountry_name().toLowerCase().contains(text.toLowerCase()))
                filterdNames.add(list.get(i));
        }
        //calling a method of the adapter class and passing the filtered list
        if(filterdNames.size()==0)
            tv_noresults.setVisibility(View.VISIBLE);
        else
            tv_noresults.setVisibility(View.GONE);
        adapter.filterList(filterdNames);
    }

    @Override
    public void onCountrySelected(Country_model model) {
        country_namecode=model.getNamecode();
        country_zipcode=model.getCountry_code();
        country_name=model.getCountry_name();
        dialog.dismiss();
        tv_countrydata.setText(country_zipcode);
    }

    public String getCountryZipCode(String id){
        String CountryID=id.trim();
        String CountryZipCode="";
        String[] rl=this.getResources().getStringArray(R.array.CountryCodes);
        for(int i=0;i<rl.length;i++){
            String[] g=rl[i].split(",");
            if(g[1].trim().equals(CountryID.trim())){
                CountryZipCode=g[0];
                break;
            }
        }
        return CountryZipCode;
    }
}
