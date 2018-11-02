package com.beiwo.klyjaz.social.activity;

import android.media.Image;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.beiwo.klyjaz.R;
import com.beiwo.klyjaz.social.dialog.CommentDialog;
import com.beiwo.klyjaz.util.ToastUtil;
import com.gyf.barlibrary.ImmersionBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForumCommentActivity extends AppCompatActivity {

    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.tv_comment_title)
    TextView tvTitle;
    @BindView(R.id.tv_comment)
    TextView tvComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forum_comment);
        ImmersionBar.with(this).init();
        if (savedInstanceState != null) {
            FragmentManager manager = getSupportFragmentManager();
            manager.popBackStackImmediate(null, 1);
        }
        ButterKnife.bind(this);
        setBackgroundWindow();


    }

    @OnClick({R.id.iv_close,R.id.tv_comment})
    public void onViewClick(View v){
        switch (v.getId()){
            case R.id.iv_close:
                finish();
                break;
            case R.id.tv_comment:
                new CommentDialog("优质评论将会被优先展示", new CommentDialog.SendListener() {
                    @Override
                    public void sendComment(String inputText) {
                        ToastUtil.toast(inputText);
                    }
                }).show(getSupportFragmentManager(), "comment");
                break;
            default:
                break;
        }
    }

    /**
     * 设置弹窗背景宽度
     */
    private void setBackgroundWindow() {
        Window window = getWindow();
        WindowManager.LayoutParams wl = window.getAttributes();
        wl.x = 0;
        wl.y = getWindowManager().getDefaultDisplay().getHeight();
        // 以下这两句是为了保证按钮可以水平满屏
        wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
        wl.height = ViewGroup.LayoutParams.MATCH_PARENT;
    }
}
