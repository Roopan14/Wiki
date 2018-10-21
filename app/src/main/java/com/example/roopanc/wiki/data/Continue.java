package com.example.roopanc.wiki.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Continue {

    @SerializedName("gpsoffset")
    @Expose
    private Integer gpsoffset;
    @SerializedName("continue")
    @Expose
    private String _continue;

    /**
     * No args constructor for use in serialization
     *
     */
    public Continue() {
    }

    /**
     *
     * @param gpsoffset
     * @param _continue
     */
    public Continue(Integer gpsoffset, String _continue) {
        super();
        this.gpsoffset = gpsoffset;
        this._continue = _continue;
    }

    public Integer getGpsoffset() {
        return gpsoffset;
    }

    public void setGpsoffset(Integer gpsoffset) {
        this.gpsoffset = gpsoffset;
    }

    public String getContinue() {
        return _continue;
    }

    public void setContinue(String _continue) {
        this._continue = _continue;
    }

    @Override
    public String toString() {
        return "Continue{" +
                "gpsoffset=" + gpsoffset +
                ", _continue='" + _continue + '\'' +
                '}';
    }
}
