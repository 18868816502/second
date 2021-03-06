package com.beiwo.qnejqaz.loan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.Api;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.Product;
import com.beiwo.qnejqaz.helper.DataHelper;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.tang.rx.RxResponse;
import com.beiwo.qnejqaz.tang.rx.observer.ApiObserver;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import io.reactivex.annotations.NonNull;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/10/18
 */

public class ProTypeActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tv_toolbar_title;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private ProType1Adapter type1Adapter;
    public int productType;
    private long nao;

    @Override
    public int getLayoutId() {
        return R.layout.activity_protype;
    }

    @Override
    public void configViews() {
        nao = System.currentTimeMillis();
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        refresh_layout.setEnableRefresh(false);
        refresh_layout.setEnableLoadMore(false);
    }

    @Override
    public void initDatas() {
        productType = getIntent().getIntExtra("productType", -1);
        if (productType == 1) {
            tv_toolbar_title.setText("新品推荐专区");
            type1Adapter = new ProType1Adapter(R.mipmap.ic_head_btn1);
        }
        if (productType == 2) {
            tv_toolbar_title.setText("闪电到账专区");
            type1Adapter = new ProType1Adapter(R.mipmap.ic_head_btn2);
        }
        if (productType == 3) {
            tv_toolbar_title.setText("大额低息专区");
            type1Adapter = new ProType1Adapter(R.mipmap.ic_head_btn3);
        }
        if (productType == 4) {
            tv_toolbar_title.setText("不查征信专区");
            type1Adapter = new ProType1Adapter(R.mipmap.ic_head_btn4);
        }

        initRecycler();
        request();
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(type1Adapter);
    }

    private void request() {
        Map<String, Object> map = new HashMap<>();
        map.put("pageNo", 1);
        map.put("pageSize", 50);
        map.put("platform", 1);
        map.put("type", 5);
        map.put("productType", productType);
        Api.getInstance().products(map)
                .compose(RxResponse.<List<Product>>compatT())
                .subscribe(new ApiObserver<List<Product>>() {
                    @Override
                    public void onNext(@NonNull List<Product> data) {
                        refresh_layout.finishRefresh();
                        if (data == null || data.size() < 1) {
                            type1Adapter.setData(null);
                        } else {
                            type1Adapter.setData(data);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        refresh_layout.finishRefresh();
                        type1Adapter.setData(null);
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (productType == 1) {
            if (nao != 0 && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
                DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_NEWPRODUCTSDIVISION, "", System.currentTimeMillis() - nao);
            }
        }
        if (productType == 2) {
            if (nao != 0 && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
                DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_LIGHTNINGACCOUNTDIVISION, "", System.currentTimeMillis() - nao);
            }
        }
        if (productType == 3) {
            if (nao != 0 && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
                DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_LARGELIMITLOWINTEREST, "", System.currentTimeMillis() - nao);
            }
        }
        if (productType == 4) {
            if (nao != 0 && System.currentTimeMillis() - nao > 500 && System.currentTimeMillis() - nao < DataHelper.MAX_SECOND) {
                DataHelper.getInstance(this).event(DataHelper.EVENT_TYPE_STAY, DataHelper.EVENT_VIEWID_NOCREDITREPORTINGDIVISION, "", System.currentTimeMillis() - nao);
            }
        }
    }
}