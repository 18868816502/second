package com.beihui.market.ui.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.widget.RelativeLayout;

import com.beihui.market.R;
import com.beihui.market.api.NetConstants;
import com.beihui.market.base.BaseComponentActivity;
import com.beihui.market.helper.SlidePanelHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.util.ImageUtils;
import com.beihui.market.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;
import com.just.agentweb.AgentWeb;

import butterknife.BindView;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class DailyMissonActivity extends BaseComponentActivity {

    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.web_view_invitation)
    RelativeLayout relativeLayout;
    private Context context;
    private AgentWeb agentWeb;


    @Override
    public int getLayoutId() {
        return R.layout.activity_daily_misson_layout;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        setupToolbarBackNavigation(toolbar, R.drawable.back_white);
        ImmersionBar.with(this).statusBarDarkFont(false).init();
        SlidePanelHelper.attach(this);
        context = this;

    }

    @Override
    public void initDatas() {
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(relativeLayout, new RelativeLayout.LayoutParams(-1, -1)).
                        useDefaultIndicator(getResources().getColor(R.color.red), 1)
                .createAgentWeb()
                .ready()
                .go(NetConstants.missionUrl(UserHelper.getInstance(context).getProfile().getId()));
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }

    @OnClick(R.id.tool_title)
    void uploadPhoto() {
        //DailyMissonActivityPermissionsDispatcher.openAlbumWithCheck(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            String path = null;
            Bitmap avatar = null;
            if (requestCode == 1) {
                path = getRealPathFromURI(data.getData());
            }
            if (path != null) {
                avatar = ImageUtils.getFixedBitmap(path, 512);
            }
            if (avatar != null) {
                //upload(avatar);
            } else {
                ToastUtil.toast("图片解析错误");
            }
        }
    }

    private void upload(Bitmap img) {


    }

    @SuppressLint("InlinedApi")
    @NeedsPermission({Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void openAlbum() {
        Intent toAlbum = new Intent(Intent.ACTION_PICK);
        toAlbum.setType("image/*");
        startActivityForResult(toAlbum, 2);
    }

    String getRealPathFromURI(Uri uri) {
        if (uri != null) {
            //小米手机
            if (uri.getScheme().equals("file")) {
                return uri.getEncodedPath();
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Cursor cursor = getContentResolver().query(uri, null, null, null, null);
                if (cursor != null) {
                    cursor.moveToFirst();
                    int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    String path = cursor.getString(idx);
                    cursor.close();
                    return path;
                }
            } else {
                return uri.getPath();
            }
        }
        return null;
    }


}
