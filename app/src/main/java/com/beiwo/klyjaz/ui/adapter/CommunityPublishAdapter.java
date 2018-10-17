package com.beiwo.klyjaz.ui.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.ui.listeners.OnItemClickListener;
import com.beiwo.klyjaz.ui.listeners.OnSaveEditListener;
import com.beiwo.klyjaz.ui.listeners.TextWatcherListener;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.ClearEditText;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @name loanmarket
 * @class name：com.beihui.market.ui.adapter
 * @class describe
 * @author A
 * @time 2018/9/11 19:00
 */
public class CommunityPublishAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private static final int HEAD = 1;
    private static final int CONTENT = 2;

    private HeadViewHolder headViewHolder;
    private ContentViewHolder contentViewHolder;
    private CommunityPublishHeadAdapter adapter;

    private String mTitle = "";
    private String mContent = "";

    public CommunityPublishAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setHeadData(List<String> mList,String mTopicTitle,String mTopicContent){
        adapter.setData(mList);
//        notifyItemChanged(0);
        this.mTitle = mTopicTitle;
        this.mContent = mTopicContent;
        notifyDataSetChanged();
    }


    public void setData(List<String> mList,String mTitle,String mContent){
        adapter.setDraftData(mList);
        if(!TextUtils.isEmpty(mTitle)){
            this.mTitle = mTitle;
        }
        if(!TextUtils.isEmpty(mContent)) {
            this.mContent = mContent;
        }
        notifyDataSetChanged();
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
            contentViewHolder.etPublishTitle.setText(mTitle);
            contentViewHolder.etPublishContent.setText(mContent);
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

    class HeadViewHolder extends RecyclerView.ViewHolder implements OnItemClickListener {

        @BindView(R.id.recycler_publish)
        RecyclerView publishRecycler;

        HeadViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            adapter = new CommunityPublishHeadAdapter(mContext);
            GridLayoutManager manager = new GridLayoutManager(mContext,4);
            manager.setSpanCount(4);
            publishRecycler.setLayoutManager(manager);
            publishRecycler.setAdapter(adapter);
            adapter.setOnItemClickListener(this);
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onItemClick(int position) {
            listener.onItemClick(position);
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

            etPublishTitle.addTextChangedListener(new TextWatcher(1));
            etPublishContent.addTextChangedListener(new TextWatcher(2));
            etPublishTitle.setFilters(new InputFilter[]{getEmojiFilter,new InputFilter.LengthFilter(30)});
            etPublishContent.setFilters(new InputFilter[]{getEmojiFilter,new InputFilter.LengthFilter(1500)});

        }
    }

    private InputFilter getEmojiFilter = new InputFilter() {
        Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Matcher emojiMatcher = emoji.matcher(source);
            if (emojiMatcher.find()) {
                ToastUtil.toast("不支持输入Emoji表情符号");
                return "";
            }
            return null;
        }
    };


    class TextWatcher extends TextWatcherListener{

        /**
         * flag 1:标题 2:内容
         */
        private int flag = 1;

        public TextWatcher(int flag) {
            this.flag = flag;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(flag == 1) {
                contentViewHolder.tvPublishTitleNum.setText((30 - s.length()) + "/30");
            }else{
//                contentViewHolder.tvPublishTitleNum.setText((30 - s.length()) + "/30");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            if(saveListener == null){
                saveListener = (OnSaveEditListener) mContext;
            }
            if(flag == 1){
                saveListener.onSaveEdit(contentViewHolder.etPublishTitle,flag,s.toString());
            }else{
                saveListener.onSaveEdit(contentViewHolder.etPublishContent,flag,s.toString());
            }

        }
    }

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    private OnSaveEditListener saveListener;
}
