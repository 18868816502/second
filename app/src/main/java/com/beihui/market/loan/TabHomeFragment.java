package com.beihui.market.loan;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.AdBanner;
import com.beihui.market.entity.Ticket;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.tang.rx.RxResponse;
import com.beihui.market.tang.rx.observer.ApiObserver;
import com.beihui.market.ui.activity.WebViewActivity;
import com.beihui.market.util.ToastUtil;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/9
 */

public class TabHomeFragment extends BaseComponentFragment {

    @BindView(R.id.bga_banner)
    BGABanner bga_banner;
    @BindView(R.id.adt_looper)
    ADTextView adt_looper;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private List<String> imgs = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private List<String> titles = new ArrayList<>();

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_tab_home;
    }

    @Override
    public void configViews() {
        initBanner();
        initLooperAd();
        initRecycler();
    }

    private void initRecycler() {

    }

    @Override
    public void initDatas() {
        bga_banner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
            @Override
            public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                intent.putExtra("webViewUrl", urls.get(position));
                intent.putExtra("webViewTitleName", titles.get(position));
                startActivity(intent);
            }
        });
    }

    private void initBanner() {
        Api.getInstance().querySupernatant(2)
                .compose(RxResponse.<List<AdBanner>>compatT())
                .subscribe(new ApiObserver<List<AdBanner>>() {
                    @Override
                    public void onNext(@NonNull List<AdBanner> data) {
                        imgs.clear();
                        urls.clear();
                        for (int i = 0; i < data.size(); i++) {
                            imgs.add(data.get(i).getImgUrl());
                            urls.add(data.get(i).getUrl());
                            titles.add(data.get(i).getTitle());
                        }
                        bga_banner.setData(imgs, null);
                        bga_banner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                            @Override
                            public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                                Glide.with(getActivity())
                                        .load(model)
                                        .placeholder(R.color.transparent)
                                        .error(R.color.transparent)
                                        .centerCrop()
                                        .into(itemView);
                            }
                        });
                    }
                });
    }

    private void initLooperAd() {
        Api.getInstance().queryBorrowingScroll()
                .compose(RxResponse.<List<String>>compatT())
                .subscribe(new ApiObserver<List<String>>() {
                    @Override
                    public void onNext(@NonNull List<String> data) {
                        adt_looper.init(data, null);
                    }
                });
    }

    private void initNormalState() {

    }

    private void initCheckingState() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }

    public static TabHomeFragment newInstance() {
        return new TabHomeFragment();
    }

    @OnClick({R.id.tv_pro_1, R.id.tv_pro_2, R.id.tv_pro_3, R.id.tv_pro_4})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_pro_1:
                ToastUtil.toast("1");
                break;
            case R.id.tv_pro_2:
                ToastUtil.toast("2");
                break;
            case R.id.tv_pro_3:
                ToastUtil.toast("3");
                break;
            case R.id.tv_pro_4:
                ToastUtil.toast("4");
                break;
            default:
                break;
        }
    }
}