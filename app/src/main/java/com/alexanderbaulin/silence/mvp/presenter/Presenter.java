package com.alexanderbaulin.silence.mvp.presenter;

import com.alexanderbaulin.silence.MyApp;
import com.alexanderbaulin.silence.dagger2.components.AppComponent;
import com.alexanderbaulin.silence.mvp.interfaces.Model;
import com.alexanderbaulin.silence.mvp.model.Data;
import com.alexanderbaulin.silence.mvp.model.DataItem;

import java.util.LinkedList;

import javax.inject.Inject;

public class Presenter implements com.alexanderbaulin.silence.mvp.interfaces.Presenter {

    @Inject
    Model model;

    public Presenter() {
        AppComponent component = MyApp.getComponent();
        if(component != null) component.injectPresenter(this);
    }

    @Override
    public long add(DataItem newDataItem, int index) {
        return model.insert(newDataItem, index);

    }

    @Override
    public void update(DataItem item, int position) {
        model.update(item);
        if (item.isAlarmOn)
            model.startAlarm(item, position);
    }

    @Override
    public void cancelAlarm(DataItem updatedItem, int position) {
        model.cancel(updatedItem, position);
    }

    @Override
    public void startAlarm(DataItem updatedItem, int position) {
        model.startAlarm(updatedItem, position);

    }

    @Override
    public LinkedList<DataItem> getData() {
        return model.getData();
    }

    @Override
    public void delete(long id) {
        model.delete(id);
    }
}
