package vay.enterwind.auto2000samarinda.pubnub.example.locationsubscribe;

import android.util.Log;

import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.SubscribeCallback;
import com.pubnub.api.models.consumer.PNStatus;
import com.pubnub.api.models.consumer.pubsub.PNMessageResult;
import com.pubnub.api.models.consumer.pubsub.PNPresenceEventResult;

import java.util.LinkedHashMap;
import java.util.Map;

import vay.enterwind.auto2000samarinda.pubnub.example.util.JsonUtil;

/**
 * Created by novay on 05/03/18.
 */

public class LocationSubscribePnCallback extends SubscribeCallback {

    private static final String TAG = LocationSubscribePnCallback.class.getName();
    private LocationSubscribeMapAdapter locationMapAdapter;
    private String watchChannel;

    public LocationSubscribePnCallback(LocationSubscribeMapAdapter locationMapAdapter, String watchChannel) {
        this.locationMapAdapter = locationMapAdapter;
        this.watchChannel = watchChannel;
    }

    @Override
    public void status(PubNub pubnub, PNStatus status) {
        Log.d(TAG, "status: " + status.toString());
    }

    @Override
    public void message(PubNub pubnub, PNMessageResult message) {
        if (!message.getChannel().equals(watchChannel)) {
            return;
        }

        try {
            Log.d(TAG, "message: " + message.toString());

            Map<String, String> newLocation = JsonUtil.fromJson(message.getMessage().toString(), LinkedHashMap.class);
            locationMapAdapter.locationUpdated(newLocation);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void presence(PubNub pubnub, PNPresenceEventResult presence) {
        if (!presence.getChannel().equals(watchChannel)) {
            return;
        }

        Log.d(TAG, "presence: " + presence.toString());
    }

}
