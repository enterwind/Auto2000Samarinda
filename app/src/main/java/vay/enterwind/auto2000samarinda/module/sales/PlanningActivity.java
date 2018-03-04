package vay.enterwind.auto2000samarinda.module.sales;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.BaseActivity;
import vay.enterwind.auto2000samarinda.R;
import vay.enterwind.auto2000samarinda.module.sales.plans.AddPlanActivity;
import vay.enterwind.auto2000samarinda.module.sales.profil.UbahPasswordActivity;

public class PlanningActivity extends BaseActivity {

    private static final String TAG = "PlanningActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = PlanningActivity.this;

    @BindView(R.id.btnAdd) FloatingActionButton btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_planning);
        ButterKnife.bind(this);

        setupBottomNavigationView(mContext, ACTIVITY_NUM);
    }

    @OnClick(R.id.btnAdd) void onAdd() {
        startActivity(new Intent(mContext, AddPlanActivity.class));
    }
}
