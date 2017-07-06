package com.beihui.market.base;


import com.beihui.market.App;
import com.beihui.market.util.SPUtils;
import com.beihui.market.util.update.FileUtils;

public class Constant {

    /**
     * 线上线下
     */
    public static  boolean isOnline = SPUtils.isOnLine(App.getInstance());

    /**
     *
     * 打马甲包需要修改
     *  (1).先修改当前majia的值
     *  (2).AndroidManifest.xml(icon)，
     *  (3).String.xml中选择string
     *  (4).build.gradle中的包名,渠道包
     *  (5).assess中CustomConfig.properties修改名称
     *  (6).打包 aixin.jks  aixinjs.jks aixinqq.jks 密码：123456
     *
     *
     *
     ********************************************************************
     *
     * 0 正式
     * 1 马甲包-芝麻现金  打包文件 aixinqq.jks  包名：com.beihui.aixinqqqq
     * 2 2345急速贷 打包文件 aixinjs.jks 包名：com.beihui.aixinjs
     *
     ********************************************************************
     */

    /**
     * 判断是测试环境还是自己配置的环境，默认测试环境
     */
    public static int majia = 0;



    public static  boolean isCeshiOnline = SPUtils.isCeshiOnLine(App.getInstance());

    public static String ceshiUrl = isCeshiOnline ? SPUtils.getBaseUrl(App.getInstance()) :"http://116.62.247.223:80";


    //http://116.62.247.223:80
    public static final String API_BASE_URL = isOnline ? "https://api.aixinqianbao.com.cn:9191"
            : ceshiUrl;



    //服务器配置头部   /s1
    public static final String HEADER_URL = "/s1";
    public static final String API_RETURN_URL = isOnline ? API_BASE_URL + HEADER_URL
            : ceshiUrl + HEADER_URL;


    public static final String HEADER_URL_S4 = "/s4";

    //http://192.168.1.141:8881
    //http://116.62.221.120
    //http://api1.aixinqianbao.com.cn
    public static final String API_BASE_H5URL = isOnline ? "http://api1.aixinqianbao.com.cn" + HEADER_URL_S4
            : ceshiUrl + HEADER_URL_S4;


    public static final String accessKey = isOnline ? "699b9305418757ef9a26e5a32ca9dbfb"
            : "699b9305418757ef9a26e5a32ca9dbfb";

    public static final String secretKey = isOnline ? "0bca3e8e2baa42218040c5dbf6978f315e104e5c"
            : "0bca3e8e2baa42218040c5dbf6978f315e104e5c";


    /**
     * 阿里云上传图像前缀地址
     */
    public static final String image_user = isOnline ? "aixin-fws"
            : "zhixun-test";

    /**
     * 阿里云上传前缀
     */
    public static final String image_head = isOnline ? "http://oss-cn-hangzhou.aliyuncs.com"
            : "http://oss-cn-shanghai.aliyuncs.com";






    public static String IsNetWorkError = "当前网络不可用！";
    public static String IsServiceError = "请求超时，服务器异常！";

    /**
     * 后端返回成功的code
     */
    public static String CODE_SUCCESS = "1000000";




    public static String BASE_PATH = FileUtils.createRootPath(App.getInstance()) + "/aixin/";
    public static String PATH_DATA = FileUtils.createRootPath(App.getInstance()) + "/cache";





}
