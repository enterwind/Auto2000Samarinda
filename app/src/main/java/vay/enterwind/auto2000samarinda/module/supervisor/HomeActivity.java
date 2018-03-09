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

    @BindView(R.id.regional)
    TextView regional;
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
        setContentView(R.layout.activity_svp_home);
        ButterKnife.bind(this);

        init();

        swipeRefresh.setOnRefreshListener(this);
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        session = new AuthManagement(getApplicationContext());
        HashMap<String, String> detail = session.getUserDetails();

        namaLengkap.setText(detail.get(AuthManagement.KEY_NAMA));
        jabatanTelepon.setText(detail.get(AuthManagement.KEY_JABATAN) + " (" + detail.get(AuthManagement.KEY_TELEPON) + ")");
        supervisor.setText("Supervisor: " + detail.get(AuthManagement.KEY_SUPERVISOR));
        regional.setText("Regional: " + detail.get(AuthManagement.KEY_REGIONAL));

        getServer(detail.get(AuthManagement.KEY_EMAIL));
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

    @Override
    public void onRefresh() {
        init();
        swipeRefresh.setRefreshing(false);
    }
}
