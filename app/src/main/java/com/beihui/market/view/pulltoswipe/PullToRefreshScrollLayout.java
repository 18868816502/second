package com.beihui.market.view.pulltoswipe;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.beihui.market.R;
import com.beihui.market.event.InsideViewPagerBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

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
 * *
 * 自定义的布局，用来管理三个子控件，其中一个是下拉头，一个是包含内容的pullableView（可以是实现Pullable接口的的任何View），
 * 还有一个上拉头，更多详解见博客http://blog.csdn.net/zhongkejingwang/article/details/38868463
 */
public class PullToRefreshScrollLayout extends RelativeLayout {

	// 初始状态
	public static final int INIT = 0;
	// 释放刷新
	public static final int RELEASE_TO_REFRESH = 1;
	// 正在刷新
	public static final int REFRESHING = 2;
	// 释放加载
	public static final int RELEASE_TO_LOAD = 3;
	// 正在加载
	public static final int LOADING = 4;
	// 操作完毕
	public static final int DONE = 5;
	public static final int LOAD_SUCCESS = 6;
	// 当前状态
	private int state = INIT;
	// 刷新回调接口
	private OnRefreshListener mListener;
	// 刷新成功
	public static final int SUCCEED = 0;
	// 刷新失败
	public static final int FAIL = 1;
	// 已经加载全部
	public static final int LOAD_ALL = 2;
	// 按下Y坐标，上一个事件点Y坐标
	private float downY, lastY;

	// 下拉的距离。注意：pullDownY和pullUpY不可能同时不为0
	public float pullDownY = 0;
	// 上拉的距离
	private float pullUpY = 0;

	// 释放刷新的距离
	private float refreshDist = 200;
	// 释放加载的距离
	private float loadMoreDist = 200;

	private MyTimer timer;
	// 回滚速度
	public float MOVE_SPEED = 8;
	// 第一次执行布局
	private boolean isLayout = false;
	// 在刷新过程中滑动操作
	private boolean isTouch = false;
	// 手指滑动距离与下拉头的滑动距离比，中间会随正切函数变化
	private float radio = 2;

	// 下拉箭头的转180°动画
	private RotateAnimation rotateAnimation;
	// 均匀旋转动画
	private RotateAnimation refreshingAnimation;

	// 下拉头
	private View refreshView;
	// 下拉的箭头
	private View pullView;
	// 正在刷新的图标
	private View refreshingView;
	// 刷新结果图标
	private View refreshStateImageView;
	// 刷新结果：成功或失败
	private TextView refreshStateTextView;

	// 上拉头
	private View loadMoreView;
	// 上拉的箭头
	private View pullUpView;
	// 正在加载的图标
	private View loadingView;
	// 加载结果图标
	private View loadStateImageView;
	// 加载结果：成功或失败
	private TextView loadStateTextView;

	// 实现了Pulled接口的View
	private View pulledView;
	// 过滤多点触碰
	private int mEvents;
	// 这两个变量用来控制pull的方向，如果不加控制，当情况满足可上拉又可下拉时没法下拉
	private boolean canPullDown = true;
	private boolean canPullUp = true;

	private Context mContext;

	@Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
	public void onMainEvent(InsideViewPagerBus bus) {
		this.isRequestInterceptTouchEvent = bus.isRequestInterceptTouchEvent;
	}

	/**
	 * xhb修改代码 为了首页卡片不冲突 viewPager
	 */
	public boolean isRequestInterceptTouchEvent = false;

