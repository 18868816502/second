package com.beiwo.qnejqaz.ui.activity;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.base.BaseComponentActivity;
import com.beiwo.qnejqaz.entity.Profession;
import com.beiwo.qnejqaz.helper.SlidePanelHelper;
import com.beiwo.qnejqaz.ui.contract.EditProfessionContract;
import com.beiwo.qnejqaz.ui.presenter.EditProfessionPresenter;
import com.beiwo.qnejqaz.util.WeakRefToastUtil;

import java.util.List;

import butterknife.BindView;

public class EditJobGroupActivity extends BaseComponentActivity implements EditProfessionContract.View, View.OnClickListener {
    @BindView(R.id.tool_bar)
    Toolbar toolbar;
    @BindView(R.id.profession_container)
    LinearLayout professionContainer;
    private View curSelectedView;

    EditProfessionPresenter presenter;

    @Override
    protected void onDestroy() {
        presenter.onDestroy();
        presenter = null;
        super.onDestroy();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_edit_job_group;
    }

    @Override
    public void configViews() {
        setupToolbar(toolbar);
        SlidePanelHelper.attach(this);
        presenter = new EditProfessionPresenter(this,this);
    }

    @Override
    public void initDatas() {
        presenter.onStart();
    }

    @Override
    public void onClick(View view) {
        if (view != curSelectedView) {
            if (curSelectedView != null) {
                curSelectedView.setSelected(false);
            }
            curSelectedView = view;
            curSelectedView.setSelected(true);

            presenter.updateProfession((Profession) view.getTag());
        }
    }

    @Override
    public void setPresenter(EditProfessionContract.Presenter presenter) {
        //injected.nothing to do.
    }

    @Override
    public void showProfession(List<Profession> professions) {
        if (professions != null && professions.size() > 0) {
            LayoutInflater inflater = LayoutInflater.from(this);
            for (int i = 0, count = professions.size(); i < count; ++i) {
                Profession pro = professions.get(i);
                TextView view = (TextView) inflater.inflate(R.layout.rv_item_profession, professionContainer, false);
                professionContainer.addView(view);
                view.setText(pro.getText());
                view.setOnClickListener(this);

                view.setTag(pro);

                if (pro.getSelected() != 0) {
                    view.setSelected(true);
                    curSelectedView = view;
                }
            }
        }
    }

    @Override
    public void showErrorMsg(String msg) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
    }

    @Override
    public void showUpdateSuccess(String msg) {
        dismissProgress();
        WeakRefToastUtil.showShort(this, msg, null);
        finish();
    }
}
