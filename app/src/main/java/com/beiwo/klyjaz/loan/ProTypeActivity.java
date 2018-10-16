package com.beiwo.klyjaz.loan;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.beiwo.klyjaz.App;
import com.beiwo.klyjaz.BuildConfig;
import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.base.BaseComponentActivity;
import com.beiwo.klyjaz.helper.SlidePanelHelper;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.injection.component.AppComponent;
import com.beiwo.klyjaz.tang.rx.RxResponse;
import com.beiwo.klyjaz.tang.rx.observer.ApiObserver;
import com.beiwo.klyjaz.ui.activity.MainActivity;
import com.beiwo.klyjaz.ui.activity.UserAuthorizationActivity;
import com.beiwo.klyjaz.ui.activity.WebViewActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
 * @date: 2018/10/11
 */

public class ProTypeActivity extends BaseComponentActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_title)
    TextView tv_toolbar_title;
    @BindView(R.id.tv_head_txt)
    TextView tv_head_txt;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private Context context;
    private int productType;
    private String type1str = "超多新口子   下款就通过";
    private String type2str = "快借极速贷   热门口子秒过";
    private String type3str = "大额低息超   分分秒秒正品";
    private String type4str = "手机微额贷   无视黑白户";
    private Map<String, Object> map = new HashMap<>();
    private int pageNo = 1;
    private int pageSize = 10;
    private ProductAdapter adapter = new ProductAdapter();

    @Override
    public int getLayoutId() {
        return R.layout.activity_pro_type;
    }

    @Override
    public void configViews() {
        context = this;
        setupToolbar(toolbar);
        ImmersionBar.with(this).statusBarDarkFont(true).init();
        SlidePanelHelper.attach(this);
        productType = getIntent().getIntExtra("productType", -1);
        if (productType == 1) {
            tv_toolbar_title.setText("高通过率专区");
            tv_head_txt.setText(type1str);
        }
        if (productType == 2) {
            tv_toolbar_title.setText("闪电到账专区");
            tv_head_txt.setText(type2str);
        }
        if (productType == 3) {
            tv_toolbar_title.setText("大额低息专区");
            tv_head_txt.setText(type3str);
        }
        if (productType == 4) {
            tv_toolbar_title.setText("不查征信专区");
            tv_head_txt.setText(type4str);
        }
    }

    @Override
    public void initDatas() {
        initRecycler();
        request();
        refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo = 1;
                request();
            }
        });
        refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@android.support.annotation.NonNull RefreshLayout refreshLayout) {
                pageNo++;
                map.put("pageNo", pageNo);
                request();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {
                final Product product = adapter.getData().get(position);
                if (BuildConfig.FORCE_LOGIN && !UserHelper.getInstance(context).isLogin()) {
                    startActivity(new Intent(context, UserAuthorizationActivity.class));
                    return;
                }
                String id = UserHelper.getInstance(context).isLogin() ? UserHelper.getInstance(context).id() : App.androidId;
                Api.getInstance().queryGroupProductSkip(id, product.getId())
                        .compose(RxResponse.<String>compatT())
                        .subscribe(new ApiObserver<String>() {
                            @Override
                            public void onNext(@NonNull String data) {
                                Intent intent = new Intent(context, WebViewActivity.class);
                                intent.putExtra("webViewUrl", data);
                                intent.putExtra("webViewTitleName", product.getProductName());
                                startActivity(intent);
                            }
                        });
            }
        });
    }

    private void initRecycler() {
        recycler.setLayoutManager(new LinearLayoutManager(context));
        recycler.setAdapter(adapter);
    }

    private void request() {
        map.put("pageNo", pageNo);
        map.put("pageSize", pageSize);
        map.put("productType", productType);
        Api.getInstance().products(map)
                .compose(RxResponse.<List<Product>>compatT())
                .subscribe(new ApiObserver<List<Product>>() {
                    @Override
                    public void onNext(@NonNull List<Product> data) {
                        refresh_layout.finishRefresh();
                        refresh_layout.finishLoadMore();
                        if (pageNo == 1) {
                            if (data == null || data.size() < 1) {
                                empty();
                            } else {
                                adapter.setNewData(data);
                            }
                        } else {
                            adapter.addData(data);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable t) {
                        super.onError(t);
                        refresh_layout.finishRefresh();
                        refresh_layout.finishLoadMore();
                        empty();
                    }
                });
    }

    private void empty() {
        adapter.setNewData(null);
        adapter.setEmptyView(R.layout.empty_sys_layout, recycler);
        TextView tv_content = adapter.getEmptyView().findViewById(R.id.tv_content);
        tv_content.setText("服务器开小差，去首页 >");
        tv_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("home", true);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
    }
}