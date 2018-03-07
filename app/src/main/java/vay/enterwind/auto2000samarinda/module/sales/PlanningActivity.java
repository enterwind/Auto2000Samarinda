package vay.enterwind.auto2000samarinda.module.sales;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
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
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.PlanAdapter;
import vay.enterwind.auto2000samarinda.models.Plan;
import vay.enterwind.auto2000samarinda.module.sales.plans.AddPlanActivity;
import vay.enterwind.auto2000samarinda.module.sales.plans.MapsActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahPasswordActivity;
import vay.enterwind.auto2000samarinda.utils.Config;

public class PlanningActivity extends BaseActivity {

    private static final String TAG = "PlanningActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = PlanningActivity.this;

    @BindView(R.id.btnAdd) FloatingActionButton btnAdd;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.kosong) CardView kosong;

    private List<Plan> planList = new ArrayList<>();
    private PlanAdapter mAdapter;

    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_planning);
        ButterKnife.bind(this);
        progress = ProgressDialog.show(PlanningActivity.this, "Loading...", "Tunggu Sebentar");

        init();

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void init() {
        recyclerView.setAdapter(null);
        mAdapter = new PlanAdapter(this, planList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        prepareData();
    }

    private void prepareData() {

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Config.URL_PLAN + sessionEmail,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if(response.length() == 0) {
                            kosong.setVisibility(View.VISIBLE);
                        }
                        planList.clear();
                        for(int i = 0; i<response.length(); i++){
                            JSONObject obj;
                            try {
                                obj = response.getJSONObject(i);
                                Plan plan = new Plan(
                                        obj.getString(Config.TAG_NAMA),
                                        obj.getString(Config.TAG_ALAMAT),
                                        obj.getString(Config.TAG_STATUS),
                                        obj.getString(Config.TAG_UUID));
                                planList.add(plan);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        progress.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);
    }

    @OnClick(R.id.btnAdd) void onAdd() {
        startActivityForResult(new Intent(mContext, AddPlanActivity.class), 1);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                init();
            }
        }
    }
}
