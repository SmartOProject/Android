package com.android.smarto.di.module;

import com.android.smarto.architecture.authentication.AuthPresenter;
import com.android.smarto.architecture.authentication.IAuthContract;
import com.android.smarto.architecture.registration.IRegisterContract;
import com.android.smarto.architecture.registration.RegisterPresenter;
import com.android.smarto.architecture.splash.ISplashContract;
import com.android.smarto.architecture.splash.SplashPresenter;
import com.android.smarto.di.scope.PresenterScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

@Module
@PresenterScope
public class PresenterModule {

    @Provides
    @PresenterScope
    ISplashContract.ISplashPresenter <ISplashContract.ISplashActivity> getSplashPresenter(){
        return new SplashPresenter<>();
    }

    @Provides
    @PresenterScope
    IAuthContract.IAuthPresenter <IAuthContract.IAuthActivity> getAuthPresenter(){
        return new AuthPresenter<>();
    }

    @Provides
    @PresenterScope
    IRegisterContract.IRegisterPresenter<IRegisterContract.IRegisterActivity> getRegisterPresenter(){
        return new RegisterPresenter<>();
    }

}
