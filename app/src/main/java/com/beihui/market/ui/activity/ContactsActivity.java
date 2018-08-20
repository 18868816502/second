package com.beihui.market.ui.activity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.entity.ContactBean;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.ui.adapter.ContactAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ContactsActivity extends BaseComponentActivity {

    @BindView(R.id.contact_smart_refresh)
    SmartRefreshLayout contactRefreshLayout;
    @BindView(R.id.contact_recycler)
    RecyclerView contactRecycler;
    private List<ContactBean> list = new ArrayList<>();
    private ContactAdapter adapter;
    @BindView(R.id.tool_bar)
    Toolbar toolbar;

    @Override
    public int getLayoutId() {
        return R.layout.activity_contacts_layout;
    }

    @Override
    public void configViews() {
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        setupToolbar(toolbar);
        contactRefreshLayout.setOnRefreshListener(refresh());

    }

    private OnRefreshListener refresh() {
        return new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getContactList();
            }
        };
    }

    @Override
    public void initDatas() {
        adapter = new ContactAdapter(R.layout.contact_item_layout, list, this);
        checkPermission();
        contactRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        contactRecycler.setAdapter(adapter);

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }


    private void checkPermission() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.READ_CONTACTS);
            if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 100);
                return;
            }
            getContactList();

        } else {
            getContactList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList();
        } else {
            Toast.makeText(ContactsActivity.this, "授权失败", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //获取联系人列表
    private void getContactList() {
        list.clear();
        String[] cols = {ContactsContract.PhoneLookup.DISPLAY_NAME, ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI, ContactsContract.CommonDataKinds.Phone.NUMBER};
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, cols, null, null, null);
        contactRefreshLayout.finishRefresh();
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                int nameFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
                int photoFieldColumnIndex = cursor.getColumnIndex(ContactsContract.PhoneLookup.PHOTO_THUMBNAIL_URI);
                int numberFieldColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String name = cursor.getString(nameFieldColumnIndex);
                String number = cursor.getString(numberFieldColumnIndex);
                String photo = cursor.getString(photoFieldColumnIndex);
                ContactBean contactBean = new ContactBean();
                contactBean.setDisplayName(name);
                contactBean.setPhoneNum(number);
                contactBean.setPhoto(photo);
                list.add(contactBean);
            }
            cursor.close();
        }
        adapter.setNewData(list);
    }

    @Override
    protected void onStop() {

        super.onStop();
    }
}
