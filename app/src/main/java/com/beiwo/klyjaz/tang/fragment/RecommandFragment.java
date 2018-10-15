package com.beiwo.klyjaz.tang.fragment;

import android.support.v7.widget.RecyclerView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;
import com.beiwo.klyjaz.injection.component.AppComponent;

import butterknife.BindView;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description: 推荐
 * @modify:
 * @date: 2018/9/11
 */

public class RecommandFragment extends BaseComponentFragment {
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
