package com.example.roopanc.wiki.data;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Terms {

    @SerializedName("description")
    @Expose
    private List<String> description = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Terms() {
    }

    /**
     *
     * @param description
     */
    public Terms(List<String> description) {
        super();
        this.description = description;
    }

    public List<String> getDescription() {
        return description;
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

}
