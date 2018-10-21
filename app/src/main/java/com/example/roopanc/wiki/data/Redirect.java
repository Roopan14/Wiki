package com.example.roopanc.wiki.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Redirect {

    @SerializedName("index")
    @Expose
    private Integer index;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;

    /**
     * No args constructor for use in serialization
     *
     */
    public Redirect() {
    }

    /**
     *
     * @param to
     * @param index
     * @param from
     */
    public Redirect(Integer index, String from, String to) {
        super();
        this.index = index;
        this.from = from;
        this.to = to;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

}