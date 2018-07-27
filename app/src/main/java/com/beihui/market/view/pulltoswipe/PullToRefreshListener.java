package com.beihui.market.view.pulltoswipe;

import android.os.Handler;

/**
 * *      ┏┓　　　┏┓
 * *    ┏┛┻━━━┛┻┓
 * *    ┃　　　　　　　┃
 * *    ┃　　　━　　　┃
 * *    ┃　┳┛　┗┳　┃
 * *    ┃　　　　　　　┃
 * *    ┃　　　┻　　　┃
 * *    ┃　　　　　　　┃
 * *    ┗━┓　　　┏━┛
 * *       ┃　　　┃   神兽保佑
 * *       ┃　　　┃   代码无BUG！
 * *       ┃　　　┗━━━┓
 * *       ┃　　　　　　　┣┓
 * *       ┃　　　　　　　┏┛
 * *       ┗┓┓┏━┳┓┏┛━━━━━┛
 * *         ┃┫┫　┃┫┫
 * *         ┗┻┛　┗┻┛
 * * @author xhb
 */
public class PullToRefreshListener implements PullToRefreshScrollLayout.OnRefreshListener {

    public static int REFRESH_RESULT = 0;

    // 刷新成功
    public static final int SUCCEED = 0;
    // 刷新失败
    public static final int FAIL = 1;
    // 已经加载全部
    public static final int LOAD_SUCCESS = 2;
    // 已经加载全部
    public static final int LOAD_ALL = 3;

    public Handler mHandler = new Handler();

    @Override
    public void onRefresh(final PullToRefreshScrollLayout pullToRefreshScrollLayout) {

        // 下拉刷新操作
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (REFRESH_RESULT) {
                    case SUCCEED:
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshScrollLayout.refreshFinish(PullToRefreshScrollLayout.SUCCEED);
                        break;
                    case FAIL:
                        // 千万别忘了告诉控件刷新完毕了哦！
                        pullToRefreshScrollLayout.refreshFinish(PullToRefreshScrollLayout.FAIL);
                        break;
                }
            }
        }, 0);
    }

    /**
     * 还有一个不需要加载的情况
     */
    @Override
    public void onLoadMore(final PullToRefreshScrollLayout pullToRefreshScrollLayout) {
        // 加载操作
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (REFRESH_RESULT) {
                    case SUCCEED:
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshScrollLayout.loadMoreFinish(PullToRefreshScrollLayout.SUCCEED);
                        break;
                    case FAIL:
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshScrollLayout.loadMoreFinish(PullToRefreshScrollLayout.FAIL);
                        break;
                    case LOAD_SUCCESS:
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshScrollLayout.loadMoreFinish(PullToRefreshScrollLayout.LOAD_SUCCESS);
                        break;
                    case LOAD_ALL:
                        // 千万别忘了告诉控件加载完毕了哦！
                        pullToRefreshScrollLayout.loadMoreFinish(PullToRefreshScrollLayout.LOAD_ALL);
                        break;
                }
            }
        }, 0);
    }
}