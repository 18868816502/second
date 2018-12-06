package com.beiwo.klyjaz.social.fragment;


import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.base.BaseComponentFragment;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/6
 */
public class TopicFragment extends BaseComponentFragment {
    private int orderType;

    @Override
    public int getLayoutResId() {
        return R.layout.temlapte_recycler;
    }

    @Override
    public void configViews() {
        orderType = getArguments().getInt("type");
    }

    @Override
    public void initDatas() {

    }

    public static TopicFragment getInstance() {
        return new TopicFragment();
    }
}