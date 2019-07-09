package com.alexanderbaulin.silence.mvp.interfaces;

import com.alexanderbaulin.silence.mvp.model.DataItem;

public interface Model {
    void insert(DataItem newDataItem, int index);

    void update(DataItem item);
}
