package com.beihui.market.ui.adapter;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beihui.market.R;
import com.beihui.market.api.Api;
import com.beihui.market.api.ResultEntity;
import com.beihui.market.entity.AccountBill;
import com.beihui.market.entity.DebtAbstract;
import com.beihui.market.entity.TabAccountBean;
import com.beihui.market.entity.TabAccountNewBean;
import com.beihui.market.entity.request.XAccountInfo;
import com.beihui.market.helper.DataStatisticsHelper;
import com.beihui.market.helper.UserHelper;
import com.beihui.market.ui.activity.CreditCardDebtDetailActivity;
import com.beihui.market.ui.activity.DebtAnalyzeActivity;
import com.beihui.market.ui.activity.FastDebtDetailActivity;
import com.beihui.market.ui.activity.LoanDebtDetailActivity;
import com.beihui.market.ui.activity.MainActivity;
import com.beihui.market.ui.activity.UserAuthorizationActivity;
import com.beihui.market.ui.dialog.BillEditAmountDialog;
import com.beihui.market.ui.fragment.TabAccountFragment;
import com.beihui.market.ui.presenter.CreditCardDebtDetailPresenter;
import com.beihui.market.ui.presenter.DebtDetailPresenter;
import com.beihui.market.util.CommonUtils;
import com.beihui.market.util.FastClickUtils;
import com.beihui.market.util.RxUtil;
import com.beihui.market.util.viewutils.ToastUtils;
import com.beihui.market.view.CircleImageView;
import com.beihui.market.view.CustomSwipeMenuLayout;
import com.beihui.market.view.GlideCircleTransform;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import io.reactivex.functions.Consumer;

import static com.beihui.market.util.CommonUtils.keep2digitsWithoutZero;

/**
 * @author xhb
 * 账单首页 列表适配器
 */
public class XTabAccountRvAdapter extends RecyclerView.Adapter<XTabAccountRvAdapter.ViewHolder> {

    //数据源
    private List<TabAccountNewBean.TabAccountNewInfoBean> dataSet = new ArrayList<>();

    //数据源
    private List<TabAccountNewBean.TabAccountNewInfoBean> dataSetCopy = new ArrayList<>();

    //剔除已还的数据源
    private List<TabAccountNewBean.TabAccountNewInfoBean> unPayeset = new ArrayList<>();

    public boolean showAll = true;

    private DebtAbstract debtAbstract = null;

    public static final int VIEW_NORMAL = R.layout.xitem_tab_account_info;
    public static final int VIEW_HEADER = R.layout.xlayout_fg_tab_account_head;

    public Activity mActivity;
    public Fragment mFragment;


    public Drawable mRedDot;
    public Drawable mGrayDot;
    public Drawable mUnPayDot;
    public Drawable mToDayDot;
    public Drawable mPayedDot;


