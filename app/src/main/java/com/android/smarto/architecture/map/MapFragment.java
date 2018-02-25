package com.android.smarto.architecture.map;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.smarto.R;
import com.android.smarto.architecture.base.BaseFragment;
import com.android.smarto.db.model.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, IMapFragment {

    @Inject
    MapPresenter<IMapFragment> mMapPresenter;

    GoogleMap mGoogleMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        mMapPresenter.onAttach(this);
        SupportMapFragment mapFragment = (SupportMapFragment)
                this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return v;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMapPresenter.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mMapPresenter.onMapReady();
    }

    @Override
    public void setupMap(List<User> friendList, User currentUser) {

        mGoogleMap.addMarker(new MarkerOptions()
        .position(new LatLng(currentUser.getLatitude(), currentUser.getLongitude()))
        .title("You"));

        for (User user: friendList){

            mGoogleMap.addMarker(new MarkerOptions()
            .position(new LatLng(user.getLatitude(), user.getLongitude()))
            .anchor(0.5f, 0.5f)
            .title(user.getName()));

        }
        mGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(currentUser.getLatitude(), currentUser.getLongitude()), 14.0f));

    }
}
