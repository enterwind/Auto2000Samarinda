package vay.enterwind.auto2000samarinda;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonElement;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.PubNubException;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.enums.PNStatusCategory;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.presence.PNHereNowChannelData;
import com.pubnub.api.models.consumer.presence.PNHereNowOccupantData;
import com.pubnub.api.models.consumer.presence.PNHereNowResult;
import com.pubnub.api.models.consumer.presence.PNSetStateResult;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import org.json.JSONException;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import vay.enterwind.auto2000samarinda.module.others.HomeActivity;
import vay.enterwind.auto2000samarinda.pubnub.Constants;
import vay.enterwind.auto2000samarinda.pubnub.example.*;
import vay.enterwind.auto2000samarinda.session.AuthManagement;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    public AuthManagement session;
    String sessionAkses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionStart();
    }

    private void sessionStart() {
        session = new AuthManagement(getApplicationContext());
        session.checkLogin();


        HashMap<String, String> detail = session.getUserDetails();
        sessionAkses = detail.get(AuthManagement.KEY_AKSES);

        if (session.isLoggedIn()) {
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
