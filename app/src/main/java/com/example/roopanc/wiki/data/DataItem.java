package com.example.roopanc.wiki.data;

import android.os.Parcel;
import android.os.Parcelable;

public class DataItem implements Parcelable {

    private String pageid, title, description, thumbnailurl;

    public DataItem(String pageid, String title, String description, String thumbnailurl) {
        this.pageid = pageid;
        this.title = title;
        this.description = description;
        this.thumbnailurl = thumbnailurl;
    }

    public String getPageid() {
        return pageid;
    }

    public void setPageid(String pageid) {
        this.pageid = pageid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailurl() {
        return thumbnailurl;
    }

    public void setThumbnailurl(String thumbnailurl) {
        this.thumbnailurl = thumbnailurl;
    }

    private DataItem(Parcel in) {
        pageid = in.readString();
        title = in.readString();
        description = in.readString();
        thumbnailurl = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pageid);
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnailurl);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<DataItem> CREATOR = new Parcelable.Creator<DataItem>() {
        @Override
        public DataItem createFromParcel(Parcel in) {
            return new DataItem(in);
        }

        @Override
        public DataItem[] newArray(int size) {
            return new DataItem[size];
        }
    };
}
