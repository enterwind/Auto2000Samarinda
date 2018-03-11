package vay.enterwind.auto2000samarinda.module.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.session.AuthManagement;
import vay.enterwind.auto2000samarinda.utils.Config;

public class HomeActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "HomeActivity";
    private static final int ACTIVITY_NUM = 0;
    private Context mContext = HomeActivity.this;

    @BindView(R.id.regional) TextView regional;
    @BindView(R.id.jabatanTelepon) TextView jabatanTelepon;
    @BindView(R.id.namaLengkap) TextView namaLengkap;

    @BindView(R.id.onlineSales) TextView onlineSales;
    @BindView(R.id.offlineSales) TextView offlineSales;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;

    AuthManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svp_home);
        ButterKnife.bind(this);

        init();

        swipeRefresh.setOnRefreshListener(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        namaLengkap.setText(sessionNama);
        jabatanTelepon.setText(sessionJabatan + " (" + sessionTelepon + ")");
        regional.setText("Regional: " + sessionRegional);

    }

    @Override
    public void onRefresh() {
        init();
        swipeRefresh.setRefreshing(false);
    }

}
