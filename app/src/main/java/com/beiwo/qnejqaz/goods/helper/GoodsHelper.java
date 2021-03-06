package com.beiwo.qnejqaz.goods.helper;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.goods.TagUtil;
import com.beiwo.qnejqaz.goods.adapter.GoodsEvaluateAdapter;
import com.beiwo.qnejqaz.social.activity.PhotoDetailActivity;
import com.beiwo.qnejqaz.social.utils.InputFilterUtils;
import com.beiwo.qnejqaz.util.ImageUtils;
import com.beiwo.qnejqaz.util.ToastUtil;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.klyjaz.goods.helper
 * @descripe
 * @time 2018/12/11 12:04
 */
public class GoodsHelper implements View.OnClickListener, TextWatcher {
    @BindView(R.id.eva_type_container01)
    View evaTypeContainer01;
    @BindView(R.id.iv_evaluate01)
    ImageView ivEva01;
    @BindView(R.id.tv_evaluate01)
    TextView tvEva01;
    @BindView(R.id.eva_type_container02)
    View evaTypeContainer02;
    @BindView(R.id.iv_evaluate02)
    ImageView ivEva02;
    @BindView(R.id.tv_evaluate02)
    TextView tvEva02;
    @BindView(R.id.eva_type_container03)
    View evaTypeContainer03;
    @BindView(R.id.iv_evaluate03)
    ImageView ivEva03;
    @BindView(R.id.tv_evaluate03)
    TextView tvEva03;
    @BindView(R.id.tag_flow_01)
    TagFlowLayout tagFlow01;

    private Context mContext;
    private View.OnClickListener mListener;
    private TagAdapter mAdapter;
    private String[] mVals = {"放款成功", "审核被拒", "已还款", "账单逾期", "复借"};

    private TagFlowLayout tagFlow02;
    private EditText etInput;
    private TextView tvCount;
    private RecyclerView recyclerView;
    private GoodsEvaluateAdapter photoAdapter;
    private TextView tvEvaluate;

    private TagAdapter m2Adapter;
    private String[] m2Vals = {"放款快", "额度高", "门槛低", "手续方便", "无需抵押", "不上征信", "审批及时", "过审高", "用户体验好", "大平台"};
    private List<String> photos;

    /*评价类型*/
    private int type = -2;
    /*是否借到*/
    private int loanStatus = 0;

    /*产品印象*/
    private StringBuilder flag;

    /*本地图片选择是否有footer*/
    private int hasFoot = 1;

    public GoodsHelper(Context mContext, View.OnClickListener listener) {
        this.mContext = mContext;
        this.mListener = listener;
        photos = new ArrayList<>();
    }

