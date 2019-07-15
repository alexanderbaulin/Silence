package com.alexanderbaulin.silence.dagger2.components;

import com.alexanderbaulin.silence.dagger2.modules.MainActivityModule;
import com.alexanderbaulin.silence.dagger2.scopes.MainActivityScope;
import com.alexanderbaulin.silence.mvp.view.activities.Main;

import dagger.Component;

@MainActivityScope
@Component(modules = { MainActivityModule.class }, dependencies = AppComponent.class)
public interface MainActivityComponent {
    void injectMainActivity(Main mainActivity);
}
