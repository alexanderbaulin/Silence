package com.alexanderbaulin.silence.dagger2.modules;

import android.content.Context;

import com.alexanderbaulin.silence.mvp.interfaces.Model;
import com.alexanderbaulin.silence.mvp.model.Data;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private final Context context;

    public AppModule(Context ctx) {
        context = ctx;
    }

    @Provides
    @Singleton
    Context context() {
        return context;
    }

    @Provides
    @Singleton
    Model getData() {
        return new Data();
    }
}
