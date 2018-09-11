package com.beihui.market.tang.fragment;

import android.support.v7.widget.RecyclerView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 关注
 * @modify:
 * @date: 2018/9/11
 */

public class FollowFragment extends BaseComponentFragment {
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    public int getLayoutResId() {
        return R.layout.f_template_recycler_layout;
    }

    @Override
    public void configViews() {

    }

    @Override
    public void initDatas() {

    }

    @Override
    protected void configureComponent(AppComponent appComponent) {

    }
}