    public XTabAccountRvAdapter(Activity mActivity, Fragment fragment) {
        this.mActivity = mActivity;
        mFragment = fragment;

        mRedDot = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_red_circle);
        mGrayDot = mActivity.getResources().getDrawable(R.drawable.xshape_tab_account_black_circle);
        mUnPayDot = mActivity.getResources().getDrawable(R.drawable.overdue_icon);
        mToDayDot = mActivity.getResources().getDrawable(R.drawable.today_icon);
        mPayedDot = mActivity.getResources().getDrawable(R.drawable.finish_icon);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder viewHolder = new ViewHolder(LayoutInflater.from(mActivity).inflate(viewType, parent, false), viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {
       if (holder.viewType == VIEW_HEADER) {
            if (debtAbstract != null) {
                holder.mHeaderAccountNum.setVisibility(View.VISIBLE);
                holder.mHeaderAccountNum.setText("共"+ debtAbstract.last30DayStayStillCount+"笔");
                holder.mHeaderWaitPay.setText(CommonUtils.keep2digitsWithoutZero(debtAbstract.getLast30DayStayStill()));
            } else {
                holder.mHeaderAccountNum.setVisibility(View.GONE);
                holder.mHeaderWaitPay.setText("赶紧先记上一笔");
            }

            //是否隐藏已还
            holder.mHeaderSort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ("全部".equals(holder.mHeaderSortType.getText().toString())) {
                        if (dataSet.size() > 0) {
                            dataSet.clear();
                        }
                        dataSet.addAll(unPayeset);
                        holder.mHeaderSortType.setText("待还");
                        showAll = false;
                    } else {
                        if (dataSet.size() > 0) {
                            dataSet.clear();
                        }
                        dataSet.addAll(dataSetCopy);
                        holder.mHeaderSortType.setText("全部");
                        showAll = true;
                    }
                    notifyDataSetChanged();
                }
            });
       } else {
           final int position = i - 1;
           final TabAccountNewBean.TabAccountNewInfoBean accountBill = dataSet.get(position);
           //示例数据
           if (accountBill.isAnalog) {
               holder.swipeMenuLayout.setSwipeEnable(false);

               if (position == 0) {
                   holder.mAvatar.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.bill_zsyh_icon));

                   holder.mDot.setImageDrawable(mRedDot);

                   holder.mDateName.setText("今天");
                   holder.mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                   holder.mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
                   holder.mAccountTypeTerm.setText("6月");
               } else {
                   holder.mAvatar.setImageDrawable(mActivity.getResources().getDrawable(R.drawable.bill_fql_icon));
                   holder.mDot.setImageDrawable(mGrayDot);
                   holder.mDateName.setText("7天后");
                   holder.mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                   holder.mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));

                   holder.mAccountTypeTerm.setText("1/1");
               }

               //账单名称 网贷账单 信用卡账单
               holder.mAccountTypeName.setText(accountBill.getTitle());
               //当期应还
               holder.mAccountTypeMoney.setText(CommonUtils.keep2digitsWithoutZero(accountBill.getAmount()));


               holder.mCardBg.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       if (UserHelper.getInstance(mActivity).getProfile() == null) {
                           //跳转到登陆页面
                           UserAuthorizationActivity.launch(mActivity, null);
                       } else {
                           Toast.makeText(mActivity, "这是示例卡，请先添加真实账单", Toast.LENGTH_SHORT).show();
                       }
                   }
               });

           } else {
               //非示例数据

               if (accountBill.getStatus() == 5) {
                   holder.swipeMenuLayout.setSwipeEnable(false);
               } else {
                   holder.swipeMenuLayout.setSwipeEnable(true);
               }

               int placeHolder;
               if (accountBill.getType() == 1) {
                   //网贷账单
                   placeHolder = R.drawable.bill_internetbank_icon;
               } else if (accountBill.getType() == 3){
                   //快捷记账
                   placeHolder = R.drawable.mine_mail_icon;
               } else {
                   //信用卡账单
                   placeHolder = R.drawable.mine_bank_default_icon;
               }
               Glide.with(mActivity).load(accountBill.getLogoUrl()).centerCrop().transform(new GlideCircleTransform(mActivity)).placeholder(placeHolder).into(holder.mAvatar);

               //备注
               if (!TextUtils.isEmpty(accountBill.remark)) {
                   holder.mRemark.setText(accountBill.remark);
               }

               //账单名称 网贷账单 信用卡账单
               holder.mAccountTypeName.setText(accountBill.getTitle());
               //当期应还
               StringBuilder builder = new StringBuilder();
               if (accountBill.getType() == 2) {
                   builder.append(accountBill.getMonth() + "月");
               } else {
                   builder.append(accountBill.getTerm() + "/").append(accountBill.getTotalTerm() + "");
               }
               holder.mAccountTypeMoney.setText(CommonUtils.keep2digitsWithoutZero(accountBill.getAmount()));
               holder.mAccountTypeTerm.setText(builder.toString());

               if (accountBill.headerStatus == null || (position > 0 && dataSet.get(position-1).getStatus() == 2)) {
                   holder.mTopRoot.setVisibility(View.GONE);
               } else {
                   holder.mTopRoot.setVisibility(View.VISIBLE);

                   /**
                    * 逾期或者最近三天还款日 显示小红点 背景颜色
                    */
                   if (accountBill.getStatus() == 2) {
                       //已还清
                       holder.mDot.setImageDrawable(mPayedDot);
                       holder.mCountName.setVisibility(View.VISIBLE);
                       holder.mCount.setVisibility(View.VISIBLE);
                       holder.mCountRight.setVisibility(View.VISIBLE);
                       holder.mCountName.setText("共");
                       holder.mCount.setText(CommonUtils.keep2digitsWithoutZero(accountBill.getTotalTerm()));
                       holder.mCount.setTextColor(Color.parseColor("#909298"));

                       holder.mDateName.setText("已还清");
                   } else if (accountBill.getStatus() == 3) {
                       //逾期
                       holder.mDot.setImageDrawable(mUnPayDot);
                       holder.mCountName.setVisibility(View.VISIBLE);
                       holder.mCount.setVisibility(View.VISIBLE);
                       holder.mCountRight.setVisibility(View.VISIBLE);
                       holder.mCountName.setText("逾期");
                       holder.mCount.setText(CommonUtils.keep2digitsWithoutZero(accountBill.getTotalTerm()));
                       holder.mCount.setTextColor(Color.RED);

                       /**
                        * 逾期时间
                        */
                       getDeteName(holder.mDateName, accountBill.getRepayTime(), accountBill.getReturnDay(), accountBill.getStatus(), accountBill.getOutBillDay());
                   } else if (accountBill.getReturnDay() == 0) {
                       holder.mCountName.setVisibility(View.GONE);
                       holder.mCount.setVisibility(View.GONE);
                       holder.mCountRight.setVisibility(View.GONE);
                       holder.mDot.setImageDrawable(mToDayDot);

                       /**
                        * 逾期时间
                        */
                       getDeteName(holder.mDateName, accountBill.getRepayTime(), accountBill.getReturnDay(), accountBill.getStatus(), accountBill.getOutBillDay());
                   } else if (accountBill.getReturnDay() <= 3) {
                       holder.mDot.setImageDrawable(mRedDot);
                       holder.mCountName.setVisibility(View.GONE);
                       holder.mCount.setVisibility(View.GONE);
                       holder.mCountRight.setVisibility(View.GONE);

                       /**
                        * 逾期时间
                        */
                       getDeteName(holder.mDateName, accountBill.getRepayTime(), accountBill.getReturnDay(), accountBill.getStatus(), accountBill.getOutBillDay());
                   } else {
                       holder.mDot.setImageDrawable(mGrayDot);
                       holder.mCountName.setVisibility(View.GONE);
                       holder.mCount.setVisibility(View.GONE);
                       holder.mCountRight.setVisibility(View.GONE);

                       /**
                        * 逾期时间
                        */
                       getDeteName(holder.mDateName, accountBill.getRepayTime(), accountBill.getReturnDay(), accountBill.getStatus(), accountBill.getOutBillDay());
                   }
               }


               /**
                * 如果是网贷 有还部分 和 已还
                * 如果是信用卡 只有已还
                * 账单类型 1-网贷 2-信用卡
                */
               if (accountBill.getType() == 1) {
                   //显示还部分
                   holder.payPart.setVisibility(View.VISIBLE);
               }
               if (accountBill.getType() == 2) {
                   //隐藏还部分
                   holder.payPart.setVisibility(View.GONE);
               }

               holder.payAll.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       showSetAllPayDialog(accountBill);
                   }
               });

               holder.payPart.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       /**
                        * 埋点 	卡片还部分点击
                        */
                       //pv，uv统计
                       DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_BILL_TAB_ACCOUNT_CARD_PART_PAY);


                       BillEditAmountDialog dialog = new BillEditAmountDialog()
                               .attachConfirmListener(new BillEditAmountDialog.EditAmountConfirmListener() {
                                   @Override
                                   public void onEditAmountConfirm(final double amount) {
                                       if (amount > accountBill.getAmount()) {
                                           Toast.makeText(mActivity, "只能还部分", Toast.LENGTH_SHORT).show();
                                       } else {
                                           /**
                                            * 是网贷
                                            */
                                           if (accountBill.getType() == 1) {
                                               Api.getInstance().updateDebtStatus(UserHelper.getInstance(mActivity).getProfile().getId(), accountBill.getBillId(), amount, 2)
                                                       .compose(RxUtil.<ResultEntity>io2main())
                                                       .subscribe(new Consumer<ResultEntity>() {
                                                                      @Override
                                                                      public void accept(ResultEntity result) throws Exception {
                                                                          if (result.isSuccess()) {
                                                                              //Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show();
                                                                              /**
                                                                       * 如果还部分金额与待还金额相同 则需要回到首屏
                                                                       */
                                                                              if (accountBill.getAmount() - amount < 0.01) {
                                                                                  ((TabAccountFragment) mFragment).refreshData();
                                                                              } else {
                                                                                  accountBill.setAmount(accountBill.getAmount() - amount);
                                                                                  notifyItemChanged(position);
                                                                              }

                                                                              //获取头信息
                                                                              ((TabAccountFragment) mFragment).initHeaderData();
                                                                          } else {
                                                                              Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      }
                                                                  },
                                                               new Consumer<Throwable>() {
                                                                   @Override
                                                                   public void accept(Throwable throwable) throws Exception {
                                                                       Log.e("exception_custom", throwable.getMessage());
                                                                   }
                                                               });
                                           }

                                           /**
                                            * 是快捷记账
                                            */
                                           if (accountBill.getType() == 3) {
                                               Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(mActivity).getProfile().getId(), accountBill.getBillId(), accountBill.getRecordId(), 2, amount)
                                                       .compose(RxUtil.<ResultEntity>io2main())
                                                       .subscribe(new Consumer<ResultEntity>() {
                                                                      @Override
                                                                      public void accept(ResultEntity result) throws Exception {
                                                                          if (result.isSuccess()) {
                                                                              //Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show();
                                                                              /**
                                                                               * 如果还部分金额与待还金额相同 则需要回到首屏
                                                                               */
                                                                              if (accountBill.getAmount() - amount < 0.01) {
                                                                                  ((TabAccountFragment) mFragment).refreshData();
                                                                              } else {
                                                                                  accountBill.setAmount(accountBill.getAmount() - amount);
                                                                                  notifyItemChanged(position);
                                                                              }

                                                                              //获取头信息
                                                                              ((TabAccountFragment) mFragment).initHeaderData();
                                                                          } else {
                                                                              Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                                          }
                                                                      }
                                                                  },
                                                               new Consumer<Throwable>() {
                                                                   @Override
                                                                   public void accept(Throwable throwable) throws Exception {
                                                                       Log.e("exception_custom", throwable.getMessage());
                                                                   }
                                                               });
                                           }
                                       }
                                   }
                               });
                       dialog.show(((MainActivity) mActivity).getSupportFragmentManager(), "paypart");
                   }
               });


               /**
                * 账单类型 1-网贷 2-信用卡
                */
               holder.mCardBg.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       /**
                        * 防止重复点击
                        */
                       if (FastClickUtils.isFastClick()) {
                           return;
                       }
                       if (accountBill.getType() == 1) {
                           Intent intent = new Intent(mActivity, LoanDebtDetailActivity.class);
                           intent.putExtra("debt_id", accountBill.getRecordId());
                           intent.putExtra("bill_id", accountBill.getBillId());
                           mActivity.startActivity(intent);
                       } else if (accountBill.getType() == 3) {
                           //快速记账详情
                           Intent intent = new Intent(mActivity, FastDebtDetailActivity.class);
                           intent.putExtra("debt_id", accountBill.getRecordId());
                           intent.putExtra("bill_id", accountBill.getBillId());
                           mActivity.startActivity(intent);
                       } else {
                           Intent intent = new Intent(mActivity, CreditCardDebtDetailActivity.class);
                           intent.putExtra("debt_id", accountBill.getRecordId());
                           intent.putExtra("bill_id", accountBill.getBillId());
                           intent.putExtra("logo", "");
                           intent.putExtra("bank_name", accountBill.getTitle());
                           intent.putExtra("card_num", "");
                           intent.putExtra("by_hand", false);//是否是手动记账
                           mActivity.startActivity(intent);
                       }
                   }
               });
           }
       }
    }

    private void getDeteName(TextView mDateName, String replyTime, int returnDay, int status, int outBillDay) {
        if (status == 1 || status == 2 || status == 3) {
            if (returnDay == 0) {
                mDateName.setText("今天");
                mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            } else if (returnDay < 0) {
                mDateName.setText("逾期" + Math.abs(returnDay) + "天");
                mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            } else if (returnDay <= 30) {
                mDateName.setText(Math.abs(returnDay) + "天后还款");
                mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            } else if (returnDay > 30) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_MONTH, returnDay);
                int month = calendar.get(Calendar.MONTH) + 1;
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                mDateName.setText(month + "月" + day + "日");
                mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
                mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
            }
        }
        if (status == 4) {
            mDateName.setText("已出账");
            mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
        if (status == 5) {
            mDateName.setText(outBillDay+"天后出账");
            mDateName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
            mDateName.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }
    }

    /**
     * 布局类型
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return VIEW_HEADER;
        } else {
            return VIEW_NORMAL;
        }
    }

    public boolean showAll(){
        return showAll;
    }

    /**
     * 列表条数
     */
    @Override
    public int getItemCount() {
        return dataSet.size()+1;
    }

    /**
     * 更新头布局
     */
    public void notifyHeaderData(DebtAbstract debtAbstract){
        this.debtAbstract = debtAbstract;
        notifyItemChanged(0);
    }

    /**
     * 已还账单（已还清做了分页)
     */
    public void notifyPayChanged(List<TabAccountNewBean> list) {
        ListIterator<TabAccountNewBean> iterator = list.listIterator();
        while (iterator.hasNext()) {
            TabAccountNewBean next = iterator.next();
            dataSet.addAll(next.list);
            dataSetCopy.addAll(next.list);
        }
        notifyDataSetChanged();
    }

    /**
     * 未还账单以及第一页已还账单
     */
    public void notifyUnPayChanged(List<TabAccountNewBean> list) {
        if (dataSet.size() > 0) {
            dataSet.clear();
        }

        ArrayList<TabAccountNewBean.TabAccountNewInfoBean> tempList = new ArrayList<>();
        ListIterator<TabAccountNewBean> iterator = list.listIterator();
        while (iterator.hasNext()) {
            TabAccountNewBean next = iterator.next();
            ListIterator<TabAccountNewBean.TabAccountNewInfoBean> infoIterator = next.list.listIterator();
            while (infoIterator.hasNext()) {
                TabAccountNewBean.TabAccountNewInfoBean info = infoIterator.next();
                info.headerTime = next.time;
                info.headerStatus = next.status;
                info.headerReturnDay = next.returnDay;
                info.count = next.total;
                break;
            }
            tempList.addAll(next.list);
            //	状态 1-待还 2-已还 3-逾期
            if (next.getStatus() == 1 || next.status == 3) {
                unPayeset.addAll(next.list);
            }
        }
        if (tempList.size() > 0) {
            dataSet.addAll(0, tempList);
            dataSetCopy.addAll(0, tempList);
            notifyDataSetChanged();
        }
    }


    /**
     * 设为已还
     */
    private void showSetAllPayDialog(final TabAccountNewBean.TabAccountNewInfoBean accountBill) {
        final Dialog dialog = new Dialog(mActivity, 0);
        View dialogView = LayoutInflater.from(mActivity).inflate(R.layout.dialog_debt_detail_set_status, null);
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (v.getId() == R.id.confirm) {
                    //pv，uv统计
                    DataStatisticsHelper.getInstance().onCountUv(DataStatisticsHelper.ID_SET_STATUS_PAID);
                    //1-网贷 2-信用卡
                    if (accountBill.getType() == 1) {
                        Api.getInstance().updateDebtStatus(UserHelper.getInstance(mActivity).getProfile().getId(), accountBill.getBillId(), 2)
                                .compose(RxUtil.<ResultEntity>io2main())
                                .subscribe(new Consumer<ResultEntity>() {
                                               @Override
                                               public void accept(ResultEntity result) throws Exception {
                                                   if (result.isSuccess()) {
                                                       Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show();
                                                       /**
                                                    * 重新回到首屏
                                                    */
                                                       ((TabAccountFragment) mFragment).refreshData();
                                                   } else {
                                                       Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Log.e("exception_custom", throwable.getMessage());
                                            }
                                        });
                    }
                    if (accountBill.getType() == 2) {
                        Api.getInstance().updateCreditCardBillStatus(UserHelper.getInstance(mActivity).getProfile().getId(), accountBill.getRecordId(), accountBill.getBillId(), 2)
                                .compose(RxUtil.<ResultEntity>io2main())
                                .subscribe(new Consumer<ResultEntity>() {
                                               @Override
                                               public void accept(ResultEntity result) throws Exception {
                                                   if (result.isSuccess()) {
                                                       Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show();
                                                       /**
                                                        * 重新回到首屏
                                                        */
                                                       ((TabAccountFragment) mFragment).refreshData();
                                                   } else {
                                                       Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Log.e("exception_custom", throwable.getMessage());
                                            }
                                        });
                    }

                    /**
                     * 快捷账单
                     */
                    if (accountBill.getType() == 3) {
                        Api.getInstance().updateFastDebtBillStatus(UserHelper.getInstance(mActivity).getProfile().getId(),  accountBill.getBillId(), accountBill.getRecordId(), 2, null)
                                .compose(RxUtil.<ResultEntity>io2main())
                                .subscribe(new Consumer<ResultEntity>() {
                                               @Override
                                               public void accept(ResultEntity result) throws Exception {
                                                   if (result.isSuccess()) {
                                                       Toast.makeText(mActivity, "更新成功", Toast.LENGTH_SHORT).show();
                                                       /**
                                                        * 重新回到首屏
                                                        */
                                                       ((TabAccountFragment) mFragment).refreshData();
                                                   } else {
                                                       Toast.makeText(mActivity, result.getMsg(), Toast.LENGTH_SHORT).show();
                                                   }
                                               }
                                           },
                                        new Consumer<Throwable>() {
                                            @Override
                                            public void accept(Throwable throwable) throws Exception {
                                                Log.e("exception_custom", throwable.getMessage());
                                            }
                                        });
                    }
                }
            }
        };
        dialogView.findViewById(R.id.confirm).setOnClickListener(clickListener);
        dialogView.findViewById(R.id.cancel).setOnClickListener(clickListener);
        ((TextView) dialogView.findViewById(R.id.title)).setText("修改分期状态为已还");
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        Window window = dialog.getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setAttributes(lp);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.show();
    }


    /**
     * ViewHolder
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public int viewType;

        //头布局
        public TextView mHeaderAccountNum;
        public TextView mHeaderWaitPay;
        public LinearLayout mHeaderSort;
        public TextView mHeaderSortType;



        public CustomSwipeMenuLayout swipeMenuLayout;
        public RelativeLayout mTopRoot;
        public LinearLayout payPart;
        public LinearLayout payAll;

        public ImageView mAvatar;
        public ImageView mDot;
        public TextView mDateName;
        public TextView mCount;
        public TextView mCountName;
        public TextView mCountRight;
        public TextView mAccountTypeName;
        public TextView mRemark;
        public TextView mAccountTypeMoney;
        public TextView mAccountTypeTerm;

        public LinearLayout mCardBg;

        public ViewHolder(View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            if (viewType == VIEW_HEADER) {
                mHeaderAccountNum = (TextView) itemView.findViewById(R.id.tv_last_one_month_wait_pay_num);
                mHeaderWaitPay = (TextView) itemView.findViewById(R.id.tv_last_one_month_wait_pay);
                mHeaderSort = (LinearLayout) itemView.findViewById(R.id.ll_tab_account_header_sort);
                mHeaderSortType = (TextView) itemView.findViewById(R.id.tv_tab_account_header_sort);

            } else {
                swipeMenuLayout = (CustomSwipeMenuLayout) itemView.findViewById(R.id.swipe_menu_layout);
                mTopRoot = (RelativeLayout) itemView.findViewById(R.id.rl_item_tab_account_normal_top_root);
                payPart = (LinearLayout) itemView.findViewById(R.id.tv_shape_tab_account_no_reply);
                payAll = (LinearLayout) itemView.findViewById(R.id.tv_shape_tab_account_reply);
                mAvatar = (ImageView) itemView.findViewById(R.id.iv_item_tab_account_avatar);
                mDot = (ImageView) itemView.findViewById(R.id.iv_item_tab_account_dot);
                mDateName = (TextView) itemView.findViewById(R.id.tv_item_tab_account_date_name);
                mCountName = (TextView) itemView.findViewById(R.id.tv_item_tab_account_count_name);
                mCount = (TextView) itemView.findViewById(R.id.tv_item_tab_account_count);
                mCountRight = (TextView) itemView.findViewById(R.id.tv_item_tab_account_count_right);
                mAccountTypeName = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_name_type);
                mRemark = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_remark);
                mAccountTypeMoney = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_loan_money);
                mAccountTypeTerm = (TextView) itemView.findViewById(R.id.tv_item_tab_acount_loan_term);

                mCardBg = (LinearLayout) itemView.findViewById(R.id.ll_item_tab_account_card);
            }
        }
    }
}