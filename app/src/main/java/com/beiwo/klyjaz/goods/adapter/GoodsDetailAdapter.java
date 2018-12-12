package com.beiwo.klyjaz.goods.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.entity.GoodsComment;
import com.beiwo.klyjaz.entity.GoodsInfo;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.beiwo.klyjaz.view.flowlayout.TagFlowLayout;
import com.bumptech.glide.Glide;

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
 * @date: 2018/12/12
 */
public class GoodsDetailAdapter extends RecyclerView.Adapter<GoodsDetailAdapter.ViewHolder> {
    private static final int TYPE_DETAIL_HEAD = R.layout.layout_goods_detail_head;//0
    private static final int TYPE_TOTAL_COMMENT = R.layout.layout_goods_total_comment;//1
    private static final int TYPE_COMMENT_LIST = R.layout.temlapte_recycler;//2

    @Override
    public int getItemCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_DETAIL_HEAD;
        else if (position == 1) return TYPE_TOTAL_COMMENT;
        else if (position == 2) return TYPE_COMMENT_LIST;
        else return -1;
    }

    private Activity context;
    private CommentTextAdapter textAdapter = new CommentTextAdapter();
    private List<String> commTexts = new ArrayList<>();
    private GoodsInfo goodsInfo = null;
    private CommentsTotal commentsTotal = null;
    private List<GoodsComment> goodsComments = new ArrayList<>();

    public void setDetailInfo(GoodsInfo info) {
        goodsInfo = info;
        notifyItemChanged(0);
    }

    public void setCommentsTotal(CommentsTotal total) {
        commentsTotal = total;
        notifyItemChanged(1);
    }

    public void setGoodsComments(List<GoodsComment> comments) {
        goodsComments = comments;
        notifyItemChanged(2);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (holder.viewType == TYPE_DETAIL_HEAD) {
            if (goodsInfo == null) return;
            Glide.with(context).load(goodsInfo.getLogo())
                    .transform(new GlideCircleTransform(context))
                    .into(holder.iv_detail_icon);
            holder.tv_detail_name.setText(goodsInfo.getName());
            holder.tv_detail_content.setText(goodsInfo.getIntroduce());
            holder.tv_detail1_range_money.setText(goodsInfo.getMinQuota() + "-" + goodsInfo.getMaxQuota() + "元");
            holder.tv_detail2_range_day.setText(goodsInfo.getTerm());
            holder.tv_detail3_rate.setText(goodsInfo.getRate());
            holder.tv_detail4_speed.setText(goodsInfo.getLoanSpeed());
            holder.tv_detail5_way.setText(goodsInfo.getAuditMethod());
            holder.tv_detail6_extra.setText(goodsInfo.getCreditContext());
        }
        if (holder.viewType == TYPE_TOTAL_COMMENT) {
            holder.tv_comment_all.setText("");
            try {//解决ratingbar流泪问题
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_stared);
                int scroeHeight = bmp.getHeight();
                ViewGroup.LayoutParams params = holder.rb_comment_progress.getLayoutParams();
                params.width = -2;
                params.height = scroeHeight;
                holder.rb_comment_progress.setLayoutParams(params);
                holder.rb_comment_progress.setRating(3.5f);
            } catch (Exception e) {
            }

            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recycler_comment_box.setLayoutManager(layoutManager);
            holder.recycler_comment_box.setAdapter(textAdapter);

            commTexts.add("全部 (34798)");
            commTexts.add("好评 (34798)");
            commTexts.add("中评 (500)");
            commTexts.add("差评 (34798)");
            textAdapter.setNewData(commTexts);
        }
        if (holder.viewType == TYPE_COMMENT_LIST) {

        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        //详情
        private ImageView iv_detail_icon;
        private TextView tv_detail_name;
        private TextView tv_detail_content;
        private TextView tv_detail1_range_money;
        private TextView tv_detail2_range_day;
        private TextView tv_detail3_rate;
        private TextView tv_detail4_speed;
        private TextView tv_detail5_way;
        private TextView tv_detail6_extra;
        //评价汇总
        private TextView tv_comment_all;
        private RatingBar rb_comment_progress;
        private RecyclerView recycler_comment_box;
        private TagFlowLayout tfl_tag;
        //评价列表
        private RecyclerView recycler;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_DETAIL_HEAD) {
                iv_detail_icon = itemView.findViewById(R.id.iv_detail_icon);
                tv_detail_name = itemView.findViewById(R.id.tv_detail_name);
                tv_detail_content = itemView.findViewById(R.id.tv_detail_content);
                tv_detail1_range_money = itemView.findViewById(R.id.tv_detail1_range_money);
                tv_detail2_range_day = itemView.findViewById(R.id.tv_detail2_range_day);
                tv_detail3_rate = itemView.findViewById(R.id.tv_detail3_rate);
                tv_detail4_speed = itemView.findViewById(R.id.tv_detail4_speed);
                tv_detail5_way = itemView.findViewById(R.id.tv_detail5_way);
                tv_detail6_extra = itemView.findViewById(R.id.tv_detail6_extra);
            }
            if (viewType == TYPE_TOTAL_COMMENT) {
                tv_comment_all = itemView.findViewById(R.id.tv_comment_all);
                rb_comment_progress = itemView.findViewById(R.id.rb_comment_progress);
                recycler_comment_box = itemView.findViewById(R.id.recycler_comment_box);
                tfl_tag = itemView.findViewById(R.id.tfl_tag);
            }
            if (viewType == TYPE_COMMENT_LIST) {
                recycler = itemView.findViewById(R.id.recycler);
            }
        }
    }
}