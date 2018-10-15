package com.beiwo.klyjaz.entity;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @version 3.1.0
 * @author xhb
 * 底部导航栏 审核状态
 */
public class TabImageBean implements Serializable{

    public List<TabImage> bottomList = new ArrayList<>();

    public int audit;

}
