package com.alexanderbaulin.silence.mvp.model;

import com.alexanderbaulin.silence.MyApp;
import com.alexanderbaulin.silence.mvp.interfaces.Model;
import com.alexanderbaulin.silence.mvp.model.database.DataBase;

import java.util.List;

public class Data implements Model {

    private DataBase db = new DataBase(MyApp.getAppContext());
    private Alarm alarm = new Alarm();

    public void insert(DataItem item, int index) {
        db.insert(item);
        alarm.setAlarm(item, index);
    }

    public void update(DataItem item) {
        db.update(item.id, item);
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
