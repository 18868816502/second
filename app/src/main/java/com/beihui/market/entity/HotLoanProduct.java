package com.beihui.market.entity;


import java.util.List;

public class HotLoanProduct {
    private int total;
    private int pageNo;
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

    public List<LoanProduct.Row> getRows() {
        return rows;
    }

    public void setRows(List<LoanProduct.Row> rows) {
        this.rows = rows;
    }
}
