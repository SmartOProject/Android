package com.android.smarto.di;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import javax.inject.Scope;

/**
 * Created by Anatoly Chernyshev on 12.02.2018.
 */

@ActivityScope
@Retention(RetentionPolicy.RUNTIME)
public @interface ActivityScope {
}
