package com.alexanderbaulin.silence.dagger2.components;

import android.content.Context;

import com.alexanderbaulin.silence.dagger2.modules.AppModule;
import com.alexanderbaulin.silence.mvp.interfaces.Model;
import com.alexanderbaulin.silence.mvp.presenter.Presenter;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = { AppModule.class})
public interface AppComponent {
    Context getContext();
    void injectPresenter(Presenter presenter);
}
