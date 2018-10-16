package com.beiwo.klyjaz.social.adapter;

import android.text.TextUtils;
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
public class PraiseListAdapter extends BaseQuickAdapter<PraiseListBean,BaseViewHolder> {

    private List<PraiseListBean> dataSet = new ArrayList<>();
    private int type = 1;

    public PraiseListAdapter(int type) {
        super(R.layout.item_social_praise);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, PraiseListBean item) {
        Glide.with(mContext).load(item.getHeadPortrait()).into((ImageView) helper.getView(R.id.iv_avatar));
        if(item.getImageList()!=null&&item.getImageList().size() != 0){
            Glide.with(mContext).load(item.getImageList().get(0).getImgUrl()).into((ImageView) helper.getView(R.id.iv_avatar));
        }else{
            helper.setVisible(R.id.iv_content,false);
        }
        helper.setText(R.id.tv_name,item.getUserName())
                .setText(R.id.tv_date,item.getCreateText())
                .setText(R.id.tv_praise_type,"赞了你的"+ (TextUtils.equals(item.getCommentType(),"1")?"评论":"回复"))
                .setText(R.id.tv_content,item.getCommentContent());
    }

    public void notifyPraiseChanged(List<PraiseListBean> list) {
        dataSet.clear();
        if (list != null && list.size() > 0) {
            dataSet.addAll(list);
        }
        setNewData(dataSet);
    }
}
