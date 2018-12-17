package com.beiwo.klyjaz.goods.adapter;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.entity.GoodsComment;
import com.beiwo.klyjaz.entity.Labels;
import com.beiwo.klyjaz.goods.TagUtil;
import com.beiwo.klyjaz.goods.fragment.GoodsCommentFragment;
import com.beiwo.klyjaz.view.flowlayout.FlowLayout;
import com.beiwo.klyjaz.view.flowlayout.TagAdapter;
import com.beiwo.klyjaz.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/17
 */
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private static final int TYPE_FLOW_HEAD = R.layout.layout_tag_flow;//0
    private static final int TYPE_TOTAL_COMMENT = R.layout.temlapte_recycler;//1
    private static final int TYPE_COMMENT_EMPTY = R.layout.empty_layout_product;//1

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return TYPE_FLOW_HEAD;
        else if (position == 1) {
            if (comments == null || comments.size() == 0) return TYPE_COMMENT_EMPTY;
            else return TYPE_TOTAL_COMMENT;
        }
        return -1;
    }

    private Activity context;
    private GoodsCommentFragment fragment;
    private List<Labels> labels = null;
    private String tagSelected;
    public List<GoodsComment> comments = null;
    private GoodsCommentAdapter commentAdapter = new GoodsCommentAdapter();
    private Handler handler = new Handler(Looper.getMainLooper());
    private StringBuilder sb = new StringBuilder();
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            if (sb.toString().length() > 0)
                fragment.tagRefresh(sb.toString().substring(0, sb.lastIndexOf(",")));
        }
    };

    public CommentListAdapter(GoodsCommentFragment fragment) {
        this.fragment = fragment;
    }

    public void setHeadFlow(List<Labels> data, String tagSelected) {
        labels = data;
        this.tagSelected = tagSelected;
        notifyItemChanged(0);
    }

    public void setCommentList(List<GoodsComment> data) {
        comments = data;
        notifyItemChanged(1);
    }

    public void appendComments(List<GoodsComment> data) {
        if (comments != null && data != null) {
            comments.addAll(data);
            notifyItemChanged(1);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = (Activity) parent.getContext();
        return new ViewHolder(LayoutInflater.from(context).inflate(viewType, parent, false), viewType);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        if (holder.viewType == TYPE_FLOW_HEAD) {
            final List<String> tags = new ArrayList<>();
            final List<String> keys = new ArrayList<>();
            int tagCountSum = 0;
            for (int i = 0; i < labels.size(); i++) {
                Labels label = labels.get(i);
                String value = TagUtil.getKeyValue(label.getFlag(), true);
                if (value == null) continue;
                int count = label.getCount();
                tagCountSum += count;
                tags.add(String.format(value, count));
                keys.add(label.getFlag());
            }
            if (tags.size() <= 0) return;
            tags.add(0, String.format(context.getString(R.string.all_comment), tagCountSum));//全部标签
            keys.add(0, "all");
            final TagAdapter<String> tagAdapter = new TagAdapter<String>(tags) {
                @Override
                public View getView(FlowLayout parent, int position, String str) {
                    TextView textView = (TextView) LayoutInflater.from(context).inflate(R.layout.layout_tag, holder.tag_flow, false);
                    textView.setText(str);
                    return textView;
                }
            };
            final Set<Integer> pos = new HashSet<>();
            pos.add(keys.contains(tagSelected) ? keys.indexOf(tagSelected) : 0);
            tagAdapter.setSelectedList(keys.contains(tagSelected) ? keys.indexOf(tagSelected) : 0);
            holder.tag_flow.setAdapter(tagAdapter);
            holder.tag_flow.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                @Override
                public boolean onTagClick(View view, int position, FlowLayout parent) {
                    if (position == 0) {
                        pos.clear();
                        pos.add(0);
                    } else {
                        if (pos.contains(0)) pos.remove(0);
                        if (pos.contains(position)) pos.remove(position);
                        else pos.add(position);
                    }
                    sb.delete(0, sb.length());
                    if (pos.size() > 0) {
                        tagAdapter.setSelectedList(pos);
                        for (int p : pos) sb.append(keys.get(p)).append(",");
                    } else {
                        tagAdapter.setSelectedList(0);
                        sb.append("all").append(",");
                    }
                    handler.postDelayed(task, 1000);
                    return true;
                }
            });
        }
        if (holder.viewType == TYPE_TOTAL_COMMENT) {
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
            commentAdapter.setNewData(comments);
        }
        if (holder.viewType == TYPE_COMMENT_EMPTY) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.empty_view.setImageResource(R.mipmap.no_data);
                    holder.tv_content.setText("暂无评价");
                }
            }, 500);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private int viewType;
        //头部流式布局
        private TagFlowLayout tag_flow;
        //评价列表
        private RecyclerView recycler;
        //空布局
        private ImageView empty_view;
        private TextView tv_content;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == TYPE_FLOW_HEAD) {
                tag_flow = itemView.findViewById(R.id.tag_flow);
            }
            if (viewType == TYPE_TOTAL_COMMENT) {
                recycler = itemView.findViewById(R.id.recycler);
            }
            if (viewType == TYPE_COMMENT_EMPTY) {
                empty_view = itemView.findViewById(R.id.empty_view);
                tv_content = itemView.findViewById(R.id.tv_content);
            }
        }
    }
}