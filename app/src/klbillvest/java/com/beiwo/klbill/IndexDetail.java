package com.beiwo.klbill;

/**
 *
 * @author:
 * @version:
 * @package:
 * @description:
 * @modify:
 * @date: 2018/8/6
 */

public class IndexDetail {
    private String desc;
    private String detail;
    private String title;
    private int type;

    public String getDesc() {
        return this.desc;
    }

    public String getDetail() {
        return this.detail;
    }

    public String getTitle() {
        return this.title;
    }

    public int getType() {
        return this.type;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "IndexDetail [desc=" + desc + ", detail=" + detail + ", title="
                + title + ", type=" + type + "]";
    }


}
