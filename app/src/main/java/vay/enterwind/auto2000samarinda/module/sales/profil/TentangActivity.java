package vay.enterwind.auto2000samarinda.module.sales.profil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.R;

public class TentangActivity extends AppCompatActivity {

    @BindView(R.id.btnBack) ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }
}
