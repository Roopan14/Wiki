package com.example.roopanc.wiki;

import com.example.roopanc.wiki.data.DataItem;

public interface MyCallbacks {
    void adapterCallback(DataItem dataItem, boolean saveToRecent);
}
