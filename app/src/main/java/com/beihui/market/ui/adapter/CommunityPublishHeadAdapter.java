package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.util.ToastUtil;

import butterknife.ButterKnife;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @anthor A
 * @time 2018/9/11 19:25
 */
public class CommunityPublishHeadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int CONTENT = 1;
    private static final int FOOT = 2;

    private ContentViewHolder contentViewHolder;
    private FootViewHolder footViewHolder;

    public CommunityPublishHeadAdapter(Context mContext) {
        this.mContext = mContext;
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
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
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

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ToastUtil.toast("添加图片");
                }
            });
        }
    }
}
