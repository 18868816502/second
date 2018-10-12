package com.beihui.market.social.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import com.beihui.market.R;
import com.beihui.market.social.bean.DraftsBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beihui.market.social.adapter
 * @descripe
 * @time 2018/10/12 16:14
 */
public class AuditedTopicAdapter extends BaseQuickAdapter<DraftsBean, BaseViewHolder> {

    private List<DraftsBean> datas;

    public AuditedTopicAdapter() {
        super(R.layout.item_audited_topic);
        datas = new ArrayList<>();
    }

    @Override
    protected void convert(BaseViewHolder helper, DraftsBean item) {
        helper.setText(R.id.tv_title,item.getTitle())
        .setText(R.id.tv_date,item.getGmtCreate());
    }

    public void notifyDraftsChanged(List<DraftsBean> list) {
        if (list != null && list.size() > 0) {
            datas.addAll(list);
        }
        setNewData(datas);
    }
}
