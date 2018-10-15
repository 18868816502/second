package com.beihui.market.social.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

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
    private int flag = 1;

    public AuditedTopicAdapter(int flag) {
        super(R.layout.item_audited_topic);
        datas = new ArrayList<>();
        this.flag = flag;
    }

    @Override
    protected void convert(BaseViewHolder helper, DraftsBean item) {
        helper.setText(R.id.tv_title,item.getTitle())
        .setText(R.id.tv_date,item.getGmtCreate());
        helper.addOnClickListener(R.id.tv_delete);
        if(flag == 1){
            helper.setVisible(R.id.tv_audit_state,true);
            if(TextUtils.equals("2",item.getForumStatus())){
                helper.setText(R.id.tv_audit_state,"审核失败");
            }else if(TextUtils.equals("3",item.getForumStatus())){
                helper.setText(R.id.tv_audit_state,"未提交");
            }else{
                helper.setText(R.id.tv_audit_state,"未上线");
            }

        }else{
            helper.setVisible(R.id.tv_audit_state,false);
        }
    }

    public void notifyDraftsChanged(List<DraftsBean> list) {
        if (list != null && list.size() > 0) {
            datas.addAll(list);
        }
        setNewData(datas);
    }

}
