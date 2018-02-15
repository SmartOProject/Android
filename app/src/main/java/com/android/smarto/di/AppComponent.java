package com.android.smarto.di;

import android.content.Context;

import com.android.smarto.app.App;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

/**
 * Created by Anatoly Chernyshev on 12.02.2018.
 */

@Singleton
@Component(modules = { AppModule.class })
public interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder context(Context context);
        AppComponent build();
    }

    void inject(App app);

}
