package com.alexanderbaulin.silence.mvp.interfaces;

import com.alexanderbaulin.silence.mvp.model.DataItem;

import java.util.LinkedList;

public interface Model {
    long insert(DataItem newDataItem, int index);

    void update(DataItem item);

    void cancel(DataItem updatedItem, int position);

    void startAlarm(DataItem updatedItem, int position);

    LinkedList<DataItem> getData();

    void delete(long id);
}
