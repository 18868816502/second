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

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(status);
        }

        public Row() {
            super();
        }

        protected Row(Parcel in) {
            super(in);
            status = in.readInt();
        }
    }
}
