package com.beiwo.klyjaz.goods.adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.CommentsTotal;
import com.beiwo.klyjaz.entity.GoodsComment;
import com.beiwo.klyjaz.entity.GoodsInfo;
import com.beiwo.klyjaz.entity.Labels;
import com.beiwo.klyjaz.goods.TagUtil;
import com.beiwo.klyjaz.goods.activity.GoodsCommentActivity;
import com.beiwo.klyjaz.view.GlideCircleTransform;
import com.beiwo.klyjaz.view.flowlayout.FlowLayout;
import com.beiwo.klyjaz.view.flowlayout.TagAdapter;
import com.beiwo.klyjaz.view.flowlayout.TagFlowLayout;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;

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
    private static final int TYPE_FOOTER = R.layout.layout_home_footview;//3

    @Override
    public int getItemCount() {
        return 4;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_DETAIL_HEAD;
        else if (position == 1) return TYPE_TOTAL_COMMENT;
        else if (position == 2) return TYPE_COMMENT_LIST;
        else if (position == 3) return TYPE_FOOTER;
        else return -1;
    }

    private Activity context;
    private String name = "产品详情";
    private String cutId, manageId;
    private CommentTextAdapter textAdapter = new CommentTextAdapter();
    private List<String> commTexts = new ArrayList<>();
    private List<String> tags = new ArrayList<>();
    //private List<String> keys = new ArrayList<>();
    private GoodsInfo goodsInfo = null;
    private CommentsTotal commentsTotal = null;
    private List<GoodsComment> goodsComments = new ArrayList<>();
    private GoodsCommentAdapter commentAdapter = new GoodsCommentAdapter();

    public void setDetailInfo(GoodsInfo info) {
        goodsInfo = info;
        if (info != null && !TextUtils.isEmpty(info.getName())) name = info.getName();
        notifyItemChanged(0);
    }

    public void setCommentsTotal(CommentsTotal total) {
        commentsTotal = total;
        if (total != null && !TextUtils.isEmpty(total.getPraiseCutId()))
            cutId = total.getPraiseCutId();
        if (total != null && !TextUtils.isEmpty(total.getManageId()))
            manageId = total.getManageId();
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
    public void onBindViewHolder(final ViewHolder holder, int position) {
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
            if (commentsTotal == null) {
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_unstar);
                int scroeHeight = bmp.getHeight();
                ViewGroup.LayoutParams params = holder.rb_comment_progress.getLayoutParams();
                params.width = -2;
                params.height = scroeHeight;
                holder.rb_comment_progress.setLayoutParams(params);
                holder.rb_comment_progress.setRating(0);
                return;
            }
            holder.tv_comment_all.setText(String.format(context.getString(R.string.comment_all), commentsTotal.getCommentCount()));
            holder.tv_comment_grade.setText(String.format("%.0f", commentsTotal.getGoodCommentRate()));
            try {//解决ratingbar流泪问题
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_stared);
                int scroeHeight = bmp.getHeight();
                ViewGroup.LayoutParams params = holder.rb_comment_progress.getLayoutParams();
                params.width = -2;
                params.height = scroeHeight;
                holder.rb_comment_progress.setLayoutParams(params);
                holder.rb_comment_progress.setRating(commentsTotal.getGoodCommentRate() / 20);
            } catch (Exception e) {
            }
            //好中差评
            holder.tv_good_comment_num.setText(commentsTotal.getGoodCommentCount() + "");
            holder.tv_mid_comment_num.setText(commentsTotal.getMinCommentCount() + "");
            holder.tv_bad_comment_num.setText(commentsTotal.getBadCommentCount() + "");
            holder.pb_good_comment.setMax(commentsTotal.getCommentCount());
            holder.pb_mid_comment.setMax(commentsTotal.getCommentCount());
            holder.pb_bad_comment.setMax(commentsTotal.getCommentCount());
            holder.pb_good_comment.setProgress(commentsTotal.getGoodCommentCount());
            holder.pb_mid_comment.setProgress(commentsTotal.getMinCommentCount());
            holder.pb_bad_comment.setProgress(commentsTotal.getBadCommentCount());
            //评价类别及数量
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.recycler_comment_box.setLayoutManager(layoutManager);
            holder.recycler_comment_box.setAdapter(textAdapter);
            commTexts.clear();
            commTexts.add(String.format(context.getString(R.string.all_comment), commentsTotal.getCommentCount()));
            commTexts.add(String.format(context.getString(R.string.good_comment), commentsTotal.getGoodCommentCount()));
            commTexts.add(String.format(context.getString(R.string.mid_comment), commentsTotal.getMinCommentCount()));
            commTexts.add(String.format(context.getString(R.string.bad_comment), commentsTotal.getBadCommentCount()));
            textAdapter.setNewData(commTexts);
            textAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    nextPage(position, "all");
                }
            });
            //标签
            List<Labels> labels = commentsTotal.getLabelList();
            if (labels != null && labels.size() > 0) {
                tags.clear();
                int tagCountSum = 0;
                for (int i = 0; i < labels.size(); i++) {
                    Labels label = labels.get(i);
                    String value = TagUtil.getKeyValue(label.getFlag(), true);
                    if (value == null) continue;
                    int count = label.getCount();
                    tagCountSum += count;
                    //keys.add(label.getFlag());
                    tags.add(String.format(value, count));
                }
                if (tags.size() <= 0) return;
                //keys.add(0, "all");
                tags.add(0, String.format(context.getString(R.string.all_comment), tagCountSum));//全部标签
                holder.tfl_tag.setAdapter(new TagAdapter<String>(tags) {
                    @Override
                    public View getView(FlowLayout parent, int position, String str) {
                        TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_tag, holder.tfl_tag, false);
                        textView.setBackgroundResource(R.drawable.bg_tag_normal);
                        textView.setTextColor(ContextCompat.getColor(context, R.color.black_2));
                        textView.setText(str);
                        return textView;
                    }
                });
                holder.tfl_tag.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        nextPage(0, tags.get(position));
                        return true;
                    }
                });
            }
            holder.csl_head_wrap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nextPage(0, "all");
                }
            });
        }
        if (holder.viewType == TYPE_COMMENT_LIST) {
            holder.recycler.setFocusableInTouchMode(false);
            holder.recycler.setLayoutManager(new LinearLayoutManager(context) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            });
            holder.recycler.setHasFixedSize(true);
            holder.recycler.setItemAnimator(new DefaultItemAnimator());
            holder.recycler.setAdapter(commentAdapter);
            commentAdapter.setNewData(goodsComments);
        }
        if (holder.viewType == TYPE_FOOTER) {
            holder.tv_footer_txt.setText("继续上拉查看更多评价");
        }
    }

    private void nextPage(int type, String tag) {
        Intent intent = new Intent(context, GoodsCommentActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("tag", tag);
        intent.putExtra("name", name);
        intent.putExtra("cutId", cutId);
        intent.putExtra("manageId", manageId);
        context.startActivity(intent);
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
        private View csl_head_wrap;
        private TextView tv_comment_all;
        private TextView tv_comment_grade;
        private RatingBar rb_comment_progress;
        private TextView tv_good_comment_num;
        private TextView tv_mid_comment_num;
        private TextView tv_bad_comment_num;
        private ProgressBar pb_good_comment;
        private ProgressBar pb_mid_comment;
        private ProgressBar pb_bad_comment;
        private RecyclerView recycler_comment_box;
        private TagFlowLayout tfl_tag;
        //评价列表
        private RecyclerView recycler;
        //footer
        private TextView tv_footer_txt;

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
                csl_head_wrap = itemView.findViewById(R.id.csl_head_wrap);
                tv_comment_all = itemView.findViewById(R.id.tv_comment_all);
                tv_comment_grade = itemView.findViewById(R.id.tv_comment_grade);
                rb_comment_progress = itemView.findViewById(R.id.rb_comment_progress);
                tv_good_comment_num = itemView.findViewById(R.id.tv_good_comment_num);
                tv_mid_comment_num = itemView.findViewById(R.id.tv_mid_comment_num);
                tv_bad_comment_num = itemView.findViewById(R.id.tv_bad_comment_num);
                pb_good_comment = itemView.findViewById(R.id.pb_good_comment);
                pb_mid_comment = itemView.findViewById(R.id.pb_mid_comment);
                pb_bad_comment = itemView.findViewById(R.id.pb_bad_comment);
                recycler_comment_box = itemView.findViewById(R.id.recycler_comment_box);
                tfl_tag = itemView.findViewById(R.id.tfl_tag);
            }
            if (viewType == TYPE_COMMENT_LIST) {
                recycler = itemView.findViewById(R.id.recycler);
            }
            if (viewType == TYPE_FOOTER) {
                tv_footer_txt = itemView.findViewById(R.id.tv_footer_txt);
            }
        }
    }
}