package vay.enterwind.auto2000samarinda.module.sales;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
    @BindView(R.id.supervisor) TextView supervisor;
    @BindView(R.id.jabatanTelepon) TextView jabatanTelepon;
    @BindView(R.id.namaLengkap) TextView namaLengkap;

    @BindView(R.id.reported) TextView reported;
    @BindView(R.id.remains) TextView remains;
    @BindView(R.id.todayPlans) TextView todayPlans;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_home);
        ButterKnife.bind(this);

        init();

        swipeRefresh.setOnRefreshListener(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        namaLengkap.setText(sessionNama);
        jabatanTelepon.setText(sessionJabatan + " (" + sessionTelepon + ")");
        supervisor.setText("Supervisor: " + sessionSupervisor);
        regional.setText("Regional: " + sessionRegional);

        getServer(sessionEmail);
    }

    @Override
    public void onRefresh() {
        init();
        swipeRefresh.setRefreshing(false);
    }

    private void getServer(String email) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        String widgetUrl = Config.URL_WIDGET + email;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, widgetUrl, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {

                if(response.equals("gagal")) {
                    StyleableToast.makeText(HomeActivity.this, "Koneksi kurang stabil, pastikan Anda terkoneksi dengan internet.", R.style.ToastGagal).show();
                } else {
                    String[] list = response.split(",");

                    todayPlans.setText(list[0] + " Planning");
                    reported.setText(list[1] + " Planning");
                    remains.setText(list[2] + " Planning");

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                StyleableToast.makeText(HomeActivity.this, "Error di Server.", R.style.ToastGagal).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}
