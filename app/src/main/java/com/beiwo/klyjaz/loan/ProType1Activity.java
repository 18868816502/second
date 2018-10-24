package com.beiwo.klyjaz.loan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.entity.Product;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
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

public class ProType1Activity extends BaseComponentActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView tv_toolbar_title;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private ProType1Adapter type1Adapter;
    private int productType;

    @Override
    public int getLayoutId() {
        return R.layout.activity_protype;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
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
    protected void configureComponent(AppComponent appComponent) {
    }
}