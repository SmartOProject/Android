package com.im.smarto.ui.map;

import com.im.smarto.network.models.ContactPosition;
import com.im.smarto.ui.base.BaseView;

import java.util.List;

/**
 * Created by Anatoly Chernyshev on 25.02.2018.
 */

public interface IMapFragment extends BaseView {

    void setupMap(List<ContactPosition> positions, ContactPosition currentUser);

}
