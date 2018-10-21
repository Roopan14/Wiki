package com.example.roopanc.wiki.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class JsonData {

    @SerializedName("batchcomplete")
    @Expose
    private Boolean batchcomplete;
    @SerializedName("continue")
    @Expose
    private Continue _continue;
    @SerializedName("query")
    @Expose
    private Query query;

    /**
     * No args constructor for use in serialization
     *
     */
    public JsonData() {
    }

    /**
     *
     * @param query
     * @param batchcomplete
     * @param _continue
     */
    public JsonData(Boolean batchcomplete, Continue _continue, Query query) {
        super();
        this.batchcomplete = batchcomplete;
        this._continue = _continue;
        this.query = query;
    }

    public Boolean getBatchcomplete() {
        return batchcomplete;
    }

    public void setBatchcomplete(Boolean batchcomplete) {
        this.batchcomplete = batchcomplete;
    }

    public Continue getContinue() {
        return _continue;
    }

    public void setContinue(Continue _continue) {
        this._continue = _continue;
    }

    public Query getQuery() {
        return query;
    }

    public void setQuery(Query query) {
        this.query = query;
    }

}
