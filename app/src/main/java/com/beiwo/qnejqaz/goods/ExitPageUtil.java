package com.beiwo.qnejqaz.goods;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * https://gitee.com/tangbuzhi
 *
 * @author: Tangbuzhi
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/12/18
 */
public class ExitPageUtil {
    private static Map<String, String> map() {
        Map<String, String> map = new HashMap<>();
        map.put("TabHomeFragment", "HomePage");
        map.put("TabLoanFragment", "LoanPage");
        map.put("SocialRecomFragment", "CommunityHomePage");
        map.put("ToolFragment", "ToolHomePage");
        map.put("PersonalFragment", "MyPage");
        map.put("ProTypeActivity1", "NewProductsDivision");
        map.put("ProTypeActivity2", "LightningAccountDivision");
        map.put("ProTypeActivity4", "NoCreditReportingDivision");
        map.put("LoanGoodsActivity", "LoanRecommendDivision");
        map.put("GoodsDetailActivity", "LoanRecommendProductDetail");
        map.put("GoodsListActivity", "LoanRecommendProductSelect");
        map.put("GoodsPublishCommentActivity", "LoanRecommendProductPraise");
        map.put("ForumPublishActivity", "CommunityPublishPage");
        map.put("TopicDetailActivity", "TopicDetailPage");
        map.put("BillListActivity", "TallyHomePage");
        map.put("NetLoanDetailActivity", "LoanBillPage");
        return map;
    }

    public static String getPageName(String pageName) {
        String result = map().get(pageName);
        //System.out.println("first = " + result);
        if (TextUtils.isEmpty(result)) result = "HomePage";
        return result;
    }
}