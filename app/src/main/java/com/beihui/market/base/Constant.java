package com.beihui.market.base;


import com.beihui.market.App;
import com.beihui.market.util.update.FileUtils;

public class Constant {

    public static String BASE_PATH = FileUtils.createRootPath(App.getInstance()) + "/aixin/";
    public static String PATH_DATA = FileUtils.createRootPath(App.getInstance()) + "/cache";


    public static int DEFAULT_FILTER_MONEY = 50000;

}
