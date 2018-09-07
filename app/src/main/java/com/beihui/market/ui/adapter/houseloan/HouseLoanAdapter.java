package com.beihui.market.ui.adapter.houseloan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.beihui.market.R;
import com.beihui.market.entity.HouseLoanBean;

import java.util.ArrayList;
import java.util.List;

public class HouseLoanAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<HouseLoanBean> mList;
    private int type = 0;//0:年限1:利率

    public HouseLoanAdapter(Context mContext) {
        this.mContext = mContext;
        mList = new ArrayList<>();
    }

    public void setList(List<HouseLoanBean> list){
        this.mList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_house_loan,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tvContent.setText(mList.get(position).getContent());
        boolean isSelect = mList.get(position).isSelect();
        if(isSelect){
            viewHolder.imgSelect.setVisibility(View.VISIBLE);
        }else{
            viewHolder.imgSelect.setVisibility(View.GONE);
        }
        viewHolder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;
        private ImageView imgSelect;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            imgSelect = itemView.findViewById(R.id.img_select);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (int) v.getTag();
                    listener.onItemClick(mList.get(position),position);
                }
            });
        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        void onItemClick(HouseLoanBean bean,int position);
    }
}
