package com.android.smarto.architecture.map;

import com.android.smarto.architecture.base.BaseView;
import com.android.smarto.db.model.User;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 25.02.2018.
 */

public interface IMapFragment extends BaseView {

    void setupMap(List<User> friendList, User currentUser);

}
