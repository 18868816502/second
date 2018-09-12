package com.beihui.market.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.beihui.market.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @anthor A
 * @time 2018/9/11 19:00
 */
public class CommunityPublishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int HEAD = 1;
    private static final int CONTENT = 2;

    private HeadViewHolder headViewHolder;
    private ContentViewHolder contentViewHolder;

    public CommunityPublishAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case HEAD:
                View headView = LayoutInflater.from(mContext).inflate(R.layout.item_community_publish_head, parent, false);
                return new HeadViewHolder(headView);
            case CONTENT:
                View contentView = LayoutInflater.from(mContext).inflate(R.layout.item_community_publish_content, parent, false);
                return new ContentViewHolder(contentView);
                default:
                    break;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(position == 0){
            headViewHolder = (HeadViewHolder) holder;
        }else{
            contentViewHolder = (ContentViewHolder) holder;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEAD;
        }else{
            return CONTENT;
        }
    }

    class HeadViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.recycler_publish)
        RecyclerView publishRecycler;

        public HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            CommunityPublishHeadAdapter adapter = new CommunityPublishHeadAdapter(mContext);
            GridLayoutManager manager = new GridLayoutManager(mContext,4);
            manager.setSpanCount(4);
            publishRecycler.setLayoutManager(manager);
            publishRecycler.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    class ContentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.et_publish_title)
        EditText etPublishTitle;
        @BindView(R.id.tv_publish_title_num)
        TextView tvPublishTitleNum;
        @BindView(R.id.et_publish_content)
        EditText etPublishContent;

        public ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            etPublishTitle.addTextChangedListener(new TextWatcher());
            etPublishTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(30)});
            etPublishContent.setFilters(new InputFilter[]{new InputFilter.LengthFilter(150)});
        }
    }

    class TextWatcher implements android.text.TextWatcher{



        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            contentViewHolder.tvPublishTitleNum.setText((30-charSequence.length()) + "/30");
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
