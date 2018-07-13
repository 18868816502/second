package com.beihui.market.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

/**
 * @author xhb
 * 针对EditText关于软键盘的帮助类
 */
public class KeyBoardHelper {

	private Activity activity;

	private OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener;
	private int screenHeight;
	private int blankHeight = 0;

	/**
	 * 构造方法
     */
	public KeyBoardHelper(Activity activity) {
		this.activity = activity;
		//屏幕高度
		screenHeight = activity.getResources().getDisplayMetrics().heightPixels;
//		activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
//		if (activity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
//			activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		}
	}

	public void onCreate() {
		View content = activity.findViewById(android.R.id.content);
		content.getViewTreeObserver().addOnGlobalLayoutListener(onGlobalLayoutListener);
	}


	public void onDestroy() {
		View content = activity.findViewById(android.R.id.content);
		content.getViewTreeObserver().removeGlobalOnLayoutListener(onGlobalLayoutListener);
	}

	private OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

		@Override
		public void onGlobalLayout() {
			Rect rect = new Rect();
			activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
			int newBlankHeight = screenHeight - rect.bottom;
			if (newBlankHeight != blankHeight) {
				if (newBlankHeight > blankHeight) {
					/**
					 * keyboard pop (软键盘弹出) black
					 */
					if (onKeyBoardStatusChangeListener != null) {
						onKeyBoardStatusChangeListener.OnKeyBoardPop(newBlankHeight);
					}
				} else { // newBlankHeight < blankHeight
					/**
					 * keyboard close (软键盘关闭)
 					 */
					if (onKeyBoardStatusChangeListener != null) {
						onKeyBoardStatusChangeListener.OnKeyBoardClose(blankHeight);
					}
				}
			}
			blankHeight = newBlankHeight;
		}
	};

	public void setOnKeyBoardStatusChangeListener(
			OnKeyBoardStatusChangeListener onKeyBoardStatusChangeListener) {
		this.onKeyBoardStatusChangeListener = onKeyBoardStatusChangeListener;
	}

	/**
	 * 显示软键盘
     */
	public void showKeyBoard(final View editText) {
		editText.requestFocus();
		InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.showSoftInput(editText, 0);
	}

	/**
	 * 隐藏软键盘
	 */
	public void hideKeyBoard(View editText) {
		InputMethodManager manager = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
		manager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	/**
	 * 软键盘状态的监听毁掉
	 */
	public interface OnKeyBoardStatusChangeListener {

		void OnKeyBoardPop(int keyBoardHeight);

		void OnKeyBoardClose(int oldKeyBoardHeight);
	}

}
