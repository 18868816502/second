package com.beiwo.klyjaz.entity;

import java.util.List;

/**
 * @author chenguoguo
 * @name loanmarket
 * @class name：com.beiwo.klyjaz.entity
 * @descripe
 * @time 2018/12/12 15:38
 */
public class GoodsManageBean {


    /**
     * total : 297
     * rows : [{"manageId":"01547a260f094e22b1581d889fbde212","initials":null,"name":"享乐花","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/a48f3933e7d44b05bd5a584108da44e0.png","auditMethod":"机审","term":"7-30天","minQuota":"200","maxQuota":"5000","rate":"0.03%/日","loanSpeed":"6分钟","status":1,"gmtCreate":null,"gmtModify":1544585671000,"introduce":"享乐花，享受借钱的欢乐！","creditContext":"无"},{"manageId":"02ebf6eb396f42478b2863711aa174e5","initials":"Y","name":"盈盈有钱","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/c94cf224ff994938a66149cfdb3d49c8.png","auditMethod":"人工审核","term":"1月1期，可分12期","minQuota":"100","maxQuota":"5000","rate":"0.09847%","loanSpeed":"当天放款","status":1,"gmtCreate":1544420080000,"gmtModify":1544585671000,"introduce":"低息大额消费分期平台 最高5W","creditContext":"不需要"},{"manageId":"032f21603bbe4017a67bf601cb22cbe4","initials":"S","name":"速有钱","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/34dd32f1b9274ade800e889f8d6575f3.png","auditMethod":"人工审核","term":"7-14天","minQuota":"100","maxQuota":"5000","rate":"0.067%","loanSpeed":"当天放款","status":1,"gmtCreate":1544420085000,"gmtModify":1544585671000,"introduce":"只要身份证，秒到5000","creditContext":"不需要"},{"manageId":"033329be744149d58813485a31b23e86","initials":"J","name":"金象贷","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/dc7516fbb8754bfe8a553d4b472ecfe2.png","auditMethod":"人工审核","term":"7","minQuota":"100","maxQuota":"5000","rate":"0.09%","loanSpeed":"当天放款","status":1,"gmtCreate":1544420077000,"gmtModify":1544585671000,"introduce":"","creditContext":"不需要"},{"manageId":"04b62bf690ad4b53ab521595de6549f0","initials":"L","name":"零用钱","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/c5f740cf979b443ead684081c1d532e7.png","auditMethod":"线上加机审","term":"7天","minQuota":"100","maxQuota":"5000","rate":"0.03","loanSpeed":"1个小时","status":1,"gmtCreate":1544420092000,"gmtModify":1544585671000,"introduce":"高炮口子","creditContext":"无当前逾期"},{"manageId":"053501ecbc8743b6b347004b0a5ecfe2","initials":"J","name":"集合信","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/6bf06a1f3135494da0ef02601ff55d46.png","auditMethod":"无审核","term":"7","minQuota":"100","maxQuota":"5000","rate":"0.03%/日","loanSpeed":"3分钟","status":1,"gmtCreate":1544420074000,"gmtModify":1544585671000,"introduce":"小额现金贷，秒下款","creditContext":"无"},{"manageId":"06558d392a614422a43e172de1471b01","initials":"M","name":"马上贷","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/6e34e2b1013d49448decf96f9686f22f.png","auditMethod":"系统审核","term":"3-18期","minQuota":"100","maxQuota":"5000","rate":"1.53%/月","loanSpeed":"1个工作日","status":1,"gmtCreate":1544420149000,"gmtModify":1544585671000,"introduce":"8000元现金 最快三分钟放贷","creditContext":"需要"},{"manageId":"0749abc75c8246d8af05740f2ad44fea","initials":"X","name":"小蜜蜂","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/fba4055668f1411c87e5bdd95a8b31e4.png","auditMethod":"人工+系统","term":"7天","minQuota":"100","maxQuota":"5000","rate":"0.05%","loanSpeed":"当天","status":1,"gmtCreate":1544420118000,"gmtModify":1544585671000,"introduce":"秒下款 高通过率 操作简单","creditContext":"不查不上"},{"manageId":"08682a0abcc640b9aeabc0a5c9846ac8","initials":"Q","name":"取款鸡","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/9e4170dd0c544afca31dea5f66eea4aa.png","auditMethod":"人工审核","term":"1-9个月","minQuota":"100","maxQuota":"5000","rate":"1.15%（月息）","loanSpeed":"当天放款","status":1,"gmtCreate":1544420073000,"gmtModify":1544585671000,"introduce":"额度高，无抵押，快至5分钟放款","creditContext":"不需要"},{"manageId":"09d30939836e401ab4b6f9718417a48b","initials":"X","name":"小带鱼","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/8cccd273e09a458390ff6de6ea41223d.png","auditMethod":"自动审核","term":"7天","minQuota":"100","maxQuota":"5000","rate":"0.027%/日起","loanSpeed":"","status":1,"gmtCreate":1544420083000,"gmtModify":1544585671000,"introduce":"","creditContext":"无"}]
     */

    private int total;
    private List<RowsBean> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<RowsBean> getRows() {
        return rows;
    }

    public void setRows(List<RowsBean> rows) {
        this.rows = rows;
    }

    public static class RowsBean {
        /**
         * manageId : 01547a260f094e22b1581d889fbde212
         * initials : null
         * name : 享乐花
         * logo : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/Spider/a48f3933e7d44b05bd5a584108da44e0.png
         * auditMethod : 机审
         * term : 7-30天
         * minQuota : 200
         * maxQuota : 5000
         * rate : 0.03%/日
         * loanSpeed : 6分钟
         * status : 1
         * gmtCreate : null
         * gmtModify : 1544585671000
         * introduce : 享乐花，享受借钱的欢乐！
         * creditContext : 无
         */

        private String manageId;
        private Object initials;
        private String name;
        private String logo;
        private String auditMethod;
        private String term;
        private String minQuota;
        private String maxQuota;
        private String rate;
        private String loanSpeed;
        private int status;
        private Object gmtCreate;
        private long gmtModify;
        private String introduce;
        private String creditContext;

        public String getManageId() {
            return manageId;
        }

        public void setManageId(String manageId) {
            this.manageId = manageId;
        }

        public Object getInitials() {
            return initials;
        }

        public void setInitials(Object initials) {
            this.initials = initials;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getAuditMethod() {
            return auditMethod;
        }

        public void setAuditMethod(String auditMethod) {
            this.auditMethod = auditMethod;
        }

        public String getTerm() {
            return term;
        }

        public void setTerm(String term) {
            this.term = term;
        }

        public String getMinQuota() {
            return minQuota;
        }

        public void setMinQuota(String minQuota) {
            this.minQuota = minQuota;
        }

        public String getMaxQuota() {
            return maxQuota;
        }

        public void setMaxQuota(String maxQuota) {
            this.maxQuota = maxQuota;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getLoanSpeed() {
            return loanSpeed;
        }

        public void setLoanSpeed(String loanSpeed) {
            this.loanSpeed = loanSpeed;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(Object gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public long getGmtModify() {
            return gmtModify;
        }

        public void setGmtModify(long gmtModify) {
            this.gmtModify = gmtModify;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getCreditContext() {
            return creditContext;
        }

        public void setCreditContext(String creditContext) {
            this.creditContext = creditContext;
        }
    }
}
