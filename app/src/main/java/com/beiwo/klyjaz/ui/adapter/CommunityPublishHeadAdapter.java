package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.ui.activity.CommunityPublishActivity;
import com.beiwo.klyjaz.ui.listeners.OnItemClickListener;
import com.bumptech.glide.Glide;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @author A
 * @time 2018/9/11 19:25
 */
public class CommunityPublishHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int CONTENT = 1;
    private static final int FOOT = 2;
    private List<String> mList;

    private ContentViewHolder contentViewHolder;
    private FootViewHolder footViewHolder;



    public CommunityPublishHeadAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setData(List<String> mList){
        this.mList.clear();
        this.mList.addAll(mList);
        if(mList.size() >= 9){
            footViewHolder.itemView.setVisibility(View.GONE);
        }else{
            footViewHolder.itemView.setVisibility(View.VISIBLE);
        }
        notifyDataSetChanged();
    }

    /**
     * 设置草稿箱数据
     */
    public void setDraftData(List<String> mList){
        this.mList.clear();
        this.mList.addAll(mList);
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case CONTENT:
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_community_publish_head_photo,parent,false);
                return new ContentViewHolder(contentView);
            case FOOT:
                View footView = LayoutInflater.from(mContext).inflate(R.layout.item_community_publish_head_foot,parent,false);
                return new FootViewHolder(footView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position < getItemCount()-1){
            contentViewHolder = (ContentViewHolder) holder;
            Glide.with(mContext).load(mList.get(position)).into(contentViewHolder.ivPhoto);
            contentViewHolder.ivDelete.setTag(position);
        }else{
            footViewHolder = (FootViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == getItemCount() - 1){
            return FOOT;
        }else{
            return CONTENT;
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_photo)
        ImageView ivPhoto;
        @BindView(R.id.iv_delete)
        ImageView ivDelete;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick((Integer) v.getTag());
                }
            });
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    choosePhoto();
                }
            });
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    /**
     * 选择照片
     */
    private void choosePhoto() {
        Matisse.from((CommunityPublishActivity) mContext)
                .choose(MimeType.ofAll(), false)
                .countable(true)
                .capture(true)
                .captureStrategy(new CaptureStrategy(true, "com.beihui.market.fileprovider","kaola"))
//                .maxSelectable(9)
                .maxSelectable(9 - mList.size())
                .gridExpectedSize(mContext.getResources().getDimensionPixelSize(R.dimen.dp120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                .thumbnailScale(0.85f)
                .imageEngine(new GlideEngine())
                .originalEnable(true)
                .maxOriginalSize(10)
                .autoHideToolbarOnSingleTap(true)
                .forResult(CommunityPublishActivity.REQUEST_CODE_CHOOSE);
    }
}
