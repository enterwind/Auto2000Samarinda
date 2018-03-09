package vay.enterwind.auto2000samarinda;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.HashMap;

import vay.enterwind.auto2000samarinda.module.others.HomeActivity;
import vay.enterwind.auto2000samarinda.session.AuthManagement;

public class MainActivity extends AppCompatActivity {

    public AuthManagement session;
    String sessionAkses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new AuthManagement(getApplicationContext());
        session.checkLogin();


        HashMap<String, String> detail = session.getUserDetails();
        sessionAkses = detail.get(AuthManagement.KEY_AKSES);

        if(session.isLoggedIn()) {
            switch (sessionAkses) {
                case "1":
                    startActivity(new Intent(MainActivity.this, vay.enterwind.auto2000samarinda.module.supervisor.HomeActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case "2":
                    startActivity(new Intent(MainActivity.this, vay.enterwind.auto2000samarinda.module.sales.HomeActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case "3":
                case "4":
                    startActivity(new Intent(MainActivity.this, vay.enterwind.auto2000samarinda.module.service.HomeActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
                case "5":
                    startActivity(new Intent(MainActivity.this, HomeActivity.class));
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                    break;
            }
        }
    }
}