	/**
	 * 执行自动回滚的handler
	 */
	Handler updateHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// 回弹速度随下拉距离moveDeltaY增大而增大
			MOVE_SPEED = (float) (8 + 5 * Math.tan(Math.PI / 2
					/ getMeasuredHeight() * (pullDownY + Math.abs(pullUpY))));
			if (!isTouch) {
				// 正在刷新，且没有往上推的话则悬停，显示"正在刷新..."
				if (state == REFRESHING && pullDownY <= refreshDist) {
					pullDownY = refreshDist;
					timer.cancel();
				} else if (state == LOADING && -pullUpY <= loadMoreDist) {
					pullUpY = -loadMoreDist;
					timer.cancel();
				}

			}
			if (pullDownY > 0) {
				pullDownY -= MOVE_SPEED;
			}
			else if (pullUpY < 0) {
				pullUpY += MOVE_SPEED;
			}
			if (pullDownY < 0) {
				// 已完成回弹
				pullDownY = 0;
				pullView.clearAnimation();
				// 隐藏下拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING) {
					changeState(INIT);
				}
				timer.cancel();
				requestLayout();
			}
			if (pullUpY > 0) {
				// 已完成回弹
				pullUpY = 0;
				pullUpView.clearAnimation();
				// 隐藏上拉头时有可能还在刷新，只有当前状态不是正在刷新时才改变状态
				if (state != REFRESHING && state != LOADING) {
					changeState(INIT);
				}
				timer.cancel();
				requestLayout();
			}
			// 刷新布局,会自动调用onLayout
			requestLayout();
			// 没有拖拉或者回弹完成
			if (pullDownY + Math.abs(pullUpY) == 0) {
				timer.cancel();
			}
		}
	};


	/**
	 * 构造方法
     */
	public PullToRefreshScrollLayout(Context context) {
		super(context);
		initView(context);
	}

	/**
	 * 构造方法
	 */
	public PullToRefreshScrollLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}

	/**
	 * 构造方法
	 */
	public PullToRefreshScrollLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView(context);
	}

	/**
	 * 初始化布局
     */
	private void initView(Context context) {
		if (!EventBus.getDefault().isRegistered(this)) {
			EventBus.getDefault().register(this);
		}


		mContext = context;
		timer = new MyTimer(updateHandler);
		rotateAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.reverse_anim);
		refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(
				context, R.anim.rotating);
		// 添加匀速转动动画
		LinearInterpolator lir = new LinearInterpolator();
		rotateAnimation.setInterpolator(lir);
		refreshingAnimation.setInterpolator(lir);
	}

	public  void hide() {
		timer.schedule(5);
	}

	/**
	 * 完成刷新操作，显示刷新结果。注意：刷新完成后一定要调用这个方法
	 * PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void refreshFinish(int refreshResult) {
		refreshingView.clearAnimation();
		refreshingView.setVisibility(View.GONE);
		switch (refreshResult) {
			case SUCCEED:
				// 刷新成功
				refreshStateImageView.setVisibility(View.INVISIBLE);
				refreshStateTextView.setText("刷新成功");
				refreshStateImageView
						.setBackgroundResource(R.drawable.x_pull_to_refresh_succeed);
				break;
			case FAIL:
			default:
				// 刷新失败
				refreshStateImageView.setVisibility(View.INVISIBLE);
				refreshStateTextView.setText("刷新失败");
				refreshStateImageView
						.setBackgroundResource(R.drawable.x_pull_to_refresh_failed);
				break;
		}
		if (pullDownY > 0) {
			// 刷新结果停留1秒
			new Handler() {
				@Override
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 0);
		} else {
			changeState(DONE);
			hide();
		}
	}

	/**
	 * 加载完毕，显示加载结果。注意：加载完成后一定要调用这个方法
	 * PullToRefreshLayout.SUCCEED代表成功，PullToRefreshLayout.FAIL代表失败
	 */
	public void loadMoreFinish(int refreshResult) {
		loadingView.clearAnimation();
		loadingView.setVisibility(View.GONE);
		switch (refreshResult) {
			case SUCCEED:
				// 加载成功
				loadStateImageView.setVisibility(View.INVISIBLE);
				loadStateTextView.setText("加载成功");
				loadStateImageView.setBackgroundResource(R.drawable.x_pull_to_refresh_load_succeed);
				break;
			case LOAD_ALL:
				loadStateImageView.setVisibility(View.INVISIBLE);
				loadStateTextView.setText("加载更多");
				loadStateImageView.setBackgroundResource(R.drawable.x_pull_to_refresh_load_succeed);
				break;
			case LOAD_SUCCESS:
				loadStateImageView.setVisibility(View.INVISIBLE);
				loadStateTextView.setText("已加载全部");
				loadStateImageView.setBackgroundResource(R.drawable.x_pull_to_refresh_load_succeed);
				break;
			case FAIL:
			default:
				// 加载失败
				loadStateImageView.setVisibility(View.INVISIBLE);
				loadStateTextView.setText("加载失败");
				loadStateImageView.setBackgroundResource(R.drawable.x_pull_to_refresh_load_failed);
				break;
		}
		if (pullUpY < 0) {
			// 刷新结果停留1秒
			new Handler()
			{
				@Override
				public void handleMessage(Message msg)
				{
					changeState(DONE);
					hide();
				}
			}.sendEmptyMessageDelayed(0, 0);
		} else {
			changeState(DONE);
			hide();
		}
	}

	public void changeState(int to) {
		state = to;
		switch (state) {
		case INIT:
			// 下拉布局初始状态
			refreshStateImageView.setVisibility(View.GONE);
			refreshStateTextView.setText("下拉刷新");
			pullView.clearAnimation();
			pullView.setVisibility(View.INVISIBLE);
			// 上拉布局初始状态
			loadStateImageView.setVisibility(View.GONE);
			loadStateTextView.setText("上拉加载更多");
			pullUpView.clearAnimation();
			pullUpView.setVisibility(View.INVISIBLE);
			break;
		case RELEASE_TO_REFRESH:
			// 释放刷新状态
			refreshStateTextView.setText("释放立即刷新");
			//TODO xhb
//			pullView.startAnimation(rotateAnimation);
			break;
		case REFRESHING:
			// 正在刷新状态
			pullView.clearAnimation();
			refreshingView.setVisibility(View.INVISIBLE);
			pullView.setVisibility(View.INVISIBLE);
			refreshingView.startAnimation(refreshingAnimation);
			refreshStateTextView.setText("正在刷新...");
			break;
		case RELEASE_TO_LOAD:
			// 释放加载状态
			loadStateTextView.setText("释放立即加载");
//			pullUpView.startAnimation(rotateAnimation);
			break;
		case LOADING:
			// 正在加载状态
			pullUpView.clearAnimation();
			loadingView.setVisibility(View.INVISIBLE);
			pullUpView.setVisibility(View.INVISIBLE);
//			loadingView.startAnimation(refreshingAnimation);
			loadStateTextView.setText("正在加载...");
			break;
		case DONE:
			// 刷新或加载完毕，啥都不做
			break;
		}
	}

	/**
	 * 不限制上拉或下拉
	 */
	private void releasePull() {
		canPullDown = true;
		canPullUp = true;
	}

	/**
	 * 不限制上拉或下拉
	 */
	public void limitPull() {
		canPullDown = false;
		canPullUp = false;
	}

	/**
	 * 非 Javadoc 由父控件决定是否分发事件，防止事件冲突
	 * @see android.view.ViewGroup#dispatchTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		/**
		 * xhb修改代码 为了首页卡片不冲突 viewPager
		 */
		if (isRequestInterceptTouchEvent) {
			// 事件分发交给父类
			super.dispatchTouchEvent(ev);
			return true;
		}

		switch (ev.getActionMasked()) {
		case MotionEvent.ACTION_DOWN:
			downY = ev.getY();
			lastY = downY;
			timer.cancel();
			mEvents = 0;
			releasePull();
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
		case MotionEvent.ACTION_POINTER_UP:
			// 过滤多点触碰
			mEvents = -1;
			break;
		case MotionEvent.ACTION_MOVE:
			if (mEvents == 0) {
				if (pullDownY > 0
						|| (((Pulled) pulledView).canPullDown()
								&& canPullDown && state != LOADING)) {
					// 可以下拉，正在加载时不能下拉
					// 对实际滑动距离做缩小，造成用力拉的感觉
					pullDownY = pullDownY + (ev.getY() - lastY) / radio;
					if (pullDownY < 0) {
						pullDownY = 0;
						canPullDown = false;
						canPullUp = true;
					}
					if (pullDownY > getMeasuredHeight())
						pullDownY = getMeasuredHeight();
					if (state == REFRESHING) {
						// 正在刷新的时候触摸移动
						isTouch = true;
					}
				} else if (pullUpY < 0
						|| (((Pulled) pulledView).canPullUp() && canPullUp && state != REFRESHING)) {
					// 可以上拉，正在刷新时不能上拉
					pullUpY = pullUpY + (ev.getY() - lastY) / radio;
					if (pullUpY > 0) {
						pullUpY = 0;
						canPullDown = true;
						canPullUp = false;
					}
					if (pullUpY < -getMeasuredHeight()) {
						pullUpY = -getMeasuredHeight();
					}
					if (state == LOADING) {
						// 正在加载的时候触摸移动
						isTouch = true;
					}
				} else
					releasePull();
			} else {
				mEvents = 0;
			}
			lastY = ev.getY();
			// 根据下拉距离改变比例
			radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight()
					* (pullDownY + Math.abs(pullUpY))));
			if (pullDownY > 0 || pullUpY < 0) {
				requestLayout();
			}
			if (pullDownY > 0) {
				if (pullDownY <= refreshDist
						&& (state == RELEASE_TO_REFRESH || state == DONE)) {
					// 如果下拉距离没达到刷新的距离且当前状态是释放刷新，改变状态为下拉刷新
					changeState(INIT);
				}
				if (pullDownY >= refreshDist && state == INIT) {
					// 如果下拉距离达到刷新的距离且当前状态是初始状态刷新，改变状态为释放刷新
					changeState(RELEASE_TO_REFRESH);
				}
			} else if (pullUpY < 0) {
				// 下面是判断上拉加载的，同上，注意pullUpY是负值
				if (-pullUpY <= loadMoreDist && (state == RELEASE_TO_LOAD || state == DONE)) {
					changeState(INIT);
				}
				// 上拉操作
				if (-pullUpY >= loadMoreDist && state == INIT) {
					changeState(RELEASE_TO_LOAD);
				}

			}
			// 因为刷新和加载操作不能同时进行，所以pullDownY和pullUpY不会同时不为0，因此这里用(pullDownY +
			// Math.abs(pullUpY))就可以不对当前状态作区分了
			if ((pullDownY + Math.abs(pullUpY)) > 8) {
				// 防止下拉过程中误触发长按事件和点击事件
				ev.setAction(MotionEvent.ACTION_CANCEL);
			}
			break;
		case MotionEvent.ACTION_UP:
			if (pullDownY > refreshDist || -pullUpY > loadMoreDist) {
			//正在刷新时往下拉（正在加载时往上拉），释放后下拉头（上拉头）不隐藏
				isTouch = false;
			}
			if (state == RELEASE_TO_REFRESH) {
				changeState(REFRESHING);
				// 刷新操作
				if (mListener != null) {
					mListener.onRefresh(this);
				}
			} else if (state == RELEASE_TO_LOAD) {
				changeState(LOADING);
				// 加载操作
				if (mListener != null)
					mListener.onLoadMore(this);
			}
			hide();
		default:
			break;
		}
		// 事件分发交给父类
		super.dispatchTouchEvent(ev);
		return true;
	}

	/**
	 * 自动模拟手指滑动的task
	 */
	private class AutoRefreshAndLoadTask extends AsyncTask<Integer, Float, String> {
		@Override
		protected String doInBackground(Integer... params) {
			while (pullDownY < 4 / 3 * refreshDist) {
				pullDownY += MOVE_SPEED;
				publishProgress(pullDownY);
				try {
					Thread.sleep(params[0]);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			changeState(REFRESHING);
			// 刷新操作
			if (mListener != null) {
				mListener.onRefresh(PullToRefreshScrollLayout.this);
			}
			hide();
		}

		@Override
		protected void onProgressUpdate(Float... values) {
			if (pullDownY > refreshDist) {
				changeState(RELEASE_TO_REFRESH);
			}
			requestLayout();
		}
	}

	/**
	 * 自动刷新
	 */
	public void autoRefresh() {
		AutoRefreshAndLoadTask task = new AutoRefreshAndLoadTask();
		task.execute(20);
	}

	/**
	 * 自动加载
	 */
	public void autoLoad() {
		pullUpY = -loadMoreDist;
		requestLayout();
		changeState(LOADING);
		// 加载操作
		if (mListener != null) {
			mListener.onLoadMore(this);
		}
	}

	private void initView() {
		// 初始化下拉布局
		pullView = refreshView.findViewById(R.id.pull_icon_head_view);
		refreshStateTextView = (TextView) refreshView
				.findViewById(R.id.state_tv_head_view);
		refreshingView = refreshView.findViewById(R.id.refreshing_icon_head_view);
		refreshStateImageView = refreshView.findViewById(R.id.state_iv_head_view);
		// 初始化上拉布局
		pullUpView = loadMoreView.findViewById(R.id.x_pull_up_icon_more_view);
		loadStateTextView = (TextView) loadMoreView
				.findViewById(R.id.x_load_state_tv_more_view);
		loadingView = loadMoreView.findViewById(R.id.loading_icon_more_view);
		loadStateImageView = loadMoreView.findViewById(R.id.x_load_state_iv_more_view);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (!isLayout) {
			// 这里是第一次进来的时候做一些初始化
			refreshView = getChildAt(0);
			pulledView = getChildAt(1);
			loadMoreView = getChildAt(2);
			isLayout = true;
			initView();
			refreshDist = ((ViewGroup) refreshView).getChildAt(0)
					.getMeasuredHeight();
			loadMoreDist = ((ViewGroup) loadMoreView).getChildAt(0)
					.getMeasuredHeight();
		}
		// 改变子控件的布局，这里直接用(pullDownY + pullUpY)作为偏移量，这样就可以不对当前状态作区分
		refreshView.layout(0,
				(int) (pullDownY + pullUpY) - refreshView.getMeasuredHeight(),
				refreshView.getMeasuredWidth(), (int) (pullDownY + pullUpY));
		pulledView.layout(0, (int) (pullDownY + pullUpY),
				pulledView.getMeasuredWidth(), (int) (pullDownY + pullUpY)
						+ pulledView.getMeasuredHeight());
		loadMoreView.layout(0,
				(int) (pullDownY + pullUpY) + pulledView.getMeasuredHeight(),
				loadMoreView.getMeasuredWidth(),
				(int) (pullDownY + pullUpY) + pulledView.getMeasuredHeight()
						+ loadMoreView.getMeasuredHeight());
	}

	class MyTimer {
		private Handler handler;
		private Timer timer;
		private MyTask mTask;

		public MyTimer(Handler handler) {
			this.handler = handler;
			timer = new Timer();
		}

		public void schedule(long period) {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
			mTask = new MyTask(handler);
			timer.schedule(mTask, 0, period);
		}

		public void cancel() {
			if (mTask != null) {
				mTask.cancel();
				mTask = null;
			}
		}

		class MyTask extends TimerTask {
			private Handler handler;

			public MyTask(Handler handler) {
				this.handler = handler;
			}

			@Override
			public void run() {
				handler.obtainMessage().sendToTarget();
			}
		}
	}

	/**
	 * 回调接口
     */
	public void setOnRefreshListener(OnRefreshListener listener) {
		mListener = listener;
	}

	/**
	 * 刷新加载回调接口
	 */
	public interface OnRefreshListener {
		/**
		 * 刷新操作
		 */
		void onRefresh(PullToRefreshScrollLayout pullToRefreshScrollLayout);

		/**
		 * 加载操作
		 */
		void onLoadMore(PullToRefreshScrollLayout pullToRefreshScrollLayout);
	}

}
