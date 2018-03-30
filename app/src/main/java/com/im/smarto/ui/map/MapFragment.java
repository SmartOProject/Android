package com.im.smarto.ui.map;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Marker;
import com.im.smarto.Constants;
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

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapFragment extends BaseFragment implements OnMapReadyCallback, IMapFragment {

    private static final String TAG = MapFragment.class.getSimpleName();

    private static final int LOCATION_REQUEST = 1111;

    @Inject
    MapPresenter<IMapFragment> mMapPresenter;

    GoogleMap mGoogleMap;
    Marker mCurrentUserMarker;

    @OnClick(R.id.fab_my_location)
    void onClick(){
        mMapPresenter.onMyLocationClicked();
    }

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, v);
        mMapPresenter.onAttach(this);
        mMapPresenter.onCreate();
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
    public void setupMap(List<ContactPosition> positions) {
        for (ContactPosition position: positions) {
            mGoogleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(position.getLatitude(), position.getLongitude()))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .anchor(0.5f, 0.5f)
                    .title(position.getName()));
        }

        mGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(positions.get(0).getLatitude(), positions.get(0).getLongitude()), 14.0f));
    }

    @Override
    public void checkLocationPermissions() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                            Manifest.permission.ACCESS_COARSE_LOCATION},
                                            LOCATION_REQUEST);
        } else {
            mMapPresenter.onPermissionsGranted();
        }
    }

    @Override
    public void showPermissionsDeniedMessage() {
        Toast.makeText(getActivity(), "Permissions denied!", Toast.LENGTH_LONG).show();
    }

    @Override
    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void showCurrentPosition(Location location) {

        if (mCurrentUserMarker != null) mCurrentUserMarker.remove();

        mCurrentUserMarker = mGoogleMap.addMarker(new MarkerOptions()
        .position(new LatLng(location.getLatitude(), location.getLongitude()))
        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        .title("I`m"));
        mGoogleMap.moveCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 14.0f));
    }

    @Override
    public void showLocationSettingsDialog() {

        final LocationManager manager = (LocationManager) getActivity().getSystemService( Context.LOCATION_SERVICE );

        if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            mMapPresenter.startLocationCheck();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("GPS Setting`s");
        builder.setMessage("GPS is disabled. Do you want to enable it?");
        builder.setPositiveButton("OK", (dialog, which) -> {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            dialog.dismiss();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.dismiss();
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void showCurrentUserPosition(Location location) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, String.valueOf(requestCode));
        switch (requestCode) {
            case LOCATION_REQUEST:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "result granted");
                    mMapPresenter.onPermissionsGranted();
                } else {
                    Log.i(TAG, "result not granted");
                    mMapPresenter.onPermissionsDenied();
                }
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
