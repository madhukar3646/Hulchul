package com.app.zippnews.activities;

import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.app.zippnews.R;
import com.app.zippnews.adapters.FindContactsAdapter;
import com.app.zippnews.model.ContactsModel;
import com.app.zippnews.utils.ConnectionDetector;
import com.app.zippnews.utils.SessionManagement;
import com.app.zippnews.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FindContactsActivity extends AppCompatActivity {

    @BindView(R.id.back_btn)
    ImageView back_btn;
    @BindView(R.id.rv_list)
    RecyclerView rv_list;
    private ConnectionDetector connectionDetector;
    private SessionManagement sessionManagement;
    private FindContactsAdapter findContactsAdapter;
    private ArrayList<ContactsModel> contactsModelArrayList=new ArrayList<>();
    private HashMap<String,String> filterContacts_hmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_contacts);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        connectionDetector=new ConnectionDetector(FindContactsActivity.this);
        sessionManagement=new SessionManagement(FindContactsActivity.this);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rv_list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        findContactsAdapter=new FindContactsAdapter(FindContactsActivity.this,contactsModelArrayList);
        rv_list.setAdapter(findContactsAdapter);

        showContacts();
    }

    private void showContacts()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.showDialog(FindContactsActivity.this);
                getContacts();
            }
        });
    }

    private void displayContacts()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Utils.dismissDialog();
                findContactsAdapter.notifyDataSetChanged();
            }
        });
    }

    public void getContacts() {
        Cursor c = null;
        try {
            c = getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC");
            filterContacts_hmap=new HashMap<>();
            while (c.moveToNext()) {
                ContactsModel model=new ContactsModel();
                String contactID = c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));
                String contactName = c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                String phNumber = c.getString(c
                        .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

                String contact = phNumber.replace(" ", "");
                String contact1 = contact.replace("-", "");
                //String contact4 = contact1.replace("+", "");
                String contact2 = contact1.replace("(", "");
                String contact3 = contact2.replace(")", "");
                String number=contact3;

                if(!filterContacts_hmap.containsKey(contactID)){
                    filterContacts_hmap.put(contactID, contactID);
                    model.setId(contactID);
                    model.setName(contactName);
                    model.setNumber(number);
                    contactsModelArrayList.add(model);
                }
            }
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayContacts();
    }
}
