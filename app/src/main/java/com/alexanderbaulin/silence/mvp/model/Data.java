package com.alexanderbaulin.silence.mvp.model;

import com.alexanderbaulin.silence.MyApp;
import com.alexanderbaulin.silence.mvp.interfaces.Model;
import com.alexanderbaulin.silence.mvp.model.database.DataBase;

import java.util.LinkedList;
import java.util.List;

public class Data implements Model {

    private DataBase db = new DataBase(MyApp.getAppContext());
    private Alarm alarm = new Alarm();

    public long insert(DataItem item, int index) {
        long key = db.insert(item);
        alarm.setAlarm(item, index);

        return key;
    }

    public void update(DataItem item) {
        db.update(item.id, item);
    }

    @Override
    public void cancel(DataItem updatedItem, int position) {
        alarm.cancel(updatedItem, position);
    }

    @Override
    public void startAlarm(DataItem updatedItem, int position) {
        alarm.setAlarm(updatedItem, position);
    }

    @Override
    public LinkedList<DataItem> getData() {
        return db.select();
    }

    @Override
    public void delete(long id) {
        db.delete(id);
    }

    public void delete(DataItem item) {
        db.delete(item.id);
    }

    public List<DataItem> select() {
        return db.select();
    }

    public void setAlarm(DataItem item, int index) {
        alarm.setAlarm(item, index);
    }

    public void cancelAlarm(DataItem item, int index) {
        alarm.cancel(item, index);
    }
}
