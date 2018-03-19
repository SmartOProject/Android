package com.im.smarto.ui.map;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.im.smarto.R;
import com.im.smarto.network.models.ContactPosition;
import com.im.smarto.ui.base.BaseFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import javax.inject.Inject;

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
    public void setupMap(List<ContactPosition> positions, ContactPosition currentUser) {
        for (ContactPosition position: positions) {
            mGoogleMap.addMarker(new MarkerOptions()
            .position(new LatLng(position.getLatitude(), position.getLongitude()))
            .anchor(0.5f, 0.5f)
            .title(position.getName()));
        }

        if (currentUser == null) mGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(positions.get(0).getLatitude(), positions.get(0).getLongitude()), 14.0f));
        else mGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(currentUser.getLatitude(), currentUser.getLongitude()), 14.0f));
    }

}
