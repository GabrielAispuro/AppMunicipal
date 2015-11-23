package com.example.gabrielaispuro.appmadero.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.gabrielaispuro.appmadero.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseObject;

import java.util.List;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private double[] lat;
    private double[] lng;
    private String method;
    private String names[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Intent intent = getIntent();
        lat = intent.getDoubleArrayExtra("latitude");
        lng = intent.getDoubleArrayExtra("longitude");
        method = intent.getStringExtra("method");
        names = intent.getStringArrayExtra("names");
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        if(method.equals("nearest")) {
            String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);

            //Compare distances to find out a nearest point
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {

                    int position = 0;
                    double latitude = lat[0];
                    double longitude = lng[0];

                    Location locationCD = new Location("");
                    locationCD.setLatitude(latitude);
                    locationCD.setLongitude(longitude);

                    float distance = location.distanceTo(locationCD);

                    for(int i = 1;i < lat.length; i++){

                        Location locationAux = new Location("");
                        locationAux.setLatitude(lat[i]);
                        locationAux.setLongitude(lng[i]);

                        float distanceAux = location.distanceTo(locationAux);

                        if(distanceAux < distance){

                            position = i;
                            locationCD = locationAux;

                        }

                    }

                    LatLng nearestCD = new LatLng(locationCD.getLatitude(), locationCD.getLongitude());

                    //Adding a mark in the google map
                    mMap.addMarker(new MarkerOptions().position(nearestCD).title(names[position]));
                    //Move the camera instantly to the nearest point with a zoom of 15
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(nearestCD, 15));
                    mMap.setOnMyLocationChangeListener(null);

                }
            });
            //mMap.setOnMyLocationChangeListener(null);

        }

        if(method.equals("report")){


            PackageManager pm = getPackageManager();
            boolean hasGps = pm.hasSystemFeature(PackageManager.FEATURE_LOCATION_GPS);

            if(hasGps){

                mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                    @Override
                    public void onMyLocationChange(Location location) {

                        LatLng point = new LatLng(location.getLatitude(), location.getLongitude());

                        //Adding a mark in the google map
                        Marker myMarker =mMap.addMarker(new MarkerOptions().position(point).draggable(true));
                        //Move the camera instantly to the nearest point with a zoom of 15
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
                        mMap.setOnMyLocationChangeListener(null);

                        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
                            @Override
                            public void onMarkerDragStart(Marker marker) {

                            }

                            @Override
                            public void onMarkerDrag(Marker marker) {

                            }

                            @Override
                            public void onMarkerDragEnd(Marker marker) {
                                LatLng newPoint = marker.getPosition();

                                Bundle bundle = new Bundle();
                                bundle.putDouble("latitude", newPoint.latitude);
                                bundle.putDouble("longitude", newPoint.longitude);
                                Intent myIntent = new Intent();
                                myIntent.putExtras(bundle);
                                setResult(RESULT_OK, myIntent);
                                finish();
                            }
                        });

                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", point.latitude);
                        bundle.putDouble("longitude", point.longitude);
                        Intent myIntent = new Intent();
                        myIntent.putExtras(bundle);
                        setResult(RESULT_OK, myIntent);


                    }
                });
            } else {

                LatLng point = new LatLng(22.2682275, -97.8243127);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 13));
                mMap.addMarker(new MarkerOptions().position(point).draggable(true));

                mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener(){
                    @Override
                    public void onMarkerDragStart(Marker marker) {

                    }

                    @Override
                    public void onMarkerDrag(Marker marker) {

                    }

                    @Override
                    public void onMarkerDragEnd(Marker marker) {
                        LatLng newPoint = marker.getPosition();

                        Bundle bundle = new Bundle();
                        bundle.putDouble("latitude", newPoint.latitude);
                        bundle.putDouble("longitude", newPoint.longitude);
                        Intent myIntent = new Intent();
                        myIntent.putExtras(bundle);
                        setResult(RESULT_OK, myIntent);
                        finish();
                    }
                });

                Bundle bundle = new Bundle();
                bundle.putDouble("latitude", point.latitude);
                bundle.putDouble("longitude", point.longitude);
                Intent myIntent = new Intent();
                myIntent.putExtras(bundle);
                setResult(RESULT_OK, myIntent);

            }

        }

    }

}
