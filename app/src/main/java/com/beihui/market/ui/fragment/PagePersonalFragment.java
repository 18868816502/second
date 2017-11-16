package com.beihui.market.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.base.BaseComponentFragment;
import com.beihui.market.entity.LoanGroup;
import com.beihui.market.injection.component.AppComponent;
import com.beihui.market.injection.component.DaggerPagePersonalComponent;
import com.beihui.market.injection.module.PagePersonalModule;
import com.beihui.market.ui.adapter.ProductGroupAdapter;
import com.beihui.market.ui.contract.PagePersonalContract;
import com.beihui.market.ui.presenter.PagePersonalPresenter;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class PagePersonalFragment extends BaseComponentFragment implements PagePersonalContract.View {

    @BindView(R.id.notice_container)
    View noticeContainer;
    @BindView(R.id.notice_content)
    TextView noticeContentView;
    @BindView(R.id.notice_close)
    View noticeCloseView;
    @BindView(R.id.recommend_group_recycler_view)
    RecyclerView groupRecyclerView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @Inject
    PagePersonalPresenter presenter;

    private ProductGroupAdapter groupAdapter;

    @Override
    public int getLayoutResId() {
        return R.layout.pager_item_personal_recommend;
    }

    @Override
    public void configViews() {
        groupAdapter = new ProductGroupAdapter();
        groupAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        groupRecyclerView.setAdapter(groupAdapter);

        viewPager.setAdapter(new PersonalPageAdapter(getChildFragmentManager()));
    }

    @Override
    public void initDatas() {
        presenter.loadProductGroup();
        presenter.loadProductHint();
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
    public void showProductHint(List<String> msg) {
        noticeContainer.setVisibility(View.VISIBLE);
        noticeCloseView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                noticeContainer.setVisibility(View.GONE);
            }
        });
        StringBuilder sb = new StringBuilder();
        for (String item : msg) {
            sb.append(item).append("\n");
        }
        noticeContentView.setText(sb);
    }

    @Override
    public void showProductGroup(List<LoanGroup> groups) {
        if (isAdded()) {
            if (groupAdapter != null) {
                groupAdapter.notifyLoanGroupChanged(groups);
            }
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
