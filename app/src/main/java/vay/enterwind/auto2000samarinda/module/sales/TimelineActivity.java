package vay.enterwind.auto2000samarinda.module.sales;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.adapter.TimelineAdapter;
import vay.enterwind.auto2000samarinda.models.Orientation;
import vay.enterwind.auto2000samarinda.models.Timeline;
import vay.enterwind.auto2000samarinda.models.Type;

public class TimelineActivity extends BaseActivity {

    private static final String TAG = "TimelineActivity";
    private static final int ACTIVITY_NUM = 3;
    private Context mContext = TimelineActivity.this;

    private RecyclerView mRecyclerView;
    private TimelineAdapter mTimeLineAdapter;
    private List<Timeline> mDataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_timeline);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(true);

//        initView();

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

//    private void initView() {
//        setDataListItems();
//        mTimeLineAdapter = new TimelineAdapter(mDataList, Orientation.VERTICAL, true);
//        mRecyclerView.setAdapter(mTimeLineAdapter);
//    }
//
//    private void setDataListItems(){
//        mDataList.add(new Timeline("Item successfully delivered", "", Type.NONE));
//        mDataList.add(new Timeline("Courier is out to delivery your order", "2017-02-12 08:00", Type.DOT));
//        mDataList.add(new Timeline("Item has reached courier facility at New Delhi", "2017-02-11 21:00", Type.FULL));
//        mDataList.add(new Timeline("Item has been given to the courier", "2017-02-11 18:00", Type.FULL));
//        mDataList.add(new Timeline("Item is packed and will dispatch soon", "2017-02-11 09:30", Type.FULL));
//        mDataList.add(new Timeline("Order is being readied for dispatch", "2017-02-11 08:00", Type.FULL));
//        mDataList.add(new Timeline("Order processing initiated", "2017-02-10 15:00", Type.FULL));
//        mDataList.add(new Timeline("Order confirmed by seller", "2017-02-10 14:30", Type.FULL));
//        mDataList.add(new Timeline("Order placed successfully", "2017-02-10 14:00", Type.FULL));
//    }

}
