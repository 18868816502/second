package com.beiwo.klyjaz.tang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/9/11
 */

public class SocialAdapter extends FragmentPagerAdapter {
    private List<Fragment> mList;

    public SocialAdapter(FragmentManager fm) {
        super(fm);
        mList = new ArrayList<>();
    }

    public void setDatas(List<Fragment> mList){
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }


    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }
}