package com.beihui.market.entity;


import java.util.List;

public class ReNews {
    private int total;
    private List<Row> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }

    public class Row {
        private String explain;
        private int readSum;
        private String image;
        private int httpType;
        private String id;
        private long gmtCreate;
        private String title;

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public int getReadSum() {
            return readSum;
        }

        public void setReadSum(int readSum) {
            this.readSum = readSum;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getHttpType() {
            return httpType;
        }

        public void setHttpType(int httpType) {
            this.httpType = httpType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public long getGmtCreate() {
            return gmtCreate;
        }

        public void setGmtCreate(long gmtCreate) {
            this.gmtCreate = gmtCreate;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
