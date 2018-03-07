package vay.enterwind.auto2000samarinda.pubnub.example.locationpublish;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

/**
 * Created by novay on 07/03/18.
 */

public class LocationPublishMapAdapter {
    private static final String TAG = LocationPublishMapAdapter.class.getName();

    private Marker marker;
    private Activity activity;
    private GoogleMap map;

    public LocationPublishMapAdapter(Activity activity, GoogleMap map) {
        this.activity = activity;
        this.map = map;
    }

    public void locationUpdated(final Map<String, String> newLocation) {
        if (newLocation.containsKey("lat") && newLocation.containsKey("lng")) {
            String lat = newLocation.get("lat");
            String lng = newLocation.get("lng");

            doUiUpdate(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng)));
        } else {
            Log.w(TAG, "message ignored: " + newLocation.toString());
        }
    }

    private void doUiUpdate(final LatLng location) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (marker != null) {
                    marker.setPosition(location);
                } else {
                    marker = map.addMarker(new MarkerOptions().position(location));
                }

                map.moveCamera(CameraUpdateFactory.newLatLng(location));
            }
        });
    }
}
