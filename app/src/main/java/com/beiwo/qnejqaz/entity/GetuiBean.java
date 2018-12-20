package com.beiwo.qnejqaz.entity;

import java.io.Serializable;

/**
 * Copyright: zhujia (C)2018
 * FileName: GetuiBean
 * Author: jiang
 * Create on: 2018/7/26 15:40
 * Description: TODO
 */
public class GetuiBean implements Serializable {

    /**
     * content : 京东白条第1期，应还333元，将于1天后到期，点击查看。
     * data : {"amount":"333","date":"2018-07-26 16:00:00","logo":"http://axgj-test.oss-cn-hangzhou.aliyuncs.com/liabilities_channel/aa578f77e6f64675b47ebec54ac6abf2.png","name":"京东白条","termNo":"1","title":"1天后到期"}
     * dataType : 1
     * messageId : 8585fe085d6f43b78900af465b0f92be
     * title : 账单即将到期
     * type : 6
     */

    private String content;
    private DataBean data;
    private int dataType;
    private String messageId;
    private String title;
    private int type;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public static class DataBean {
        /**
         * amount : 333
         * date : 2018-07-26 16:00:00
         * logo : http://axgj-test.oss-cn-hangzhou.aliyuncs.com/liabilities_channel/aa578f77e6f64675b47ebec54ac6abf2.png
         * name : 京东白条
         * termNo : 1
         * title : 1天后到期
         */

        private String amount;
        private String date;
        private String logo;
        private String name;
        private int termNo;
        private String title;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getTermNo() {
            return termNo;
        }

        public void setTermNo(int termNo) {
            this.termNo = termNo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
