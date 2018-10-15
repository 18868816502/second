package com.beiwo.klyjaz.entity;


import java.util.List;

public class HotLoanProduct {
    private int total;
    private int pageNo;
    //一键借款按钮 0.不显示 1.显示
    private int button;
    private List<LoanProduct.Row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getButton() {
        return button;
    }

    public void setButton(int button) {
        this.button = button;
    }

    public List<LoanProduct.Row> getRows() {
        return rows;
    }

    public void setRows(List<LoanProduct.Row> rows) {
        this.rows = rows;
    }
}
