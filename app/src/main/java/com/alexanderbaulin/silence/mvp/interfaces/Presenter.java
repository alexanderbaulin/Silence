package com.alexanderbaulin.silence.mvp.interfaces;

import com.alexanderbaulin.silence.mvp.model.DataItem;

public interface Presenter {
    void add(DataItem newDataItem, int i);
    void update(DataItem item, int position);

    void cancelAlarm(DataItem updatedItem, int position);

    void startAlarm(DataItem updatedItem, int position);
}
