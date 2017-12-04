package com.beihui.market.ui.fragment;


import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanGroup;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerPagePersonalComponent;
import com.beihui.market.injection.module.PagePersonalModule;
import com.beihui.market.ui.adapter.ProductGroupAdapter;
import com.beihui.market.ui.contract.PagePersonalContract;
import com.beihui.market.ui.presenter.PagePersonalPresenter;
import com.beihui.market.view.StateLayout;
import com.beihui.market.view.stateprovider.ProductStateProvider;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PagePersonalFragment extends BaseComponentFragment implements PagePersonalContract.View {

    @BindView(R.id.state_layout)
    StateLayout stateLayout;
    @BindView(R.id.recommend_group_recycler_view)
    RecyclerView groupRecyclerView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Inject
    PagePersonalPresenter presenter;

    private ProductGroupAdapter groupAdapter;
    private PersonalPageAdapter pagerAdapter;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //pv，uv统计
            DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_RESUME_PERSONAL_PRODUCT);
        }
    }

    @Override
    public int getLayoutResId() {
        return R.layout.pager_item_personal_recommend;
    }

    @Override
    public void configViews() {
        stateLayout.setStateViewProvider(new ProductStateProvider(getContext(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loadProductGroup();
            }
        }));
        groupAdapter = new ProductGroupAdapter();
        groupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (groupAdapter.select(position)) {
                    viewPager.setCurrentItem(position);
                }
            }
        });
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        groupRecyclerView.setAdapter(groupAdapter);
        groupRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            int padding = (int) (getResources().getDisplayMetrics().density * 5);

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.right = padding;
            }
        });

        pagerAdapter = new PersonalPageAdapter(getChildFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                groupAdapter.select(position);
                //pv，uv统计
                DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SELECT_PERSONAL_GROUP);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void initDatas() {
        presenter.loadProductGroup();
    }

    @Override
    protected void configureComponent(AppComponent appComponent) {
        DaggerPagePersonalComponent.builder()
                .appComponent(appComponent)
                .pagePersonalModule(new PagePersonalModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void setPresenter(PagePersonalContract.Presenter presenter) {
        //
    }

    @Override
    public void showProductGroup(List<LoanGroup> groups) {
        if (isAdded()) {
            stateLayout.switchState(StateLayout.STATE_CONTENT);
            if (groupAdapter != null) {
                groupAdapter.notifyLoanGroupChanged(groups);
            }
            if (pagerAdapter != null) {
                pagerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showNoProductGroup() {
        if (isAdded()) {
            stateLayout.switchState(StateLayout.STATE_EMPTY);
        }
    }

    @Override
    public void showNetError() {
        if (isAdded()) {
            stateLayout.switchState(StateLayout.STATE_NET_ERROR);
        }
    }

    class PersonalPageAdapter extends FragmentPagerAdapter {

        PersonalPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle param = new Bundle();
            param.putString("groupId", groupAdapter.getItem(position).getId());
            PagePersonalProductFragment fragment = new PagePersonalProductFragment();
            fragment.setArguments(param);
            return fragment;
        }

        @Override
        public int getCount() {
            return groupAdapter != null ? groupAdapter.getItemCount() : 0;
        }
    }
}
