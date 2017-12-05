package com.beihui.market.entity;


import android.os.Parcel;

import java.util.List;

public class MyProduct {

    private int total;
    private int success;
    private List<MyProduct.Row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public List<MyProduct.Row> getRows() {
        return rows;
    }

    public void setRows(List<MyProduct.Row> rows) {
        this.rows = rows;
    }

    public static class Row extends LoanProduct.Row {
        private int status;
        private String url;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Row() {
            super();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.status);
            dest.writeString(this.url);
        }

        protected Row(Parcel in) {
            super(in);
            this.status = in.readInt();
            this.url = in.readString();
        }

        public static final Creator<Row> CREATOR = new Creator<Row>() {
            @Override
            public Row createFromParcel(Parcel source) {
                return new Row(source);
            }

            @Override
            public Row[] newArray(int size) {
                return new Row[size];
            }
        };
    }
}
