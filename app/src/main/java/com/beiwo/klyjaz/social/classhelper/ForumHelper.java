package com.beiwo.klyjaz.social.classhelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.api.Api;
import com.beiwo.klyjaz.api.ResultEntity;
import com.beiwo.klyjaz.helper.UserHelper;
import com.beiwo.klyjaz.social.activity.PhotoDetailActivity;
import com.beiwo.klyjaz.social.bean.ForumInfoBean;
import com.beiwo.klyjaz.social.bean.PraiseBean;
import com.beiwo.klyjaz.tang.DlgUtil;
import com.beiwo.klyjaz.ui.activity.PersonalCenterActivity;
import com.beiwo.klyjaz.util.RxUtil;
import com.beiwo.klyjaz.util.ToastUtil;
import com.beiwo.klyjaz.view.CircleImageView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.functions.Consumer;

/**
 * @author chenguoguo
 * @name loanmarket_social
 * @class name：com.beiwo.klyjaz.social.classhelper
 * @descripe
 * @time 2018/10/29 17:18
 */
public class ForumHelper {

    private Context mContext;

    /**
     * Header
     */
    @BindView(R.id.tv_article_title)
    TextView tvArticleTitle;
    @BindView(R.id.iv_author_avatar)
    CircleImageView ivAuthorAvatar;
    @BindView(R.id.tv_author_name)
    TextView tvAuthorName;
    @BindView(R.id.tv_author_time)
    TextView tvAuthorTime;
    @BindView(R.id.tv_article_attention)
    TextView tvAttention;
    @BindView(R.id.banner)
    BGABanner bgaBanner;
    @BindView(R.id.tv_article_content)
    TextView tvArticleContent;
    @BindView(R.id.tv_article_praise)
    TextView tvPraise;
    @BindView(R.id.tv_comment_num)
    TextView tvCommentNum;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.iv_comment)
    ImageView ivComment;

    /**
     * Empty layout
     */
    @BindView(R.id.empty_container)
    View emptyContainer;

    /**
     * footer
     */
    private View footView;
    private TextView tvCommentMore;

    private ForumInfoBean.ForumBean forumBean;


    public ForumHelper(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 初始化header View
     * @return view
     * @param recyclerView 父布局
     */
    public View initHead(RecyclerView recyclerView) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_head, recyclerView,false);
        ButterKnife.bind(this,view);
        return view;
    }

    /**
     * 初始化footer View
     * @return view
     * @param recyclerView 父布局
     */
    public View initFoot(RecyclerView recyclerView, View.OnClickListener listener) {
        footView = LayoutInflater.from(mContext).inflate(R.layout.item_article_detail_foot, recyclerView,false);
        tvCommentMore = footView.findViewById(R.id.tv_comment_num_title);
        footView.setOnClickListener(listener);
        return footView;
    }

    /**
     * 更新动态header数据
     * @param forumBean 动态详情数据
     */
    public void updateHeadDatas(final ForumInfoBean.ForumBean forumBean) {
        if (forumBean == null) {
            return;
        }
        this.forumBean = forumBean;
        Glide.with(mContext).load(forumBean.getUserHeadUrl()).placeholder(R.drawable.mine_icon_head).into(ivAuthorAvatar);
        tvArticleTitle.setText(forumBean.getTitle());
        tvAuthorName.setText(TextUtils.isEmpty(forumBean.getUserName()) ? "未知" : forumBean.getUserName());
        tvAuthorTime.setText(forumBean.getCreateText());
        tvArticleContent.setText(forumBean.getContent());
        tvCommentNum.setText(String.valueOf("评论 " + forumBean.getCommentCount()));
        bindBannerData(forumBean.getPicUrl());
        bindForumPraise();
        //empty数据显示
        if(forumBean.getCommentCount() < 1){
            emptyContainer.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 绑定动态点赞
     */
    private void bindForumPraise() {
        if (forumBean.getIsPraise() == 0) {
            Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_social_personal_praise_unselected);
            dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
            tvPraise.setCompoundDrawables(dwLeft, null, null, null);
            tvPraise.setText("赞");
            tvPraise.setTextColor(mContext.getResources().getColor(R.color.black_2));
        } else {
            Drawable dwLeft = mContext.getResources().getDrawable(R.drawable.icon_social_personal_praise_selected);
            dwLeft.setBounds(0, 0, dwLeft.getMinimumWidth(), dwLeft.getMinimumHeight());
            tvPraise.setCompoundDrawables(dwLeft, null, null, null);
            tvPraise.setText(String.valueOf(forumBean.getPraiseCount()));
            tvPraise.setTextColor(mContext.getResources().getColor(R.color.c_ff5240));
        }
    }

    /**
     * 绑定banner数据
     * @param picLists banner图片数据
     */
    private void bindBannerData(final List<String> picLists){
        if (picLists != null && picLists.size() != 0) {
            bgaBanner.setVisibility(View.VISIBLE);
            bgaBanner.setData(picLists, null);
            bgaBanner.setAdapter(new BGABanner.Adapter<ImageView, String>() {
                @Override
                public void fillBannerItem(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                    Glide.with(mContext).load(model).into(itemView);
                }
            });
            bgaBanner.setDelegate(new BGABanner.Delegate<ImageView, String>() {
                @Override
                public void onBannerItemClick(BGABanner banner, ImageView itemView, @Nullable String model, int position) {
                    Intent intent = new Intent(mContext, PhotoDetailActivity.class);
                    intent.putStringArrayListExtra("datas", (ArrayList<String>) picLists);
                    intent.putExtra("position", position);
                    mContext.startActivity(intent);

                }
            });
            if (picLists.size() == 1) {
                bgaBanner.setAutoPlayAble(false);
            } else {
                bgaBanner.setAutoPlayAble(true);
            }
        }else{
            bgaBanner.setVisibility(View.GONE);
        }
    }

    /**
     * 更新动态footer数据
     * @param forum 动态详情数据
     */
    public void updateFootDatas(ForumInfoBean.ForumBean forum){
        tvCommentMore.setText(String.format(mContext.getString(R.string.article_comment_total_num),forum.getCommentCount()));
        if(forum.getCommentCount() < 4){
            footView.setVisibility(View.GONE);
        }
    }


    @OnClick(R.id.tv_article_praise)
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tv_article_praise:
                if(!UserHelper.getInstance(mContext).isLogin()){
                    DlgUtil.loginDlg((Activity) mContext, null);
                }else{
                    if(forumBean != null){
                        if(forumBean.getIsPraise() == 0){
                            clickPraise(forumBean.getForumId());
                        }else{
                            cancelPraise(forumBean.getForumId());
                        }
                    }
                }
            case R.id.iv_author_avatar:
                Intent intent = new Intent(mContext, PersonalCenterActivity.class);
                intent.putExtra("userId", forumBean.getUserId());
                mContext.startActivity(intent);
                break;
            default:
                break;
        }
    }

    @SuppressLint("CheckResult")
    private void clickPraise(String forumId){
        Api.getInstance().fetchClickPraise(0,forumId, UserHelper.getInstance(mContext).getProfile().getId())
                .compose(RxUtil.<ResultEntity<PraiseBean>>io2main())
                .subscribe(new Consumer<ResultEntity<PraiseBean>>() {
                               @Override
                               public void accept(ResultEntity<PraiseBean> result){
                                   if (result.isSuccess()) {
                                       forumBean.setIsPraise(1);
                                       forumBean.setPraiseCount(forumBean.getPraiseCount()+1);
                                       bindForumPraise();
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){

                            }
                        });
    }

    @SuppressLint("CheckResult")
    private void cancelPraise(String forumId) {
        Api.getInstance().fetchCancelPraise(0,forumId,UserHelper.getInstance(mContext).getProfile().getId())
                .compose(RxUtil.<ResultEntity>io2main())
                .subscribe(new Consumer<ResultEntity>() {
                               @Override
                               public void accept(ResultEntity result){
                                   if (result.isSuccess()) {
                                       forumBean.setIsPraise(0);
                                       forumBean.setPraiseCount(forumBean.getPraiseCount() - 1);
                                       bindForumPraise();
                                   } else {
                                       ToastUtil.toast(result.getMsg());
                                   }
                               }
                           },
                        new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable){

                            }
                        });
    }
}
