package vay.enterwind.auto2000samarinda.module.sales;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import butterknife.ButterKnife;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;

public class CheckpointActivity extends BaseActivity {

    private static final String TAG = "CheckpointActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = CheckpointActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_checkpoint);
        ButterKnife.bind(this);

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }
}
