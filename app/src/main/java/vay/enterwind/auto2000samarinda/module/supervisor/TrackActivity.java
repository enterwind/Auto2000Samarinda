package vay.enterwind.auto2000samarinda.module.supervisor;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;

public class TrackActivity extends BaseActivity {
    private static final String TAG = "TrackActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = TrackActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svp_track);
        ButterKnife.bind(this);

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }
}
