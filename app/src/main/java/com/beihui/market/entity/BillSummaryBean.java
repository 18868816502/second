package com.beihui.market.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Copyright: zhujia (C)2018
 * FileName: BillSummaryBean
 * Author: jiang
 * Create on: 2018/7/20 14:47
 * Description: 账单汇总数据类
 */
public class BillSummaryBean implements Serializable {
        /**
         * totalLiAmount : 30713.83
         * overLiAmount : 936.19
         * personBillItem : [{"recordId":"01bdcdf3bbd54a9ca6d686b9873d269c","title":"布衣钱包","iconId":null,"totalAmount":1200,"month":null,"totalTerm":1,"logoUrl":null,"type":1}]
         */

        private String totalLiAmount;
        private String overLiAmount;
        private List<PersonBillItemBean> personBillItem;

        public String getTotalLiAmount() {
            return totalLiAmount;
        }

        public void setTotalLiAmount(String totalLiAmount) {
            this.totalLiAmount = totalLiAmount;
        }

        public String getOverLiAmount() {
            return overLiAmount;
        }

        public void setOverLiAmount(String overLiAmount) {
            this.overLiAmount = overLiAmount;
        }

        public List<PersonBillItemBean> getPersonBillItem() {
            return personBillItem;
        }

        public void setPersonBillItem(List<PersonBillItemBean> personBillItem) {
            this.personBillItem = personBillItem;
        }

        public static class PersonBillItemBean {
            /**
             * recordId : 01bdcdf3bbd54a9ca6d686b9873d269c
             * title : 布衣钱包
             * iconId : null
             * totalAmount : 1200
             * month : null
             * totalTerm : 1
             * logoUrl : null
             * type : 1
             */

            private String recordId;
            private String title;
            private Object iconId;
            private String totalAmount;
            private Object month;
            private String totalTerm;
            private Object logoUrl;
            private String type;

            public String getRecordId() {
                return recordId;
            }

            public void setRecordId(String recordId) {
                this.recordId = recordId;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public Object getIconId() {
                return iconId;
            }

            public void setIconId(Object iconId) {
                this.iconId = iconId;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public Object getMonth() {
                return month;
            }

            public void setMonth(Object month) {
                this.month = month;
            }

            public String getTotalTerm() {
                return totalTerm;
            }

            public void setTotalTerm(String totalTerm) {
                this.totalTerm = totalTerm;
            }

            public Object getLogoUrl() {
                return logoUrl;
            }

            public void setLogoUrl(Object logoUrl) {
                this.logoUrl = logoUrl;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

        }

}
