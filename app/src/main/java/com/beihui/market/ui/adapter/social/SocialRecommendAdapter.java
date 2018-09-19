package com.beihui.market.ui.adapter.social;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.beihui.market.R;
import com.beihui.market.entity.SocialTopicBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter.social
 * @descripe
 * @time 2018/9/19 10:10
 */
public class SocialRecommendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<SocialTopicBean> datas;
    private Context mContext;
    private static final int ADVANTAGE = 0;
    private static final int OPERATE_TOPIC = 1;
    private static final int USER_TOPIC = 2;

    public SocialRecommendAdapter(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
    }

    public void setDatas(List<SocialTopicBean> mList){
        datas.clear();
        datas.addAll(mList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case ADVANTAGE:
                View advantageView = LayoutInflater.from(mContext).inflate(R.layout.item_social_recommend_advertage,parent,false);
                return new AdvantageViewHolder(advantageView);
            case OPERATE_TOPIC:
                View operateView = LayoutInflater.from(mContext).inflate(R.layout.item_social_recommend_operate_topic,parent,false);
                return new OperateTopicViewHolder(operateView);
            case USER_TOPIC:
                View userTopicView = LayoutInflater.from(mContext).inflate(R.layout.item_social_recommend_user_topic,parent,false);
                return new UserTopicViewHolder(userTopicView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    @Override
    public int getItemViewType(int position) {
        if(datas.size() < 7){
            if(position == 0){
                return ADVANTAGE;
            }else if(position == getItemCount() - 1){
                return USER_TOPIC;
            }else{
                return OPERATE_TOPIC;
            }
        }else{
            if(position == 0){
                return ADVANTAGE;
            }else if(position == 7){
                return USER_TOPIC;
            }else{
                return OPERATE_TOPIC;
            }
        }
    }

    class AdvantageViewHolder extends RecyclerView.ViewHolder {

        public AdvantageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class OperateTopicViewHolder extends RecyclerView.ViewHolder {

        public OperateTopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    class UserTopicViewHolder extends RecyclerView.ViewHolder {

        public UserTopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
