package com.alexanderbaulin.silence.dagger2.modules;

import com.alexanderbaulin.silence.dagger2.components.AppComponent;
import com.alexanderbaulin.silence.dagger2.scopes.MainActivityScope;

import com.alexanderbaulin.silence.mvp.presenter.Presenter;
import com.alexanderbaulin.silence.mvp.view.activities.Main;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class MainActivityModule {
    @Provides
    @MainActivityScope
    com.alexanderbaulin.silence.mvp.interfaces.Presenter getPresenter() {
        return new Presenter();
    }
}
