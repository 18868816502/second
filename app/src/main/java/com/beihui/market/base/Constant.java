package com.beihui.market.base;


import com.beihui.market.App;
import com.beihui.market.util.update.FileUtils;

public class Constant {

    //线上环境还是测试环境
    public static  boolean isOnline = true;


    public static final String API_BASE_URL = isOnline ? "https://api.aixinqianbao.com.cn:9191"
            : "http://116.62.247.223:80";



    public static String BASE_PATH = FileUtils.createRootPath(App.getInstance()) + "/aixin/";
    public static String PATH_DATA = FileUtils.createRootPath(App.getInstance()) + "/cache";





}
