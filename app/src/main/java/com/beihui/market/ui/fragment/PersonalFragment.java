package com.beihui.market.ui.fragment;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.injection.component.AppComponent;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/7/17
 */

public class PersonalFragment extends BaseComponentFragment {

    public static PersonalFragment newInstance() {
        return new PersonalFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.f_fragment_personal;
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
