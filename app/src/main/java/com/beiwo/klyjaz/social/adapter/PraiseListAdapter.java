package com.beiwo.klyjaz.social.adapter;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.bean.PraiseListBean;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beihui.market.social.adapter
 * @descripe
 * @time 2018/10/13 11:54
 */
public class PraiseListAdapter extends BaseQuickAdapter<PraiseListBean, BaseViewHolder> {
    private List<PraiseListBean> dataSet = new ArrayList<>();
    private int type = 1;

    public PraiseListAdapter(int type) {
        super(R.layout.item_social_praise);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PraiseListBean item) {
        if(!TextUtils.isEmpty(item.getHeadPortrait())) {
            Glide.with(mContext).load(item.getHeadPortrait()).into((ImageView) helper.getView(R.id.iv_avatar));
        }
        helper.addOnClickListener(R.id.iv_avatar);
        if (item.getImageList() != null && item.getImageList().size() != 0) {
            helper.getView(R.id.iv_content).setVisibility(View.VISIBLE);
            Glide.with(mContext).load(item.getImageList().get(0).getImgUrl()).into((ImageView) helper.getView(R.id.iv_content));
        } else {
            helper.getView(R.id.iv_content).setVisibility(View.GONE);
        }
        helper.setText(R.id.tv_name, item.getUserName())
                .setText(R.id.tv_date, item.getCreateText())
                .setText(R.id.tv_content, item.getForumTitle());

        if(type == 1){
            String mPraiseStr = "";
            switch (item.getPraiseType()){
                case "0":
                    mPraiseStr = "动态";
                    break;
                case "1":
                    mPraiseStr = "评论";
                    break;
                case "2":
                    mPraiseStr = "回复";
                    break;
                default:
                    break;
            }
            helper.setText(R.id.tv_praise_type, "赞了你的" + mPraiseStr);

        }else{
            helper.setText(R.id.tv_praise_type, (TextUtils.equals(item.getCommentType(), "1") ? "评论" : "回复")
                    + "我:"
                    + (TextUtils.isEmpty(item.getCommentContent())?"暂无":item.getCommentContent()));
        }
    }

    public void notifyPraiseChanged(List<PraiseListBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}