package com.beiwo.qnejqaz.ui.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beiwo.qnejqaz.BuildConfig;
import com.beiwo.qnejqaz.R;
import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.entity.CreditCardBean;
import com.beiwo.qnejqaz.helper.UserHelper;
import com.beiwo.qnejqaz.ui.activity.MainActivity;
import com.beiwo.qnejqaz.util.FastClickUtils;
import com.beiwo.qnejqaz.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.moxie.client.exception.ExceptionType;
import com.moxie.client.exception.MoxieException;
import com.moxie.client.manager.MoxieCallBack;
import com.moxie.client.manager.MoxieCallBackData;
import com.moxie.client.manager.MoxieContext;
import com.moxie.client.manager.MoxieSDK;
import com.moxie.client.model.MxParam;
import com.moxie.client.model.TitleParams;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class CreditBillAdapter extends RecyclerView.Adapter<CreditBillAdapter.ViewHolder> {

    public List<CreditCardBean> list = new ArrayList<>();

    public Activity mActivity;

    public static final int VIEW_NORMAL = R.layout.x_item_account_flow_credit_crad;

    public CreditBillAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(VIEW_NORMAL, parent, false));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final CreditCardBean bean = list.get(position);
        Glide.with(mActivity).load(bean.logo).centerCrop().transform(new GlideCircleTransform(mActivity)).placeholder(R.drawable.mine_bank_default_icon).into(holder.mAvatar);
        holder.mName.setText(bean.bankName);

        holder.mRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*防止重复点击*/
                if (FastClickUtils.isFastClick()) {
                    return;
                }
                loginMoxie(bean.bankCode);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<CreditCardBean> data) {
        if (list.size() > 0) {
            list.clear();
        }
        list.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * TODO
     *
     * @version 3.1.0
     * @author xhb
     */
    private void loginMoxie(String bankTag) {
        MxParam mxParam = new MxParam();
        mxParam.setUserId(UserHelper.getInstance(mActivity).getProfile().getId());
        mxParam.setApiKey(BuildConfig.MOXIE_APP_KEY);
        mxParam.setQuitDisable(false);

        mxParam.setQuitLoginDone(MxParam.PARAM_COMMON_YES);
        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);
        mxParam.setFunction(MxParam.PARAM_FUNCTION_ONLINEBANK);
        mxParam.setItemType(MxParam.PARAM_ITEM_TYPE_CREDITCARD);  //信用卡

        //设置协议地址
        mxParam.setAgreementUrl(NetConstants.H5_USER_MOXIE_PROTOCOL);

        //自定义Title, 还有更多方法请用IDE查看
        TitleParams titleParams = new TitleParams.Builder()
                //设置返回键的icon，不设置此方法会默认使用魔蝎的icon
                .leftNormalImgResId(R.mipmap.btn_back_normal_black)
                //用于设置selector，表示按下的效果，不设置默认使leftNormalImgResId()设置的图片
                .leftPressedImgResId(R.mipmap.btn_back_normal_black)
                //标题字体颜色
                .titleColor(mActivity.getResources().getColor(R.color.black_1))
                //title背景色
                .backgroundColor(mActivity.getResources().getColor(R.color.white))
                //设置右边icon
                .rightNormalImgResId(R.drawable.moxie_client_banner_refresh_black)
                //是否支持沉浸式
                .immersedEnable(true)
                .build();
        mxParam.setTitleParams(titleParams);

        mxParam.setItemCode(bankTag);
        MoxieSDK.getInstance().start(mActivity, mxParam, new MoxieCallBack() {
            @Override
            public boolean callback(MoxieContext moxieContext, MoxieCallBackData moxieCallBackData) {
                if (moxieCallBackData != null) {
                    switch (moxieCallBackData.getCode()) {
                        /**
                         * 账单导入中
                         *
                         * 如果用户正在导入魔蝎SDK会出现这个情况，如需获取最终状态请轮询贵方后台接口
                         * 魔蝎后台会向贵方后台推送Task通知和Bill通知
                         * Task通知：登录成功/登录失败
                         * Bill通知：账单通知
                         */
                        case MxParam.ResultCode.IMPORTING:
                            //状态为IMPORTING, 且loginDone为true，说明这个时候已经在采集中，已经登录成功
                            //Log.d("customMoxie", "任务已经登录成功，正在采集中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");

                            //状态为IMPORTING, 且loginDone为false，说明这个时候正在登录中
                            //Log.d("customMoxie", "任务正在登录中，SDK退出后不会再回调任务状态，任务最终状态会从服务端回调，建议轮询APP服务端接口查询任务/业务最新状态");
                            break;
                        /**
                         * 任务还未开始
                         *
                         * 假如有弹框需求，可以参考 {@link BigdataFragment#showDialog(MoxieContext)}
                         *
                         * example:
                         *  case MxParam.ResultCode.IMPORT_UNSTART:
                         *      showDialog(moxieContext);
                         *      return true;
                         * */
                        case MxParam.ResultCode.IMPORT_UNSTART:
                            moxieContext.finish();
                            return true;
                        case MxParam.ResultCode.THIRD_PARTY_SERVER_ERROR:
                            break;
                        case MxParam.ResultCode.MOXIE_SERVER_ERROR:
                            break;
                        case MxParam.ResultCode.USER_INPUT_ERROR:
                            break;
                        case MxParam.ResultCode.IMPORT_FAIL:
                            break;
                        case MxParam.ResultCode.IMPORT_SUCCESS:
                            //根据taskType进行对应的处理
                            switch (moxieCallBackData.getTaskType()) {
                                case MxParam.PARAM_FUNCTION_EMAIL:
                                    break;
                                case MxParam.PARAM_FUNCTION_ONLINEBANK:
                                    break;
                                default:
                            }
                            moxieContext.finish();
                            return true;
                    }
                }
                Intent intent = new Intent(mActivity, MainActivity.class);
                intent.putExtra("account", true);
                intent.putExtra("moxieMsg", "3秒后刷新页面信用卡就会显示啦");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                mActivity.startActivity(intent);
                EventBus.getDefault().post("1");
                return false;
            }

            @Override
            public void onError(MoxieContext moxieContext, MoxieException moxieException) {
                super.onError(moxieContext, moxieException);
                if (moxieException.getExceptionType() == ExceptionType.SDK_HAS_STARTED) {
                    Toast.makeText(mActivity, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                } else if (moxieException.getExceptionType() == ExceptionType.SDK_LACK_PARAMETERS) {
                    Toast.makeText(mActivity, moxieException.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout mRoot;
        public ImageView mAvatar;
        public TextView mName;

        public ViewHolder(View itemView) {
            super(itemView);
            mRoot = itemView.findViewById(R.id.iv_item_credit_card_root);
            mAvatar = itemView.findViewById(R.id.iv_item_credit_card_avatar);
            mName = itemView.findViewById(R.id.iv_item_credit_card_name);
        }
    }
}