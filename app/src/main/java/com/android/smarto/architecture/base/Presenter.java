package com.android.smarto.architecture.base;

import com.android.smarto.app.App;
import com.android.smarto.prefs.ISharedPreferencesRepository;
import com.android.smarto.utils.UtilityWrapper;

/**
 * Created by Anatoly Chernyshev on 30.01.2018.
 */

public abstract class Presenter <V extends BaseView> implements BasePresenter<V> {

    private V mView;
    private UtilityWrapper mUtilityWrapper;

    public Presenter() {
        mUtilityWrapper = new UtilityWrapper();
        App.get().getApplicationComponent().inject(mUtilityWrapper);
    }

    @Override
    public void onAttach(V view) {
        mView = view;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    protected V getView(){
        return mView;
    }

    public ISharedPreferencesRepository getPrefs(){
        return mUtilityWrapper.getmSharedPreferencesRepository();
    }

}
