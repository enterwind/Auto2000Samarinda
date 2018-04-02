package vay.enterwind.auto2000samarinda.module.supervisor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.FeedListAdapter;
import vay.enterwind.auto2000samarinda.adapter.TimelineAdapter;
import vay.enterwind.auto2000samarinda.models.FeedItem;
import vay.enterwind.auto2000samarinda.models.Timeline;
import vay.enterwind.auto2000samarinda.models.Type;
import vay.enterwind.auto2000samarinda.module.others.references.AddReferenceActivity;
import vay.enterwind.auto2000samarinda.utils.AppController;
import vay.enterwind.auto2000samarinda.utils.Config;
import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class TimelineActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "TimelineActivity";
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = TimelineActivity.this;

    SpotsDialog dialog;

    @BindView(R.id.refreshLayout) SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.btnAdd) FloatingActionButton btnAdd;
    @BindView(R.id.kosong) CardView kosong;

    @BindView(R.id.list) ListView listView;
    private FeedListAdapter listAdapter;
    private List<FeedItem> feedItems;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spv_timeline);
        ButterKnife.bind(this);
        swipeRefresh.setOnRefreshListener(this);
        dialog = new SpotsDialog(this);
        dialog.show();

        initFeed();
        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    private void initFeed() {
        feedItems = new ArrayList<FeedItem>();
        listAdapter = new FeedListAdapter(this, feedItems);
        listView.setAdapter(listAdapter);

        JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
                Config.URL_TIMELINE + "1", null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.d(TAG, "Response: " + response.toString());
                if (response != null) {
                    parseJsonFeed(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonReq);
    }

    private void parseJsonFeed(JSONObject response) {
        try {
            JSONArray feedArray = response.getJSONArray("feed");
            if(feedArray.length() == 0) {
                kosong.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                for (int i = 0; i < feedArray.length(); i++) {
                    JSONObject feedObj = (JSONObject) feedArray.get(i);

                    FeedItem item = new FeedItem();
                    item.setId(feedObj.getInt("id"));
                    item.setName(feedObj.getString("nama"));
                    item.setStatus(feedObj.getString("pesan"));
                    item.setProfilePic(feedObj.getString("foto"));
                    item.setTimeStamp(feedObj.getString("created_at"));

                    feedItems.add(item);
                }
                listAdapter.notifyDataSetChanged();
            }
            dialog.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        finish();
        startActivity(getIntent());
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    @OnClick(R.id.btnAdd) void onAdd() {
        startActivityForResult(new Intent(mContext, AddReferenceActivity.class), 1);
    }
}
