package com.beiwo.qnejqaz.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class News {

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

    public static class Row implements Parcelable {
        private String explain;
        private String image;
        private List<String> imageList;
        private int pv;
        private String source;
        private long gmtCreate;
        private String title;
        private String id;
        private int showType;

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<String> getImageList() {
            return imageList;
        }

        public void setImageList(List<String> imageList) {
            this.imageList = imageList;
        }

        public int getPv() {
            return pv;
        }

        public void setPv(int pv) {
            this.pv = pv;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
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

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getShowType() {
            return showType;
        }

        public void setShowType(int showType) {
            this.showType = showType;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.explain);
            dest.writeString(this.image);
            dest.writeStringList(this.imageList);
            dest.writeInt(this.pv);
            dest.writeString(this.source);
            dest.writeLong(this.gmtCreate);
            dest.writeString(this.title);
            dest.writeString(this.id);
            dest.writeInt(this.showType);
        }

        public Row() {
        }

        protected Row(Parcel in) {
            this.explain = in.readString();
            this.image = in.readString();
            this.imageList = in.createStringArrayList();
            this.pv = in.readInt();
            this.source = in.readString();
            this.gmtCreate = in.readLong();
            this.title = in.readString();
            this.id = in.readString();
            this.showType = in.readInt();
        }

        public static final Parcelable.Creator<Row> CREATOR = new Parcelable.Creator<Row>() {
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
