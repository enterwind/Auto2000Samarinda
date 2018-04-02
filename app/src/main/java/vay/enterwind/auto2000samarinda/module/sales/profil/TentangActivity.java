package vay.enterwind.auto2000samarinda.module.sales.profil;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import vay.enterwind.auto2000samarinda.R;

public class TentangActivity extends AppCompatActivity {

    @BindView(R.id.btnBack) ImageView btnBack;
    @BindView(R.id.btnUpdate) Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tentang);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnBack) void onBack() {
        finish();
    }

    @OnClick(R.id.btnUpdate) void onUpdate() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=vay.enterwind.auto2000samarinda"));
        startActivity(intent);
    }
}
