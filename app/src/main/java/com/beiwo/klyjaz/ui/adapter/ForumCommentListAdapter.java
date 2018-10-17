package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.constant.ConstantTag;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.bean.CommentReplyBean;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.ui.listeners.OnViewClickListener;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @time 2018/9/12 19:27
 */
public class ForumCommentListAdapter extends BaseQuickAdapter<CommentReplyBean,BaseViewHolder> {

    private Context mContext;
    private List<CommentReplyBean> datas;

    public ForumCommentListAdapter() {
        super(R.layout.item_article_detail_comment);
        this.datas = datas;
    }

    @Override
    protected void convert(BaseViewHolder helper, CommentReplyBean item) {
//        viewHolder.tvCommentPraise.setTag(position);
//        viewHolder.ivArticleComment.setTag(position);
//        viewHolder.ivCommentatorAcatar.setTag(R.id.comment_list_avatar,position);

//        if(!TextUtils.isEmpty(datas.get(position).getUserHeadUrl())) {
//            Glide.with(mContext).load(datas.get(position).getUserHeadUrl()).into(viewHolder.ivCommentatorAcatar);
//        }
//        viewHolder.tvCommentatorName.setText(datas.get(position).getUserName());
//        viewHolder.tvCommentTime.setText(String.valueOf(datas.get(position).getGmtCreate()));
//        viewHolder.tvCommentContent.setText(datas.get(position).getContent());
//        viewHolder.tvCommentPraise.setText(String.valueOf(datas.get(position).getPraiseCount()));
//        if (TextUtils.equals(UserHelper.getInstance(mContext).getProfile().getId(), datas.get(position).getUserId())) {
//            viewHolder.tvCommentDelete.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.tvCommentDelete.setVisibility(View.GONE);
//        }
//        if (viewHolder.adapter != null){
//            viewHolder.adapter.setDatas(datas.get(position).getReplyDtoList());
//        }
    }
    class OnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_commentator_avatar:
                    Intent pIntent = new Intent(mContext, PersonalCenterActivity.class);
                    pIntent.putExtra("userId",datas.get((Integer) v.getTag(R.id.comment_list_avatar)).getUserId());
                    mContext.startActivity(pIntent);
                    break;
                //点赞
                case R.id.tv_comment_praise:
//                    listener.onViewClick(v, ConstantTag.TAG_PRAISE_COMMENT, (Integer) v.getTag());
                    break;
                //评论
                case R.id.iv_article_comment:
//                    listener.onViewClick(v, ConstantTag.TAG_REPLY_COMMENT, (Integer) v.getTag());
                    break;
                case R.id.tv_comment_delete:
//                    listener.onViewClick(v, ConstantTag.TAG_COMMENT_DELETE, (Integer) v.getTag());
                    break;
                default:
                    break;
            }
        }
    }
}
