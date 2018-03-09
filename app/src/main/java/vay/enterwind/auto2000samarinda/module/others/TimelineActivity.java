package vay.enterwind.auto2000samarinda.module.others;

import android.content.Context;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

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
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.TimelineAdapter;
import vay.enterwind.auto2000samarinda.models.Orientation;
import vay.enterwind.auto2000samarinda.models.Timeline;
import vay.enterwind.auto2000samarinda.models.Type;
import vay.enterwind.auto2000samarinda.utils.Config;

public class TimelineActivity extends BaseActivity implements TimelineAdapter.OnLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "TimelineActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = TimelineActivity.this;

    private TimelineAdapter mTimeLineAdapter;
    private ArrayList<Timeline> mDataList;

    private RequestQueue requestQueue;
    private int requestCount = 1;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_timeline);
        ButterKnife.bind(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

        initView();

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void initView() {
        mDataList = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mTimeLineAdapter = new TimelineAdapter(this, this);
        mRecyclerView.setAdapter(mTimeLineAdapter);

        swipeRefresh.setOnRefreshListener(this);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager llManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (dy > 0 && llManager.findLastCompletelyVisibleItemPosition() == (mTimeLineAdapter.getItemCount() - 2)) {
                    mTimeLineAdapter.showLoading();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        requestCount = 1;
        getData();
        mTimeLineAdapter.setMore(true);
    }

    @Override
    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefresh.setRefreshing(false);
                requestCount = 1;
                getData();
                mTimeLineAdapter.setMore(true);
            }
        }, 2000);
    }

    private void getData() {
        requestQueue.add(getDataFromServer(requestCount));
        requestCount++;
    }

    private JsonArrayRequest getDataFromServer(final int requestCount) {
        return new JsonArrayRequest(Config.URL_TIMELINE+ String.valueOf(requestCount),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: "+response);
                        parseData(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mTimeLineAdapter.dismissLoading();
                    }
                });
    }

    private void parseData(JSONArray array) {
        mDataList.clear();

        if(array.length() == 0) {
            // rlKosong.setVisibility(View.VISIBLE);
        }

        for (int i = 0; i < array.length(); i++) {
            JSONObject json;
            try {
                json = array.getJSONObject(i);

                Type type = null;

                // Kondisi Jenis Prospek
                if(json.getString(Config.TAG_STATUS).equals("1")) {
                    type = Type.NONE;
                }
                if(json.getString(Config.TAG_STATUS).equals("2")) {
                    type = Type.DOT;
                }
                if(json.getString(Config.TAG_STATUS).equals("3")) {
                    type = Type.FULL;
                }

                mDataList.add(new Timeline(
                        json.getString(Config.TAG_PESAN),
                        json.getString(Config.TAG_CREATED_AT),
                        type
                ));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        mTimeLineAdapter.addAll(mDataList);
        mTimeLineAdapter.dismissLoading();
        mTimeLineAdapter.addItemMore(mDataList);
        mTimeLineAdapter.setMore(true);
    }

    @Override
    public void onLoadMore() {
        getData();
    }
}
