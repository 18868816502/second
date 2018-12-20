package com.beiwo.qnejqaz.social.classhelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.social.adapter.ForumPublishAdapter;
import com.beiwo.qnejqaz.util.ImageUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.classhelper
 * @descripe
 * @time 2018/11/13 15:46
 */
public class ForumPublishHelper implements BaseQuickAdapter.OnItemChildClickListener {

    private Context mContext;
    private View.OnClickListener listener;

    @BindView(R.id.recycler)
    RecyclerView recyclerView;

    private ForumPublishAdapter mAdapter;
    private List<String> datas;
    private List<String> draftsUrls;

    public ForumPublishHelper(Context mContext) {
        this.mContext = mContext;
        datas = new ArrayList<>();
        draftsUrls = new ArrayList<>();
    }

    /**
     * 初始化header View（外层头部）
     *
     * @return view
     */
    public View initHead(View.OnClickListener listener) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_forum_publish_head, recyclerView, false);
        ButterKnife.bind(this, view);
        this.listener = listener;
        bindView();
        return view;
    }

    /**
     * 初始化footer View（内层底部，添加图片）
     *
     * @return view
     */
    private View initFoot() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_community_publish_head_foot, recyclerView, false);
        view.setOnClickListener(listener);
        return view;
    }

    private void bindView() {
        mAdapter = new ForumPublishAdapter();
        GridLayoutManager manager = new GridLayoutManager(mContext, 4);
        manager.setSpanCount(4);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.addFooterView(initFoot());
        //设置尾部不占据一整行
        mAdapter.setFooterViewAsFlow(true);
        mAdapter.setOnItemChildClickListener(this);
    }

    public void setDatas(List<String> list) {
        this.datas.addAll(list);
        mAdapter.setNewData(datas);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取选取的照片集合
     *
     * @return 图片地址集合
     */
    public List<String> getList() {
        return datas;
    }

    /**
     * 获取选择的图片数量
     *
     * @return 图片数量
     */
    public int getListSize() {
        return datas.size();
    }

    /**
     * 获取转换后的bitmap集合
     *
     * @return 本地选取bitmap集合
     */
    public List<Bitmap> getBitmapList() {
        clearNetPicture();
        List<Bitmap> bitmaps = new ArrayList<>();
        for (int i = 0; i < datas.size(); i++) {
            Bitmap bitmap = ImageUtils.getFixedBitmap(datas.get(i), 512);
            bitmaps.add(bitmap);
        }
        return bitmaps;
    }

    /**
     * 获取选择本地的图片数量
     *
     * @return 图片数量
     */
    public int getSize() {
        return datas.size();
    }

    /**
     * 清除草稿箱中的网络图片
     */
    private void clearNetPicture() {
        Iterator<String> it = datas.iterator();
        while (it.hasNext()) {
            String x = it.next();
            if (x.contains("http")) {
                it.remove();
            }
        }
    }

    /**
     * 设置草稿箱数据
     *
     * @param list 草稿箱list
     */
    public void setDraftUrls(List<String> list) {
        this.datas.addAll(list);
        this.draftsUrls.addAll(list);
        mAdapter.setNewData(draftsUrls);
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 获取草稿箱图片urls数据
     */
    public List<String> getDraftUrls() {
        return draftsUrls;
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.iv_delete:
                datas.remove(position);
                mAdapter.setNewData(datas);
                mAdapter.notifyDataSetChanged();

                //清除草稿箱中的key信息
                if (draftsUrls != null && draftsUrls.size() != 0) {
                    if (position < draftsUrls.size()) {
                        draftsUrls.remove(position);
                    }
                }
                break;
            default:
                break;
        }
    }
}