package com.beiwo.qnejqaz.ui.activity;

import com.beiwo.qnejqaz.api.NetConstants;
import com.beiwo.qnejqaz.base.BaseH5Activity;

/**
 * Copyright: dondo (C)2018
 * FileName: GuideInviteActivity
 * Author: jiang
 * Create on: 2018/8/20 14:52
 * Description:
 */
public class GuideInviteActivity extends BaseH5Activity {
    @Override
    public String loadUrl() {
        return NetConstants.guideInvite();
    }

    @Override
    public void initTitle() {
        titleTv.setText("如何成功完成奖励任务");
    }
}