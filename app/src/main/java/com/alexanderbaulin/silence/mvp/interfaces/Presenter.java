package com.alexanderbaulin.silence.mvp.interfaces;

import com.alexanderbaulin.silence.mvp.model.DataItem;

import java.util.LinkedList;

public interface Presenter {
    long add(DataItem newDataItem, int i);
    void update(DataItem item, int position);

    void cancelAlarm(DataItem updatedItem, int position);

    void startAlarm(DataItem updatedItem, int position);

    LinkedList<DataItem> getData();

    void delete(long id);
}
