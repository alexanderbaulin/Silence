package com.alexanderbaulin.silence.mvp.presenter;

import com.alexanderbaulin.silence.mvp.interfaces.Model;
import com.alexanderbaulin.silence.mvp.model.Alarm;
import com.alexanderbaulin.silence.mvp.model.Data;
import com.alexanderbaulin.silence.mvp.model.DataItem;

public class Presenter implements com.alexanderbaulin.silence.mvp.interfaces.Presenter {

    private Model model = new Data();

    @Override
    public void add(DataItem newDataItem, int index) {
        model.insert(newDataItem, index);

    }

    @Override
    public void update(DataItem item) {
        model.update(item);
    }
}
