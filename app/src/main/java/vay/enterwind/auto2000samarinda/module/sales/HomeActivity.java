package vay.enterwind.auto2000samarinda.module.sales;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.session.AuthManagement;

public class HomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;

    @BindView(R.id.regional) TextView regional;
    @BindView(R.id.supervisor) TextView supervisor;
    @BindView(R.id.jabatanTelepon) TextView jabatanTelepon;
    @BindView(R.id.namaLengkap) TextView namaLengkap;

    @BindView(R.id.reported) TextView reported;
    @BindView(R.id.remains) TextView remains;
    @BindView(R.id.todayPlans) TextView todayPlans;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;

    AuthManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_home);
        ButterKnife.bind(this);

        init();

        swipeRefresh.setOnRefreshListener(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void init() {
        session = new AuthManagement(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetails();

        namaLengkap.setText(detail.get(AuthManagement.KEY_NAMA));
        jabatanTelepon.setText(detail.get(AuthManagement.KEY_JABATAN) + " (" + detail.get(AuthManagement.KEY_TELEPON) + ")");
        supervisor.setText("Supervisor: " + detail.get(AuthManagement.KEY_SUPERVISOR));
        regional.setText("Regional: " + detail.get(AuthManagement.KEY_REGIONAL));

    }

    @Override
    public void onRefresh() {
        init();
        swipeRefresh.setRefreshing(false);
    }
}
