package vay.enterwind.auto2000samarinda.module.supervisor;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.PlanAdapter;
import vay.enterwind.auto2000samarinda.adapter.ReportAdapter;
import vay.enterwind.auto2000samarinda.models.Plan;
import vay.enterwind.auto2000samarinda.models.Sales;
import vay.enterwind.auto2000samarinda.utils.Config;

public class ReportActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "ReportActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = ReportActivity.this;

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.kosong) CardView kosong;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;

    private List<Sales> salesList = new ArrayList<>();
    private ReportAdapter mAdapter;

    SpotsDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spv_report);
        ButterKnife.bind(this);
        swipeRefresh.setOnRefreshListener(this);
        dialog = new SpotsDialog(this);
        dialog.show();

        init();

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void init() {
        recyclerView.setAdapter(null);
        mAdapter = new ReportAdapter(this, salesList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();
    }

    private void prepareData() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.URL_SALES + sessionEmail,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() == 0) {
                            kosong.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                        }
                        salesList.clear();
                        for(int i = 0; i<response.length(); i++){
                            JSONObject obj;
                            try {
                                obj = response.getJSONObject(i);
                                Sales sales = new Sales(
                                        obj.getString(Config.TAG_EMAIL),
                                        obj.getString(Config.TAG_FOTO),
                                        obj.getString(Config.TAG_NAMA),
                                        obj.getString(Config.TAG_TELEPON),
                                        obj.getString(Config.TAG_ALAMAT),
                                        obj.getString(Config.TAG_STATUS),
                                        obj.getString("planning"),
                                        obj.getString("reported"),
                                        obj.getString("remains")
                                );
                                salesList.add(sales);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        dialog.dismiss();
                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
