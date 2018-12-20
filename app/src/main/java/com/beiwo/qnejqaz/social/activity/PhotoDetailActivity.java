package com.beiwo.qnejqaz.social.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.activity
 * @descripe
 * @time 2018/10/22 11:23
 */
public class PhotoDetailActivity extends BaseComponentActivity {

    @BindView(R.id.bgaBanner)
    BGABanner banner;
    private List<String> datas;
    private int position;

    @Override
    public int getLayoutId() {
        return R.layout.activity_photo_detail;
    }

    @Override
    public void configViews() {
        Intent intent = getIntent();
        if (intent != null) {
            datas = intent.getStringArrayListExtra("datas");
            position = intent.getIntExtra("position", 0);
        }
        banner.setData(R.layout.item_view_banner_photo,datas, null);
        banner.setAutoPlayAble(false);
        banner.setCurrentItem(position);
        banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
            @Override
            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                Glide.with(PhotoDetailActivity.this).load(model).into(itemView);
            }
        });
    }

    @Override
    public void initDatas() {

    }

    @OnClick(R.id.back_container)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.back_container:
               finish();
                break;
            default:
                break;
        }
    }
}
