package vay.enterwind.auto2000samarinda.pubnub.example;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.google.common.collect.ImmutableList;
import com.pubnub.api.PubNub;

import vay.enterwind.auto2000samarinda.pubnub.example.flightpaths.FlightPathsTabContentFragment;
import vay.enterwind.auto2000samarinda.pubnub.example.locationpublish.LocationPublishTabContentFragment;
import vay.enterwind.auto2000samarinda.pubnub.example.locationsubscribe.LocationSubscribeTabContentFragment;

/**
 * Created by novay on 07/03/18.
 */

public class MainActivityTabManager extends FragmentStatePagerAdapter {

    private final LocationSubscribeTabContentFragment locationSubscribe;
    private final LocationPublishTabContentFragment locationPublish;
    private final FlightPathsTabContentFragment flightPathsTabContentFragment;

    private ImmutableList<Fragment> items;

    public MainActivityTabManager(FragmentManager fm, int NumOfTabs, PubNub pubNub) {
        super(fm);

        this.locationSubscribe = new LocationSubscribeTabContentFragment();
        this.locationSubscribe.setPubNub(pubNub);

        this.locationPublish = new LocationPublishTabContentFragment();
        this.locationPublish.setPubNub(pubNub);

        this.flightPathsTabContentFragment = new FlightPathsTabContentFragment();
        this.flightPathsTabContentFragment.setPubNub(pubNub);

        this.items = ImmutableList.of((Fragment) locationSubscribe, (Fragment) locationPublish, (Fragment) flightPathsTabContentFragment);
    }

    @Override
    public Fragment getItem(int position) {
        return this.items.get(position);
    }

    @Override
    public int getCount() {
        return this.items.size();
    }

}