    /**
     * 装载综合评价部分View
     *
     * @param container
     * @return
     */
    public View init01Layout(ViewGroup container) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_comment_2layout, container, false);
        ButterKnife.bind(this, view);
        initFlow01Data();
        evaTypeContainer01.setOnClickListener(this);
        evaTypeContainer02.setOnClickListener(this);
        evaTypeContainer03.setOnClickListener(this);
        return view;
    }

    /*初始化流式布局(是否借到)*/
    private void initFlow01Data() {
        tagFlow01.setAdapter(mAdapter = new TagAdapter<String>(mVals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag_flow1, tagFlow01, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    /**
     * 获取评价类型
     *
     * @return 评价类型
     */
    public int getType() {
        return type;
    }

    /**
     * 获取是否借到状态
     *
     * @return 借到状态
     */
    public int getLoanStatus() {
        for (Integer position : tagFlow01.getSelectedList()) {
            loanStatus = position + 1;
        }
        if (tagFlow01.getSelectedList().size() == 0) {
            loanStatus = 0;
        }
        return loanStatus;
    }


    /*加载产品印象View*/
    public View init02Layout(ViewGroup container) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_comment_3layout, container, false);
        init02View(view);
        initFlow02Data();
        return view;
    }

    private void init02View(View view) {
        flag = new StringBuilder();
        tagFlow02 = view.findViewById(R.id.tag_flow_02);
        recyclerView = view.findViewById(R.id.recycler);
        etInput = view.findViewById(R.id.et_goods_comment);
        tvCount = view.findViewById(R.id.tv_count);
        tvEvaluate = view.findViewById(R.id.tv_evaluate);
        tvEvaluate.setOnClickListener(mListener);
        etInput.addTextChangedListener(this);
        etInput.setFilters(new InputFilter[]{InputFilterUtils.emojiFilter, new InputFilter.LengthFilter(200)});
        bindRecyclerView();
    }

    /*绑定本地图片列表控件*/
    private void bindRecyclerView() {
        photoAdapter = new GoodsEvaluateAdapter();
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        manager.setSpanCount(4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(photoAdapter);
        photoAdapter.notifyDataSetChanged();
        photoAdapter.addFooterView(initFoot());
        //设置尾部不占据一整行
        photoAdapter.setFooterViewAsFlow(true);
        photoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                intent.putStringArrayListExtra("datas", (ArrayList<String>) photos);
                intent.putExtra("position", position);
                mContext.startActivity(intent);
            }
        });
        photoAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_delete:
                        photos.remove(position);
                        photoAdapter.setNewData(photos);
                        if (hasFoot == 0) {
                            photoAdapter.addFooterView(initFoot());
                            hasFoot = 1;
                        }
                        break;
                }
            }
        });
    }

    /*装载图片列表的加号布局*/
    private View initFoot() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_goods_comment_photo_foot, recyclerView, false);
        view.setOnClickListener(mListener);
        return view;
    }

    /*初始化流式布局(产品印象)*/
    private void initFlow02Data() {
        tagFlow02.setAdapter(m2Adapter = new TagAdapter<String>(m2Vals) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv = (TextView) LayoutInflater.from(mContext).inflate(R.layout.item_tag_flow2, tagFlow02, false);
                tv.setText(s);
                return tv;
            }
        });
    }

    /*获取产品印象*/
    public String getFlag() {
        flag.setLength(0);
        List<Integer> index = new ArrayList<>();
        for (Integer position : tagFlow02.getSelectedList()) {
            index.add(position);
        }
        for (int i = 0; i < index.size(); i++) {
            if (i == index.size() - 1) {
                flag.append(TagUtil.getValByKey(m2Vals[i]));
            } else {
                flag.append(TagUtil.getValByKey(m2Vals[i])).append(",");
            }
        }
        return flag.toString();
    }

    /*获取评论内容*/
    public String getContent() {
        return etInput.getText().toString();
    }

    /**
     * 获取转换后的bitmap集合
     *
     * @return 本地选取bitmap集合
     */
    public List<Bitmap> getBitmapList() {
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < photos.size(); i++) {
            Bitmap bitmap = ImageUtils.getFixedBitmap(photos.get(i), 512);
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    /*设置本地图片数据*/
    public void setPhotos(List<String> list) {
        this.photos.addAll(list);
        photoAdapter.setNewData(photos);
        photoAdapter.notifyDataSetChanged();
        if (photos.size() >= 4) {
            photoAdapter.removeAllFooterView();
            hasFoot = 0;
        }
    }

    public int getListSize() {
        return photos.size();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.eva_type_container01:
                clearState();
                ivEva01.setBackgroundResource(R.drawable.icon_goods_pos_selected);
                tvEva01.setTextColor(mContext.getResources().getColor(R.color.black_1));
                type = 1;
                break;
            case R.id.eva_type_container02:
                clearState();
                ivEva02.setBackgroundResource(R.drawable.icon_goods_com_selected);
                tvEva02.setTextColor(mContext.getResources().getColor(R.color.black_1));
                type = 0;
                break;
            case R.id.eva_type_container03:
                clearState();
                ivEva03.setBackgroundResource(R.drawable.icon_goods_neg_selected);
                tvEva03.setTextColor(mContext.getResources().getColor(R.color.black_1));
                type = -1;
                break;
            case R.id.tv_evaluate:
                ToastUtil.toast(getFlag());
                break;
            default:
                break;
        }
    }

    /*清除评价选中状态*/
    private void clearState() {
        ivEva01.setBackgroundResource(R.drawable.icon_goods_pos_unselected);
        ivEva02.setBackgroundResource(R.drawable.icon_goods_com_unselected);
        ivEva03.setBackgroundResource(R.drawable.icon_goods_neg_unselected);
        tvEva01.setTextColor(mContext.getResources().getColor(R.color.black_2));
        tvEva02.setTextColor(mContext.getResources().getColor(R.color.black_2));
        tvEva03.setTextColor(mContext.getResources().getColor(R.color.black_2));
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        tvCount.setText(String.valueOf(200 - s.length()));
        //tvCount.setText(String.valueOf(s.length()));
    }

    @Override
    public void afterTextChanged(Editable s) {
    }
}